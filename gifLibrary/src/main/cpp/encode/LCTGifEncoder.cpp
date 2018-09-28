#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <vector>
#include "BaseGifEncoder.h"
#include "LCTGifEncoder.h"
#include "../BitWritingBlock.h"

using namespace std;

LCTGifEncoder::LCTGifEncoder() {
	// init width, height to 1, to prevent divide by zero.
	width = 1;
	height = 1;

	useDither = true;
	frameNum = 0;
	lastPixels = NULL;
	lastColorReducedPixels = NULL;
	fp = NULL;
	lastRootColor = GREEN;
}

LCTGifEncoder::~LCTGifEncoder() {
	release();
}

bool LCTGifEncoder::init(uint16_t width, uint16_t height, const char* fileName) {
	this->width = width;
	this->height = height;

	fp = fopen(fileName, "wb");
	if (NULL == fp) {
		return false;
	}
	if (NULL != lastPixels) {
		delete[] lastPixels;
	}
	lastPixels = new uint32_t[width * height];
	if (NULL != lastColorReducedPixels) {
		delete[] lastColorReducedPixels;
	}
	lastColorReducedPixels = new uint32_t[width * height];

	writeHeader();
	return true;
}

void LCTGifEncoder::release() {
	if (NULL != lastPixels) {
		delete[] lastPixels;
		lastPixels = NULL;
	}

	if (NULL != lastColorReducedPixels) {
		delete[] lastColorReducedPixels;
		lastColorReducedPixels = NULL;
	}

	if (NULL != fp) {
		uint8_t gifFileTerminator = 0x3B;
		fwrite(&gifFileTerminator, 1, 1, fp);
		fclose(fp);
		fp = NULL;
	}
}

void LCTGifEncoder::setDither(bool useDither) {
		this->useDither = useDither;
}

uint16_t LCTGifEncoder::getWidth() {
		return width;
}

uint16_t LCTGifEncoder::getHeight() {
	return height;
}

void LCTGifEncoder::setThreadCount(int32_t threadCount) { }

void LCTGifEncoder::removeSamePixels(uint8_t* src1, uint8_t* src2, EncodeRect* rect)
{
	int32_t bytesPerLine = width * 4;
	int32_t beginY = 0;
	for (; beginY < height - 1; ++beginY) {
		if (0 != memcmp(src1 + bytesPerLine * beginY, src2 + bytesPerLine * beginY, bytesPerLine)) {
			break;
		}
	}
	int32_t endY = height - 1;
	for (; beginY + 1 <= endY; --endY) {
		if (0 != memcmp(src1 + bytesPerLine * endY, src2 + bytesPerLine * endY, bytesPerLine)) {
			break;
		}
	}
	++endY;

	int32_t lastY = width * height;
	bool isSame = true;
	int32_t beginX = 0;
	for (; beginX < width - 1 && isSame; ++beginX) {
		isSame = true;
		for (int32_t y = 0; y < lastY; y += width) {
			if (((uint32_t*)src1)[y + beginX] != ((uint32_t*)src2)[y + beginX]) {
				isSame = false;
				break;
			}
		}
	}
	--beginX;
	isSame = true;
	int32_t endX = width - 1;
	for (; beginX + 1 <= endX && isSame; --endX) {
		isSame = true;
		for (int32_t y = 0; y < lastY; y += width) {
			if (((uint32_t*)src1)[y + endX] != ((uint32_t*)src2)[y + endX]) {
				isSame = false;
				break;
			}
		}
	}
	++endX;

	rect->x = beginX;
	rect->y = beginY;
	rect->width = endX - beginX + 1;
	rect->height = endY - beginY;
}

void LCTGifEncoder::writeHeader()
{
	fwrite("GIF89a", 6, 1, fp);
	writeLSD();
}

bool LCTGifEncoder::writeLSD()
{
	// logical screen size
	fwrite(&width, 2, 1, fp);
	fwrite(&height, 2, 1, fp);

	// packed fields
	uint8_t gctFlag = 0; // 1 : global color table flag
	uint8_t colorResolution = 8; // only 8 bit
	uint8_t oderedFlag = 0;
	uint8_t gctSize = 0;
	uint8_t packed = (gctFlag << 7) | ((colorResolution - 1) << 4) | (oderedFlag << 3) | gctSize;
	fwrite(&packed, 1, 1, fp);

	uint8_t backgroundColorIndex = 0xFF;
	fwrite(&backgroundColorIndex, 1, 1, fp);

	uint8_t aspectRatio = 0;
	fwrite(&aspectRatio, 1, 1, fp);

	return true;
}

bool LCTGifEncoder::writeContents(Cube* cubes, uint8_t* pixels, uint16_t delay, const EncodeRect& encodingRect)
{
	writeNetscapeExt();

	writeGraphicControlExt(delay);
	writeFrame(cubes, pixels, encodingRect);

	return true;
}

bool LCTGifEncoder::writeNetscapeExt()
{
	//                                   code extCode,                                                            size,       loop count, end
	const uint8_t netscapeExt[] = {0x21, 0xFF, 0x0B, 'N', 'E', 'T', 'S', 'C', 'A', 'P', 'E', '2', '.', '0', 0x03, 0x01, 0x00, 0x00, 0x00};
	fwrite(netscapeExt, sizeof(netscapeExt), 1, fp);
	return true;
}

bool LCTGifEncoder::writeGraphicControlExt(uint16_t delay)
{
	uint8_t disposalMethod = 2; // dispose
	uint8_t userInputFlag = 0; // User input is not expected.
	uint8_t transparencyFlag = 1; // Transparent Index is given.

	uint8_t packed = (disposalMethod << 2) | (userInputFlag << 1) | transparencyFlag;
	//                                                     size, packed, delay(bb), transIndex, terminator
	const uint8_t graphicControlExt[] = {0x21, 0xF9, 0x04, packed, (uint8_t)(delay & 0xFF), (uint8_t)(delay >> 8), 0xFF, 0x00};
	fwrite(graphicControlExt, sizeof(graphicControlExt), 1, fp);
	return true;
}

bool LCTGifEncoder::writeFrame(Cube* cubes, uint8_t* pixels, const EncodeRect& encodingRect)
{
	uint8_t code = 0x2C;
	fwrite(&code, 1, 1, fp);
	uint16_t ix = encodingRect.x;
	uint16_t iy = encodingRect.y;
	uint16_t iw = encodingRect.width;
	uint16_t ih = encodingRect.height;
	uint8_t localColorTableFlag = 1;
	uint8_t interlaceFlag = 0;
	uint8_t sortFlag = 0;
	uint8_t sizeOfLocalColorTable = 7;
	uint8_t packed = (localColorTableFlag << 7) | (interlaceFlag << 6) | (sortFlag << 5) | sizeOfLocalColorTable;
	fwrite(&ix, 2, 1, fp);
	fwrite(&iy, 2, 1, fp);
	fwrite(&iw, 2, 1, fp);
	fwrite(&ih, 2, 1, fp);
	fwrite(&packed, 1, 1, fp);

	writeLCT(2 << sizeOfLocalColorTable, cubes);
	writeBitmapData(pixels, encodingRect);
	return true;
}

bool LCTGifEncoder::writeLCT(int32_t colorNum, Cube* cubes)
{
	uint32_t color;
	Cube* cube;
	for (int32_t i = 0; i < colorNum; ++i) {
		cube = cubes + i;
		color = cube->color[RED] | (cube->color[GREEN] << 8) | (cube->color[BLUE] << 16);
		fwrite(&color, 3, 1, fp);
	}
	return true;
}

bool LCTGifEncoder::writeBitmapData(uint8_t* pixels, const EncodeRect& encodingRect)
{
	uint32_t pixelNum = width * height;
	uint8_t* endPixels = pixels + (encodingRect.y + encodingRect.height - 1) * width + encodingRect.x + encodingRect.width;
	uint8_t dataSize = 8;
	uint32_t codeSize = dataSize + 1;
	uint32_t codeMask = (1 << codeSize) - 1;
	BitWritingBlock writingBlock;
	fwrite(&dataSize, 1, 1, fp);

	vector<uint16_t> lzwInfoHolder;
	lzwInfoHolder.resize(MAX_STACK_SIZE * BYTE_NUM);
	uint16_t* lzwInfos = &lzwInfoHolder[0];

	pixels = pixels + width * encodingRect.y + encodingRect.x;
	uint8_t* rowStart = pixels;
	uint32_t clearCode = 1 << dataSize;
	writingBlock.writeBits(clearCode, codeSize);
	uint32_t infoNum = clearCode + 2;
	uint16_t current = *pixels;
	uint8_t endOfImageData = 0;

	++pixels;
	if (encodingRect.width <= pixels - rowStart) {
		rowStart = rowStart + width;
		pixels = rowStart;
	}

	uint16_t* next;
	while (endPixels > pixels) {
		next = &lzwInfos[current * BYTE_NUM + *pixels];
		if (0 == *next || *next >= MAX_STACK_SIZE) {
			writingBlock.writeBits(current, codeSize);

			*next = infoNum;
			if (infoNum < MAX_STACK_SIZE) {
				++infoNum;
			} else {
				writingBlock.writeBits(clearCode, codeSize);
				infoNum = clearCode + 2;
				codeSize = dataSize + 1;
				codeMask = (1 << codeSize) - 1;
				memset(lzwInfos, 0, MAX_STACK_SIZE * BYTE_NUM * sizeof(uint16_t));
			}
			if (codeMask < infoNum - 1 && infoNum < MAX_STACK_SIZE) {
				++codeSize;
				codeMask = (1 << codeSize) - 1;
			}
			if (endPixels <= pixels) {
				break;
			}
			current = *pixels;
		} else {
			current = *next;
		}
		++pixels;
		if (encodingRect.width <= pixels - rowStart) {
			rowStart = rowStart + width;
			pixels = rowStart;
		}
	}
	writingBlock.writeBits(current, codeSize);
	writingBlock.toFile(fp);
	fwrite(&endOfImageData, 1, 1, fp);

	return true;
}

void LCTGifEncoder::encodeFrame(uint32_t* pixels, int32_t delayMs) {
	uint32_t pixelNum = width * height;
	EncodeRect imageRect;
	imageRect.x = 0;
	imageRect.y = 0;
	imageRect.width = width;
	imageRect.height = height;

	memcpy(lastPixels, pixels, pixelNum * sizeof(uint32_t));

	Cube cubes[256] = {0, };
	computeColorTable(pixels, cubes, width * height);
	reduceColor(cubes, 255, pixels);
	writeContents(cubes, (uint8_t*)pixels, delayMs / 10, imageRect);

	++frameNum;
}

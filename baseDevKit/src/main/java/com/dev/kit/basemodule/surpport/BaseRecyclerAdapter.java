package com.dev.kit.basemodule.surpport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.util.ToastUtil;

import java.util.List;

/**
 * BaseRecyclerAdapter
 * Created by cuiyan on 16-10-20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected List<T> dataList;
    protected Context context;
    protected int itemViewLayoutId;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public BaseRecyclerAdapter(Context context, List<T> dataList, int itemViewLayoutId) {
        this.context = context;
        this.dataList = dataList;
        this.itemViewLayoutId = itemViewLayoutId;
    }

    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecyclerViewHolder.getViewHolder(context, parent, itemViewLayoutId);
    }

    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        fillData(holder, position);
        if (onItemClickListener != null) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.OnItemLongClick(v, holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public T getItem(int position) {
        return dataList.get(position);
    }

    public abstract void fillData(RecyclerViewHolder holder, int position);

    public void appendData(List<T> expendedData) {
        synchronized (this) {
            if (dataList != null) {
                dataList.addAll(expendedData);
                notifyDataSetChanged();
            }
        }
    }

    public void appendDataAndRefreshLocal(List<T> expendedData) {
        synchronized (this) {
            if (dataList != null) {
                int startPosition = dataList.size();
                dataList.addAll(expendedData);
                notifyItemRangeInserted(startPosition, expendedData.size());
                notifyItemRangeChanged(startPosition, expendedData.size());
            }
        }
    }

    public void insertData(int index, List<T> expendedData) {
        synchronized (this) {
            if (dataList != null) {
                dataList.addAll(index, expendedData);
                notifyDataSetChanged();
            }
        }
    }

    public void appendItem(T item, boolean updateSingleItem) {
        synchronized (this) {
            if (dataList != null) {
                dataList.add(item);
                if (updateSingleItem) {
                    notifyItemInserted(dataList.size() - 1);
                    notifyItemRangeChanged(dataList.size() - 1, 1);
                } else {
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void updateDataList(List<T> dataList) {
        synchronized (this) {
            if (dataList != null) {
                this.dataList.clear();
                this.dataList.addAll(dataList);
                notifyDataSetChanged();
            }
        }
    }

    public void removeItem(T item, boolean updateSingleItem) {
        synchronized (this) {
            int itemPos = dataList.indexOf(item);
            if (itemPos >= 0) {
                dataList.remove(item);
                if (updateSingleItem) {
                    notifyItemRemoved(itemPos);
                    notifyItemRangeChanged(itemPos - 1, dataList.size() - itemPos);
                } else {
                    notifyDataSetChanged();
                }
            }
            if (dataList.size() == 0) {
                notifyDataSetChanged();
            }
        }
    }

    public void clearData() {
        synchronized (this) {
            if (dataList != null) {
                dataList.clear();
                notifyDataSetChanged();
            }
        }
    }

    public void replaceData(int index, T data) {
        synchronized (this) {
            if (dataList != null && dataList.size() >= index + 1) {
                dataList.remove(index);
                dataList.add(index, data);
                notifyDataSetChanged();
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.onItemLongClickListener = longClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View v, int position);
    }

    public List<T> getDataList() {
        return dataList;
    }

    void showToast(final String msg) {
        ToastUtil.showToast(context, msg);
    }

    void showToast(final int msgResourceId) {
        ToastUtil.showToast(context, msgResourceId);
    }
}

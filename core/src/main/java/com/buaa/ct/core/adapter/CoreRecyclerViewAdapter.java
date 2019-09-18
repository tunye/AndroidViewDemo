package com.buaa.ct.core.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.core.bean.BaseEntityOp;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.listener.OnRecycleViewItemClickListener;
import com.buaa.ct.core.view.recyclerview.RecycleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10202 on 2015/10/10.
 */
public abstract class CoreRecyclerViewAdapter<T, V extends CoreRecyclerViewAdapter.MyViewHolder> extends RecyclerView.Adapter<V> {
    protected Context context;
    protected OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    protected List<T> datas;
    protected BaseEntityOp<T> baseEntityOp;

    public CoreRecyclerViewAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener onItemClickListener) {
        onRecycleViewItemClickListener = onItemClickListener;
    }

    public void removeData(T t) {
        removeData(datas.indexOf(t));
    }

    public void removeData(int pos) {
        datas.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addData(T t) {
        datas.add(t);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addData(int pos, T t) {
        datas.add(pos, t);
        notifyItemInserted(pos);
    }

    /**
     * 返回插入数据的位置
     *
     * @param t
     * @return
     */
    public int addDatas(List<T> t) {
        int size = getItemCount();
        datas.addAll(t);
        notifyItemRangeInserted(size, t.size());
        if (baseEntityOp != null) {
            baseEntityOp.saveData(t);
        }
        return size;
    }

    public void setDataSet(List<T> t) {
        datas = new ArrayList<>(t);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    @Override
    public void onBindViewHolder(@NonNull final V holder, int position) {
        holder.itemView.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                onItemClick(holder.getAdapterPosition());
                if (onRecycleViewItemClickListener != null) {
                    onRecycleViewItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            }
        });
    }

    public void onItemClick(int pos) {

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class MyViewHolder extends RecycleViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }
}

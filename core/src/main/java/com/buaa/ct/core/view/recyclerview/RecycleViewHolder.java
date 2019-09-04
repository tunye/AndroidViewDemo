package com.buaa.ct.core.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


/**
 * Created by 10202 on 2015/10/12.
 */
public class RecycleViewHolder extends RecyclerView.ViewHolder implements AnimateViewHolder {

    public RecycleViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(-holder.itemView.getHeight() * 0.3f);
        holder.itemView.setAlpha(0);
    }

    @Override
    public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(holder.itemView)
                .translationY(0)
                .alpha(1)
                .setDuration(300)
                .setListener(listener)
                .start();
    }

    @Override
    public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(holder.itemView)
                .translationY(-holder.itemView.getHeight() * 0.3f)
                .alpha(0)
                .setDuration(300)
                .setListener(listener)
                .start();
    }
}
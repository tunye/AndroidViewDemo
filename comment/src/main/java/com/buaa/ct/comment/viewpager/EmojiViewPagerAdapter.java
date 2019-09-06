package com.buaa.ct.comment.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.buaa.ct.comment.R;
import com.buaa.ct.comment.emoji.EmojiIcon;

import java.util.List;


public class EmojiViewPagerAdapter extends RecyclingPagerAdapter {

    private LayoutInflater inflater;
    private int mEmojiHeight;
    private List<List<EmojiIcon>> mPagers;
    private OnClickEmojiListener mListener;
    private int mChildCount = 0;

    public EmojiViewPagerAdapter(Context context, List<List<EmojiIcon>> pager, int emojiHeight, OnClickEmojiListener listener) {
        inflater = LayoutInflater.from(context);
        mPagers = pager;
        mEmojiHeight = emojiHeight;
        mListener = listener;
    }

    public void setEmojiHeight(int emojiHeight) {
        mEmojiHeight = emojiHeight;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_pager_emoji, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final EmojiAdapter adapter = new EmojiAdapter(container.getContext(), mPagers.get(position), mEmojiHeight);
        vh.gv.setAdapter(adapter);
        vh.gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mListener == null)
                    return;
                if (position == adapter.getCount() - 1) {
                    mListener.onDelete();
                } else {
                    mListener.onEmojiClick(adapter.getItem(position));
                }
            }
        });
        adapter.notifyDataSetChanged();
        return convertView;
    }

    @Override
    public int getCount() {
        return mPagers.size();
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public interface OnClickEmojiListener {
        void onEmojiClick(EmojiIcon emoji);

        void onDelete();
    }

    static class ViewHolder {
        GridView gv;

        public ViewHolder(View v) {
            gv = v.findViewById(R.id.gv_emoji);
        }
    }
}

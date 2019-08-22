/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buaa.ct.comment.emoji;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.buaa.ct.comment.R;

import java.util.List;


class EmojiAdapter extends ArrayAdapter<EmojiIcon> {
    private boolean mUseSystemDefault = false;
    private int mEmojiHeight;

    public EmojiAdapter(Context context, List<EmojiIcon> data, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        mUseSystemDefault = false;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, List<EmojiIcon> data, boolean useSystemDefault, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        mUseSystemDefault = useSystemDefault;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, EmojiIcon[] data, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        mUseSystemDefault = false;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, EmojiIcon[] data, boolean useSystemDefault, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        mUseSystemDefault = useSystemDefault;
        this.mEmojiHeight = emojiHeight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_emoji, null);
            ViewHolder holder = new ViewHolder();
            holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            holder.icon.setUseSystemDefault(mUseSystemDefault);
            v.setTag(holder);
        }
        EmojiIcon emoji = getItem(position);

        ViewHolder holder = (ViewHolder) v.getTag();
        holder.icon.setText(emoji.getEmoji());
        if (position == 27) {
            holder.icon.setBackgroundResource(R.drawable.btn_del_selector);
        } else {
            holder.icon.setBackgroundResource(Color.TRANSPARENT);
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, mEmojiHeight);
        holder.icon.setLayoutParams(lp);
        return v;
    }

    private class ViewHolder {
        EmojiconTextView icon;
    }
}
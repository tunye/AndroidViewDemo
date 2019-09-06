package com.buaa.ct.comment.utils;

import com.buaa.ct.comment.data.Nature;
import com.buaa.ct.comment.data.Objects;
import com.buaa.ct.comment.data.People;
import com.buaa.ct.comment.data.Places;
import com.buaa.ct.comment.data.Symbols;
import com.buaa.ct.comment.emoji.EmojiIcon;

import java.util.ArrayList;
import java.util.List;

public class CreateEmojiViewPagerData {
    public static List<List<EmojiIcon>> create() {
        List<List<EmojiIcon>> pagers = new ArrayList<>();
        realFillData(People.DATA, pagers, false);
        realFillData(Nature.DATA, pagers, false);
        realFillData(Places.DATA, pagers, false);
        realFillData(Objects.DATA, pagers, false);
        realFillData(Symbols.DATA, pagers, true);
        return pagers;
    }

    private static void realFillData(EmojiIcon[] emojis, List<List<EmojiIcon>> pagers, boolean needAppendEmptyItem) {
        List<EmojiIcon> es = null;
        int size = 0;
        for (EmojiIcon ej : emojis) {
            if (size == 0) {
                es = new ArrayList<>();
            }
            if (size == 27) {
                es.add(new EmojiIcon(""));
            } else {
                es.add(ej);
            }
            size++;
            if (size == 28) {
                pagers.add(es);
                size = 0;
            }
        }
        if (needAppendEmptyItem && es != null) {
            int exSize = 28 - es.size();
            for (int i = 0; i < exSize; i++) {
                es.add(new EmojiIcon(""));
            }
            pagers.add(es);
        }
    }
}

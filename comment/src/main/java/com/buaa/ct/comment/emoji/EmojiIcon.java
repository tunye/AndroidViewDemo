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

import java.io.Serializable;


public class EmojiIcon implements Serializable {
    private static final long serialVersionUID = 1L;
    private int icon;
    private char value;
    private String emoji;

    private EmojiIcon() {
    }

    public EmojiIcon(String emoji) {
        this.emoji = emoji;
    }

    public static EmojiIcon fromResource(int icon, int value) {
        EmojiIcon emoji = new EmojiIcon();
        emoji.icon = icon;
        emoji.value = (char) value;
        return emoji;
    }

    public static EmojiIcon fromCodePoint(int codePoint) {
        EmojiIcon emoji = new EmojiIcon();
        emoji.emoji = newString(codePoint);
        return emoji;
    }

    public static EmojiIcon fromChar(char ch) {
        EmojiIcon emoji = new EmojiIcon();
        emoji.emoji = Character.toString(ch);
        return emoji;
    }

    public static EmojiIcon fromChars(String chars) {
        EmojiIcon emoji = new EmojiIcon();
        emoji.emoji = chars;
        return emoji;
    }

    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

    public char getValue() {
        return value;
    }

    public int getIcon() {
        return icon;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EmojiIcon && emoji.equals(((EmojiIcon) o).emoji);
    }

    @Override
    public int hashCode() {
        return emoji.hashCode();
    }
}

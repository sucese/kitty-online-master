package com.guoxiaoxing.userinterface;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guoxiaoxing on 2015/11/4.
 */
public class EditTextUtil
{

    /**
     * InputFilter，用于在输入时过滤表情
     */
    class EmojiFilter implements InputFilter
    {
        private Context context = null;
        private Pattern emojiPattern = Pattern
                .compile(
                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
                );

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {

            Matcher emojiMatcher = emojiPattern.matcher(source);
            if (emojiMatcher.find())
            {
                Toast.makeText(context, "不支持表情，请删除后重新输入", Toast.LENGTH_SHORT).show();
                ;
            }
            return source;
        }
    }
}

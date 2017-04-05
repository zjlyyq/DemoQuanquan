package com.example.zjlyyq.demo.Emotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jialuzhang on 2017/3/5.
 */

public class EmotionTextView extends EditText {

    public EmotionTextView(Context context) {
        super(context);
    }

    public EmotionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmotionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text.toString()), type);
        } else {
            super.setText(text, type);
        }
    }

    //正则表达式含义为[f(连续三位0-9的整数)]
    private Pattern buildPattern() {
        return Pattern.compile("\\[f[0-9]{3}]", Pattern.CASE_INSENSITIVE);//将给定的正则表达式编译并赋予给Pattern类
    }

    private CharSequence replace(String text) {
        try {
            SpannableString spannableString = new SpannableString(text);
            int start = 0;
            Pattern pattern = buildPattern();
            Matcher matcher = pattern.matcher(text);// 生成一个给定命名的Matcher对象
            while (matcher.find()) {//尝试在目标字符串里查找下一个匹配子串。
                String faceText = matcher.group();// 返回当前查找而获得的与组匹配的所有子串内容
                String key = faceText.substring(1);
                key = key.substring(key.indexOf("[")+1, key.indexOf("]"));
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        getContext().getResources().getIdentifier(key, "drawable", getContext().getPackageName()), options);
                ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
                int startIndex = text.indexOf(faceText, start);//取得该表情代码在完整字符串中的起始索引，默认为0
                int endIndex = startIndex + faceText.length();
                if (startIndex >= 0)
                    spannableString.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);	//将表情代码替换为表情图片
                start = (endIndex - 1);
            }
            return spannableString;
        } catch (Exception e) {
            return text;
        }
    }
}

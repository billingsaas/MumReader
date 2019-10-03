package com.zhanggouzi.mumreader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

public class ClipboardHelper {

    private static final String TAG ="ClipboardHelper" ;

    public static CharSequence getText(Context context){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            Log.i(TAG, "There are " + clipData.getItemCount() + " items");

            // 从数据集中获取（粘贴）第一条文本数据
            return  clipData.getItemAt(0).getText();
        }

        return "";
    }
}

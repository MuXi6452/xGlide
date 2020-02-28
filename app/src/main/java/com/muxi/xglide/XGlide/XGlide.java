package com.muxi.xglide.XGlide;

import android.content.Context;
//调用方式：XGlide.with(this).loading(R.mipmap.ic_launcher).load(mUrls[i]).into(iv);
public class XGlide {
    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}

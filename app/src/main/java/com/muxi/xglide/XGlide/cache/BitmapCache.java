package com.muxi.xglide.XGlide.cache;
import android.graphics.Bitmap;
import com.muxi.xglide.XGlide.BitmapRequest;
public interface BitmapCache {
    void put(BitmapRequest request, Bitmap bitmap); //存入内存
    Bitmap get(BitmapRequest request); //读取缓存图片
    void remove(BitmapRequest request); //清除缓存图片
}

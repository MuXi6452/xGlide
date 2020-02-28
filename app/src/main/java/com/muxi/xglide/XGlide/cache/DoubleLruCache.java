package com.muxi.xglide.XGlide.cache;
import android.content.Context;
import android.graphics.Bitmap;
import com.muxi.xglide.XGlide.BitmapRequest;
public class DoubleLruCache implements BitmapCache{
    private MemoryLruCache memoryLruCache;    //内存
    private DiskBitmapCache diskBitmapCache;    //磁盘

    public DoubleLruCache(Context context) {
//        memoryLruCache = MemoryLruCache.getInstance();
        diskBitmapCache = DiskBitmapCache.getInstance(context);
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
//        memoryLruCache.put(request,bitmap);
        diskBitmapCache.put(request,bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
//        Bitmap bitmap = memoryLruCache.get(request);
//        if(bitmap==null){
            Bitmap  bitmap = diskBitmapCache.get(request);
//            memoryLruCache.put(request,bitmap);
//        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
//        memoryLruCache.remove(request);
        diskBitmapCache.remove(request);
    }
}

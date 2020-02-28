package com.muxi.xglide.XGlide.cache;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import com.muxi.xglide.XGlide.BitmapRequest;
import com.muxi.xglide.XGlide.cache.disk.DiskLruCache;
import com.muxi.xglide.XGlide.cache.disk.Util;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static com.muxi.xglide.XGlide.cache.disk.Util.getAppVersion;
public class DiskBitmapCache implements BitmapCache {
    private DiskLruCache diskLruCache;
    private static volatile DiskBitmapCache instance;
    private String imageCachePath = "Image";
    private static final byte[] lock = new byte[0];
    private int MB = 1024*1024;
    private int maxDiskSize = 50 * MB;

    private DiskBitmapCache(Context context){
        File cacheFile = getImageCacheFile(context,imageCachePath);
        if(!cacheFile.exists()){
            cacheFile.mkdir();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheFile,getAppVersion(context),1,maxDiskSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskBitmapCache getInstance(Context context){
            if(instance==null){
                synchronized (lock){
                    if(instance==null){
                        instance =new DiskBitmapCache(context);
                    }
                }
            }
            return instance;
    }

    //设置图片缓存目录
    private File getImageCacheFile(Context context, String imageCachePath) {
        String path;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = context.getExternalCacheDir().getPath();
        }else {
            path = context.getCacheDir().getPath();
        }
        File dir = new File(path + File.separator + imageCachePath);
//        Log.e("DiskBitmapCache_图片缓存路径：", dir.getAbsolutePath());
        return dir;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
       DiskLruCache.Editor editor;
       OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(request.getUrlMd5());
            outputStream = editor.newOutputStream(0);
            if(presetBitmap2Disk(outputStream,bitmap)){
                editor.commit();
            }else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Util.closeQuietly(outputStream);
        }
    }

    private boolean presetBitmap2Disk(OutputStream outputStream, Bitmap bitmap) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 10, bufferedOutputStream);
            bufferedOutputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Util.closeQuietly(bufferedOutputStream);
        }
        return false;
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        InputStream stream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(request.getUrlMd5());
            if(snapshot!=null){
                stream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Util.closeQuietly(stream);
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            diskLruCache.remove(request.getUrlMd5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

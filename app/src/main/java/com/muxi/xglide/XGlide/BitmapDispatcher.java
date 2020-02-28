package com.muxi.xglide.XGlide;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.muxi.xglide.XGlide.cache.DoubleLruCache;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
public class BitmapDispatcher extends Thread{
    private Handler handler = new Handler(Looper.getMainLooper());
    private LinkedBlockingQueue<BitmapRequest> requestQueue;//创建一个阻塞队列
    private DoubleLruCache doubleLruCache = new DoubleLruCache(XApplication.getInstance());
    public BitmapDispatcher(LinkedBlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()){   //BitmapDispatcher没有被干掉
            try {
                final BitmapRequest bitmapRequest = requestQueue.take();
                showLoadingImg(bitmapRequest);//先显示占位图
                final Bitmap bitmap = findBitmap(bitmapRequest);//加载图片
                showImageView(bitmapRequest,bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoadingImg(BitmapRequest bitmapRequest) {  //显示占位图
        if(bitmapRequest.getResId()>0 && bitmapRequest.getImageView()!=null){
            final int resId = bitmapRequest.getResId();
            final ImageView iv = bitmapRequest.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageResource(resId);
                }
            });
        }
    }

    private Bitmap findBitmap(BitmapRequest bitmapRequest) {    //加载图片
        Bitmap bitmap = null;
        bitmap = doubleLruCache.get(bitmapRequest);//先看内存和本地有没有图片
        if(bitmap==null){
            bitmap = downloadImage(bitmapRequest.getUrl());
            if(bitmap!=null){
                doubleLruCache.put(bitmapRequest,bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap downloadImage(String url) {  //下载图片
        Bitmap bitmap = null;
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection)u.openConnection();
            InputStream is = c.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void showImageView(final BitmapRequest bitmapRequest, final Bitmap bitmap) {    //显示到ImageView
        if(bitmap!=null && bitmapRequest.getImageView()!=null
                && bitmapRequest.getUrlMd5().equals(bitmapRequest.getImageView().getTag())){
            final ImageView iv = bitmapRequest.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bitmap);
                    if(bitmapRequest.getRequestListener()!=null){
                        RequestListener listener = bitmapRequest.getRequestListener();
                        listener.onSuccess(bitmap);
                    }
                }
            });
        }
    }
}

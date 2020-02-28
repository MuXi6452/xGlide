package com.muxi.xglide.XGlide;
import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
public class BitmapRequest {
    private String url;//图片路径
    private Context context;
    private SoftReference<ImageView> imageView; //SoftReference软引用，垃圾回收时会优先回收
    private int resId;  //占位图
    private RequestListener requestListener;    //回调对象
    private String urlMd5;  //图片的标识，防止图片错位

    public BitmapRequest(Context context) {
        this.context = context;
    }

    //链式调度：像Glide.with().load().into()
    public BitmapRequest load(String url) {
        this.url = url;
        this.urlMd5 = MD5Utils.toMD5(url);
        return this;
    }

    public BitmapRequest loading(int resId) {
        this.resId = resId;
        return this;
    }

    public BitmapRequest listener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public void into(ImageView iv) {
        iv.setTag(urlMd5);
        this.imageView = new SoftReference<>(iv);
        RequestManager.getInstance().addBitmapRequest(this);
    }

    //生成Get方法：
    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public ImageView getImageView() {   //注意这个返回的是ImageView
        return imageView.get();
    }

    public int getResId() {
        return resId;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public String getUrlMd5() {
        return urlMd5;
    }
}

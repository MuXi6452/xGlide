package com.muxi.xglide.XGlide;
import android.content.Context;

import java.util.concurrent.LinkedBlockingQueue;
public class RequestManager {
    private static RequestManager requestManager = new RequestManager();    //单例
    //创建阻塞队列：
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();
    //创建一个线程数组：
    private BitmapDispatcher[] bitmapDispatchers;
    private RequestManager(){
        start();
    }

    public static RequestManager getInstance(){
        return requestManager;
    }
    //将图片请求对象添加到队列中去
    public void addBitmapRequest(BitmapRequest bitmapRequest){
        if(bitmapRequest==null) return;
        if(!requestQueue.contains(bitmapRequest)){
            requestQueue.add(bitmapRequest);
        }
    }
    //开启所有线程的总开关
    public void start(){
        stop();
        startAllDispatcher();
    }

    //创建并开始所有的线程
    public void startAllDispatcher(){
        //获取手机支持的单个应用最大的线程数
        int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();
            bitmapDispatchers[i]= bitmapDispatcher;
        }
    }
    //停止所有的线程
    public void stop(){
        if(bitmapDispatchers!=null && bitmapDispatchers.length>0){
            for (BitmapDispatcher bd:bitmapDispatchers) {
                if(!bd.isInterrupted()){
                    bd.interrupt();
                }
            }
        }
    }
}

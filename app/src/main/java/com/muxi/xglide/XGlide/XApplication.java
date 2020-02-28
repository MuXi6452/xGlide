package com.muxi.xglide.XGlide;
import android.app.Application;
public class XApplication extends Application {
    private static XApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static XApplication getInstance(){
        return application;
    }
}

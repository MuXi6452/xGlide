package com.muxi.xglide;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.muxi.xglide.XGlide.XGlide;

public class MainActivity extends AppCompatActivity {
    private String[] mUrls = {"http://img2.imgtn.bdimg.com/it/u=1287225853,2590220833&fm=11&gp=0.jpg"
            ,"http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20180802/9cf71c90b79641b28cf0e983e40490ef.jpeg"
            ,"http://n.sinaimg.cn/sinacn11/700/w960h540/20180728/c6f9-hfxsxzf7992415.jpg"};
    private LinearLayout mLlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLlContainer = findViewById(R.id.ll_container);
//        XGlide.with(this).loading(R.mipmap.ic_launcher).load(mUrls[0]).into(mIvTest);
        for (int i = 0; i < mUrls.length; i++) {
            ImageView iv = new ImageView(this);
            XGlide.with(this).loading(R.mipmap.ic_launcher).load(mUrls[i]).into(iv);
            mLlContainer.addView(iv);
        }
    }
}

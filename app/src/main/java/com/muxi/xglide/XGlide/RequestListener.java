package com.muxi.xglide.XGlide;

import android.graphics.Bitmap;

public interface RequestListener {
    boolean onSuccess(Bitmap bitmap);
    boolean onFailure();
}

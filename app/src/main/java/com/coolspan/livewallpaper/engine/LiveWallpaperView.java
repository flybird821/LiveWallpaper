package com.coolspan.livewallpaper.engine;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author: Coolspan
 * time: 2017/3/13 15:52
 * describe: 动态壁纸引擎
 */
public class LiveWallpaperView extends SurfaceView implements SurfaceHolder.Callback {

    public LiveWallpaperView(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

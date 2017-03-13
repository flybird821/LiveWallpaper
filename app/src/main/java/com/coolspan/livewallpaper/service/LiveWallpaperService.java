package com.coolspan.livewallpaper.service;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.coolspan.livewallpaper.view.LiveWallpaperView;

/**
 * author: Coolspan
 * time: 2017/3/13 15:19
 * describe: 动态壁纸服务，此服务继承自系统服务，一般不出现异常，此服务不会退出
 */
public class LiveWallpaperService extends WallpaperService {

    private Context context;

    private LiveWallpaperEngine liveWallpaperEngine;

    private final static long REFLESH_GAP_TIME = 1000L;//如果想播放的流畅，需满足1s 16帧   62ms切换间隔时间

    @Override
    public Engine onCreateEngine() {
        this.context = this;
        this.liveWallpaperEngine = new LiveWallpaperEngine();
        return this.liveWallpaperEngine;
    }

    private class LiveWallpaperEngine extends LiveWallpaperService.Engine {

        private Runnable viewRunnable;
        private Handler handler;

        private LiveWallpaperView liveWallpaperView;

        private final SurfaceHolder surfaceHolder;

        public LiveWallpaperEngine() {
            this.surfaceHolder = getSurfaceHolder();
            this.liveWallpaperView = new LiveWallpaperView(LiveWallpaperService.this.getBaseContext());
            this.liveWallpaperView.initView(surfaceHolder);
            this.handler = new Handler();
            this.initRunnable();
            this.handler.post(this.viewRunnable);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {//ICE_CREAM_SANDWICH_MR1  15
                return;
            } else {
                setOffsetNotificationsEnabled(true);
            }
        }

        private void initRunnable() {
            this.viewRunnable = new Runnable() {
                @Override
                public void run() {
                    LiveWallpaperEngine.this.drawView();
                }
            };
        }

        private void drawView() {
            if (this.liveWallpaperView == null) {
                return;
            } else {
                this.handler.removeCallbacks(this.viewRunnable);
                this.liveWallpaperView.surfaceChanged(this.surfaceHolder, -1, this.liveWallpaperView.getWidth(), this.liveWallpaperView.getHeight());
                if (!(isVisible())) {
                    return;
                } else {
                    this.handler.postDelayed(this.viewRunnable, REFLESH_GAP_TIME);
                    this.liveWallpaperView.loadNextWallpaperBitmap();
                }
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.drawView();
            if (this.liveWallpaperView != null) {
                this.liveWallpaperView.surfaceCreated(holder);
            } else {
                //nothing
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.drawView();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (this.handler != null) {
                if (visible) {
                    this.handler.post(this.viewRunnable);
                } else {
                    this.handler.removeCallbacks(this.viewRunnable);
                }
            } else {
                //nothing
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (this.handler != null) {
                this.handler.removeCallbacks(this.viewRunnable);
            } else {
                //nothing
            }
            if (this.liveWallpaperView != null) {
                this.liveWallpaperView.surfaceDestroyed(holder);
            } else {
                //nothing
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (this.handler != null) {
                this.handler.removeCallbacks(this.viewRunnable);
            } else {
                //nothing
            }
        }
    }
}

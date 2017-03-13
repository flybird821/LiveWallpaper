package com.coolspan.livewallpaper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.LruCache;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.coolspan.livewallpaper.R;
import com.coolspan.livewallpaper.model.WallpaperModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: Coolspan
 * time: 2017/3/13 15:52
 * describe: 动态壁纸视图
 */
public class LiveWallpaperView extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;

    private Paint mPaint;
    private LruCache<String, Bitmap> mMemoryCache;

    private List<WallpaperModel> mWallpaperList;

    private WallpaperModel wallpaperModel;
    private Bitmap mNextBitmap;
    private int index;

    public LiveWallpaperView(Context context) {
        super(context);
        this.context = context;
        this.index = 0;
        this.initWallpaperList();
        this.initCacheConfig();
        this.initPaintConfig();
    }

    private void initWallpaperList() {
        this.mWallpaperList = new ArrayList<WallpaperModel>(10) {
            {
                this.add(new WallpaperModel("livewallpaper1", R.raw.livewallpaper1));
                this.add(new WallpaperModel("livewallpaper2", R.raw.livewallpaper2));
                this.add(new WallpaperModel("livewallpaper3", R.raw.livewallpaper3));
                this.add(new WallpaperModel("livewallpaper4", R.raw.livewallpaper4));
                this.add(new WallpaperModel("livewallpaper5", R.raw.livewallpaper5));
                this.add(new WallpaperModel("livewallpaper6", R.raw.livewallpaper6));
                this.add(new WallpaperModel("livewallpaper7", R.raw.livewallpaper7));
                this.add(new WallpaperModel("livewallpaper8", R.raw.livewallpaper8));
                this.add(new WallpaperModel("livewallpaper9", R.raw.livewallpaper9));
                this.add(new WallpaperModel("livewallpaper10", R.raw.livewallpaper10));
            }
        };
    }

    private void initPaintConfig() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(5);
    }

    private void initCacheConfig() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        this.mMemoryCache = new LruCache<String, Bitmap>(maxMemory / 8) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public void initView(SurfaceHolder surfaceHolder) {
        this.loadNextWallpaperBitmap();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.removeCallback(this);
        drawSurfaceView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawSurfaceView(holder);
    }

    private void drawSurfaceView(SurfaceHolder holder) {
        if (this.mNextBitmap != null && !this.mNextBitmap.isRecycled()) {
            Canvas localCanvas = holder.lockCanvas();
            if (localCanvas != null) {
                Rect rect = new Rect();
                rect.left = rect.top = 0;
                rect.bottom = localCanvas.getHeight();
                rect.right = localCanvas.getWidth();
                localCanvas.drawBitmap(this.mNextBitmap, null, rect, this.mPaint);
                holder.unlockCanvasAndPost(localCanvas);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.releaseBitmap();
    }

    public void loadNextWallpaperBitmap() {
        this.wallpaperModel = this.mWallpaperList.get(this.index);
        if (this.wallpaperModel != null) {
            Bitmap bitmap = this.mMemoryCache.get(this.wallpaperModel.getWallpaperKey());
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(this.getResources(), this.wallpaperModel.getWallpaperRid());
                if (bitmap != null) {
                    this.mMemoryCache.put(this.wallpaperModel.getWallpaperKey(), bitmap);
                }
            }
            if (bitmap != null) {
                this.mNextBitmap = bitmap;
            }
        }
        this.index++;
        if (this.index >= this.mWallpaperList.size()) {
            this.index = 0;
        }
    }

    /**
     * 释放动态壁纸
     */
    public void releaseBitmap() {
        try {
            if (mMemoryCache != null && mMemoryCache.size() > 0) {
                Map<String, Bitmap> stringBitmapMap = mMemoryCache.snapshot();
                mMemoryCache.evictAll();
                if (stringBitmapMap != null && stringBitmapMap.size() > 0) {
                    for (Map.Entry<String, Bitmap> entry : stringBitmapMap.entrySet()) {
                        if (entry != null && entry.getValue() != null && !entry.getValue().isRecycled()) {
                            entry.getValue().recycle();
                        }
                    }
                    stringBitmapMap.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

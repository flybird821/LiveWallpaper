package com.coolspan.livewallpaper;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

/**
 * 壁纸开发
 * 1.资源文件做为壁纸
 * 2.Bitmap做为壁纸
 * 3.动态壁纸
 * <p>
 * author:Coolspan
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 使用资源文件设置壁纸
     * 直接设置为壁纸，不会有任何界面和弹窗出现
     *
     * @param view
     */
    public void onSetWallpaperForResource(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //WallpaperManager.FLAG_LOCK  WallpaperManager.FLAG_SYSTEM
                wallpaperManager.setResource(R.raw.wallpaper, WallpaperManager.FLAG_SYSTEM);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Bitmap设置壁纸
     * 直接设置为壁纸，不会有任何界面和弹窗出现
     * 壁纸切换，会有动态的渐变切换
     *
     * @param view
     */
    public void onSetWallpaperForBitmap(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            Bitmap wallpaperBitmap = BitmapFactory.decodeResource(getResources(), R.raw.girl);
            wallpaperManager.setBitmap(wallpaperBitmap);
//            setWallpaper(wallpaperBitmap);
//            setWallpaper(getResources().openRawResource(R.raw.girl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除壁纸
     *
     * @param view
     */
    public void clearWallpaper(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.clear();
//            clearWallpaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

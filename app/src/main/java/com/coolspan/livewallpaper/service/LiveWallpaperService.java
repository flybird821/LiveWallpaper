package com.coolspan.livewallpaper.service;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

/**
 * author: Coolspan
 * time: 2017/3/13 15:19
 * describe:
 */
public class LiveWallpaperService extends WallpaperService {

    private Context context;

    private LiveWallpaperEngine liveWallpaperEngine;

    @Override
    public Engine onCreateEngine() {
        this.context = this;
        this.liveWallpaperEngine = new LiveWallpaperEngine();
        return this.liveWallpaperEngine;
    }

    private class LiveWallpaperEngine extends LiveWallpaperService.Engine {

    }
}

package com.ctrl.android.kcetong.toolkit.util;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.finalteam.galleryfinal.PauseOnScrollListener;

/**
 * Created by Administrator on 2016/11/25.
 */

public class UILPauseOnScrollListener extends PauseOnScrollListener {

    public UILPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        ImageLoader.getInstance().resume();
    }

    @Override
    public void pause() {
        ImageLoader.getInstance().pause();
    }
}
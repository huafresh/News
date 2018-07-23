package hua.news.module_video.fragments;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;

import com.example.hua.framework.network.NetworkHelper;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.hua.framework.classes.CommonThreadFactory;

/**
 * 网络测速
 *
 * @author hua
 * @version 2017/11/27 11:25
 */

public class NetWorkSpeedHelp {

    private ScheduledExecutorService mScheduledService;
    private long lastTotalTxBytes;
    private ScheduledFuture<?> mScheduledTask;
    private Handler mHander = new Handler(Looper.getMainLooper());
    private boolean isStared = false;

    private OnUpdateListener mListener;

    public NetWorkSpeedHelp() {
        mScheduledService = new ScheduledThreadPoolExecutor(1,
                new CommonThreadFactory(NetworkHelper.class.getName()));
    }

    public static NetWorkSpeedHelp getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final NetWorkSpeedHelp sInstance = new NetWorkSpeedHelp();
    }

    /**
     * 开启网络测速
     */
    public void startCalculateSpeed(final OnUpdateListener listener) {
        mListener = listener;
        if (TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED || isStared) {
            return;
        }
        lastTotalTxBytes = TrafficStats.getTotalTxBytes();
        mScheduledTask = mScheduledService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long curTotalTxBytes = TrafficStats.getTotalTxBytes();
                final long speed = curTotalTxBytes - lastTotalTxBytes;
                lastTotalTxBytes = curTotalTxBytes;
                if (mListener != null) {
                    mHander.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onUpdate(speed);
                        }
                    });
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        isStared = true;
    }

    public boolean isRunning(){
        return isStared;
    }

    /**
     * 停止测速
     */
    public void stop() {
        mScheduledTask.cancel(true);
        isStared = false;
    }


    public interface OnUpdateListener {
        /**
         * 测速更新回调，单位是b/s
         *
         * @param speed 网速
         */
        void onUpdate(long speed);
    }

}

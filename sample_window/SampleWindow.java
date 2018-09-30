package com.hua.samplewindow;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.IDisplayManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.MergedConfiguration;
import android.view.Choreographer;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.IWindow;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;

import com.android.internal.os.IResultReceiver;

import static android.view.Display.DEFAULT_DISPLAY;

/**
 * @author hua
 * @version 2018/9/30 14:54
 */

public class SampleWindow {

    private IWindowSession mSession;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private IBinder mToken = new Binder();
    private IWindow mWindow = new MyWindow();
    private Rect mInsets = new Rect();
    private Rect mFrame = new Rect();
    private Rect mVisibleInsets = new Rect();
    private Rect rect1 = new Rect();
    private Rect rect2 = new Rect();
    private Rect rect3 = new Rect();
    private Rect rect4 = new Rect();
    private MergedConfiguration mConfig = new MergedConfiguration();
    private Surface mSurface = new Surface();
    private Paint mPaint = new Paint();
    private Choreographer choreographer;

    private InputChannel inputChannel = new InputChannel();
    private InputHandle inputHandle;
    private IWindowManager wms;
    private boolean isContinue = true;
    private FrameRender frameRender = new FrameRender();

    public static void main(String[] args) {
        try {
            //new SampleWindow().run();
            Log.e("@@@hua","hello, app_process");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void run() throws Exception {
        Looper.prepare();

        wms = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        mSession = WindowManagerGlobal.getWindowSession();
        IDisplayManager dm = IDisplayManager.Stub.asInterface(ServiceManager.getService(Context.DISPLAY_SERVICE));
        DisplayInfo di = dm.getDisplayInfo(Display.DEFAULT_DISPLAY);
        Point screenSize = new Point(di.appWidth, di.appHeight);

        initLayoutParams(screenSize);

        installWindow(wms);

        choreographer = Choreographer.getInstance();

        scheduleNextFrame();

        Looper.loop();

        isContinue = false;

        unInstallWindow();
    }

    private void installWindow(IWindowManager wms) throws Exception {
        wms.addWindowToken(mToken, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, DEFAULT_DISPLAY);
        params.token = mToken;
        mSession.add(mWindow, 0, params, View.VISIBLE, mInsets, mFrame, inputChannel);
        mSession.relayout(mWindow, 0,
                params, params.width, params.height,
                View.VISIBLE, 0,
                mFrame, mInsets, rect1, rect2, rect3, rect4,
                mVisibleInsets, mConfig, mSurface);

        if (!mSurface.isValid()) {
            throw new RuntimeException("invalid surface");
        }

        inputHandle = new InputHandle(inputChannel, Looper.myLooper());
    }

    private void unInstallWindow() throws Exception {
        mSession.remove(mWindow);
        wms.removeWindowToken(mToken, DEFAULT_DISPLAY);
    }

    private void initLayoutParams(Point screenSize) {
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.setTitle("sampleWindow");
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = screenSize.x / 4;
        params.y = screenSize.y / 4;
        params.width = screenSize.x / 2;
        params.height = screenSize.y / 2;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        mPaint.setColor(Color.RED);
    }

    class FrameRender implements Runnable {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            try {
                long time = choreographer.getFrameTime() % 1000;

                if (mSurface.isValid()) {
                    Canvas canvas = mSurface.lockCanvas(null);
                    canvas.drawColor(Color.DKGRAY);
                    canvas.drawRect(2 * params.width * time / 1000 - params.width,
                            0, 2 * params.width * time / 1000, params.height, mPaint);
                    mSurface.unlockCanvasAndPost(canvas);
                    mSession.finishDrawing(mWindow);
                }

                if (isContinue) {
                    scheduleNextFrame();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void scheduleNextFrame() {
        choreographer.postCallback(Choreographer.CALLBACK_ANIMATION, frameRender, null);
    }

    class InputHandle extends InputEventReceiver {
        Looper looper;

        InputHandle(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
            this.looper = looper;
        }

        @Override
        public void onInputEvent(InputEvent event, int displayId) {
            if (event instanceof MotionEvent) {
                if (((MotionEvent) event).getAction() == MotionEvent.ACTION_UP) {
                    looper.quit();
                }
            }
            super.onInputEvent(event, displayId);
        }
    }

    private class MyWindow extends IWindow.Stub {

        @Override
        public void executeCommand(String s, String s1, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {

        }

        @Override
        public void resized(Rect rect, Rect rect1, Rect rect2, Rect rect3, Rect rect4, Rect rect5, boolean b, MergedConfiguration mergedConfiguration, Rect rect6, boolean b1, boolean b2, int i) throws RemoteException {

        }

        @Override
        public void moved(int i, int i1) throws RemoteException {

        }

        @Override
        public void dispatchAppVisibility(boolean b) throws RemoteException {

        }

        @Override
        public void dispatchGetNewSurface() throws RemoteException {

        }

        @Override
        public void windowFocusChanged(boolean b, boolean b1) throws RemoteException {

        }

        @Override
        public void closeSystemDialogs(String s) throws RemoteException {

        }

        @Override
        public void dispatchWallpaperOffsets(float v, float v1, float v2, float v3, boolean b) throws RemoteException {

        }

        @Override
        public void dispatchWallpaperCommand(String s, int i, int i1, int i2, Bundle bundle, boolean b) throws RemoteException {

        }

        @Override
        public void dispatchDragEvent(DragEvent dragEvent) throws RemoteException {

        }

        @Override
        public void updatePointerIcon(float v, float v1) throws RemoteException {

        }

        @Override
        public void dispatchSystemUiVisibilityChanged(int i, int i1, int i2, int i3) throws RemoteException {

        }

        @Override
        public void dispatchWindowShown() throws RemoteException {

        }

        @Override
        public void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) throws RemoteException {

        }

        @Override
        public void dispatchPointerCaptureChanged(boolean b) throws RemoteException {

        }
    }

}

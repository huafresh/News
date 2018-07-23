package com.example.hua.framework.wrapper.popupwindow;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.hua.framework.interfaces.IWindow;

/**
 * 一个通用的PopupWindow。
 *
 * @author hua
 * @date 2017/5/2
 * @see IWindow
 */

public class CommonPopupWindow extends PopupWindow implements IWindow.IContainer {

    public static final int BLACK_DISMISS_DELAY = 300;
    /**
     * 当前展示的内容
     */
    private IWindow.IContentView mContentView;

    /**
     * 是否显示时背景变暗
     */
    private boolean enableBacBlack = true;

    private Context mContext;
    private float bacAlpha = 0.5f;
    private boolean autoDismissBlack = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long delayDisBlack = -1;

    CommonPopupWindow(Context context) {
        super(context);
        mContext = context;
    }

    public static CommonPopupWindow get(Context context) {
        return buildDefaultPopupWindow(context);
    }

    private static CommonPopupWindow buildDefaultPopupWindow(Context context) {
        CommonPopupWindow popupWindow = new CommonPopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        return popupWindow;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(new WrapperOnDismissListener(onDismissListener));
    }

    public CommonPopupWindow setContent(View view) {
        setContent(new DefaultContentView(view));
        return this;
    }

    public CommonPopupWindow setContent(IWindow.IContentView contentView) {
        if (contentView == null) {
            throw new IllegalArgumentException("contentView can not be null");
        }
        mContentView = contentView;
        setContentView(mContentView.getContentView(mContext));
        mContentView.onAttachToContainer(this);
        super.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonPopupWindow.this.onDismiss();
            }
        });
        return this;
    }

    private void onDismiss() {
        if (mContentView != null) {
            mContentView.onDetachContainer(this);
        }
        if (autoDismissBlack) {
            if (delayDisBlack != -1) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blackBackgroundIfNeed(1f);
                    }
                }, delayDisBlack);
                delayDisBlack = -1;
            } else {
                blackBackgroundIfNeed(1f);
            }

        }
    }

    private void blackBackgroundIfNeed(float alpha) {
        if (mContext instanceof Activity && enableBacBlack) {
            final Window window = ((Activity) mContext).getWindow();
            final WindowManager.LayoutParams params = window.getAttributes();

            float currentAlpha = params.alpha;
            if (currentAlpha != alpha) {
                ValueAnimator animator = ValueAnimator.ofFloat(currentAlpha, alpha)
                        .setDuration(BLACK_DISMISS_DELAY);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        params.alpha = (float) animation.getAnimatedValue();
                        window.setAttributes(params);
                    }
                });
                animator.start();
            }
        }
    }

    public CommonPopupWindow dismissBlack() {
        blackBackgroundIfNeed(1f);
        return this;
    }

    /**
     * 设置弹出时的背景变暗程度
     *
     * @param alpha 0.0f-1.0f
     */
    public CommonPopupWindow setBackgroundAlpha(float alpha) {
        this.bacAlpha = alpha;
        return this;
    }

    public CommonPopupWindow setAnimation(int style) {
        setAnimationStyle(style);
        return this;
    }

    public CommonPopupWindow autoDismissBlack(boolean auto) {
        this.autoDismissBlack = auto;
        return this;
    }

    public CommonPopupWindow dismissBlackDelay(long delay) {
        this.delayDisBlack = delay;
        return this;
    }

    /**
     * 设置宽度为屏幕宽度*{@code widthScale}
     */
    public CommonPopupWindow setWidthScale(float widthScale) {
        setWidth((int) (getScreenWidth(mContext) * widthScale));
        return this;
    }

    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        return outMetric.widthPixels;
    }

    /**
     * 设置弹出时背景是否变暗
     *
     * @param b true变暗，false不变暗
     */
    public CommonPopupWindow enableBlackBac(boolean b) {
        enableBacBlack = b;
        return this;
    }

    public CommonPopupWindow replaceContent(IWindow.IContentView contentView) {
        dismiss();
        setContent(contentView);
        show();
        return this;
    }

    public CommonPopupWindow show() {
        View view = new View(mContext);
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
        onShow();
        return this;
    }

    private void onShow() {
        blackBackgroundIfNeed(bacAlpha);
    }

    public CommonPopupWindow showAtLocation(int gravity, int x, int y) {
        View view = new View(mContext);
        showAtLocation(view, gravity, x, y);
        onShow();
        return this;
    }

    public CommonPopupWindow showAnchor(View anchor) {
        super.showAsDropDown(anchor);
        onShow();
        return this;
    }

    public CommonPopupWindow showAnchor(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        onShow();
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CommonPopupWindow showAnchor(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        onShow();
        return this;
    }

    public CommonPopupWindow replaceContent(View view) {
        dismiss();
        setContent(new DefaultContentView(view));
        show();
        return this;
    }

    @Override
    public void addContentView(IWindow.IContentView contentView) {
        setContent(contentView);
    }

    @Override
    public void removeContentView(IWindow.IContentView contentView) {
        dismiss();
    }

    private static class DefaultContentView implements IWindow.IContentView {
        private View view;

        public DefaultContentView(View view) {
            this.view = view;
        }


        @Override
        public View getContentView(Context context) {
            return view;
        }

        @Override
        public void onAttachToContainer(IWindow.IContainer container) {
            //do nop
        }

        @Override
        public void onDetachContainer(IWindow.IContainer container) {
            //do nop
        }
    }

    private class WrapperOnDismissListener implements OnDismissListener {
        private OnDismissListener listener;

        public WrapperOnDismissListener(OnDismissListener listener) {
            this.listener = listener;
        }

        @Override
        public void onDismiss() {
            if (listener != null) {
                listener.onDismiss();
            }
            CommonPopupWindow.this.onDismiss();
        }
    }

}

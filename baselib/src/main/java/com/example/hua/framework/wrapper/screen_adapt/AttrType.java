package com.example.hua.framework.wrapper.screen_adapt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
public abstract class AttrType {

    public static class WidthHeight extends AttrType {

        @Override
        public void adapt(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = DesignScreen.getNewPxFromDp(layoutParams.width);
            layoutParams.height = DesignScreen.getNewPxFromDp(layoutParams.height);
        }
    }

    public static class Padding extends AttrType {
        @Override
        public void adapt(View view) {
            int left = view.getPaddingLeft();
            int top = view.getPaddingTop();
            int right = view.getPaddingRight();
            int bottom = view.getPaddingBottom();

            view.setPadding(
                    DesignScreen.getNewPxFromDp(left),
                    DesignScreen.getNewPxFromDp(top),
                    DesignScreen.getNewPxFromDp(right),
                    DesignScreen.getNewPxFromDp(bottom)
            );
        }
    }

    public static class Margin extends AttrType {
        @Override
        public void adapt(View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.leftMargin = DesignScreen.getNewPxFromDp(marginParams.leftMargin);
                marginParams.topMargin = DesignScreen.getNewPxFromDp(marginParams.topMargin);
                marginParams.rightMargin = DesignScreen.getNewPxFromDp(marginParams.rightMargin);
                marginParams.bottomMargin = DesignScreen.getNewPxFromDp(marginParams.bottomMargin);
            }
        }
    }

    public static class TextSize extends AttrType {

        @Override
        public void adapt(View view) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextSize(DesignScreen.getNewPxFromSp(textView.getTextSize()));
            }
        }
    }

    /**
     * 修改传入的View的布局参数，对特定的属性进行适配。
     * 在这里可以不用把LayoutParams设置回去，最后会统一设置。
     *
     * @param view View
     */
    public abstract void adapt(View view);

}

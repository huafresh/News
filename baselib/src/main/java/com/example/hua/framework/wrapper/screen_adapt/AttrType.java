package com.example.hua.framework.wrapper.screen_adapt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
public abstract class AttrType {

    static List<AttrType> getAttrTypeList(){
        List<AttrType> list = new ArrayList<>();
        list.add(new WidthHeight());
        list.add(new Padding());
        list.add(new Margin());
        list.add(new TextSize());
        return list;
    }

    public static class WidthHeight extends AttrType {

        @Override
        public void adapt(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = DesignScreen.getNewPxForDp(layoutParams.width);
            layoutParams.height = DesignScreen.getNewPxForDp(layoutParams.height);
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
                    DesignScreen.getNewPxForDp(left),
                    DesignScreen.getNewPxForDp(top),
                    DesignScreen.getNewPxForDp(right),
                    DesignScreen.getNewPxForDp(bottom)
            );
        }
    }

    public static class Margin extends AttrType {
        @Override
        public void adapt(View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.leftMargin = DesignScreen.getNewPxForDp(marginParams.leftMargin);
                marginParams.topMargin = DesignScreen.getNewPxForDp(marginParams.topMargin);
                marginParams.rightMargin = DesignScreen.getNewPxForDp(marginParams.rightMargin);
                marginParams.bottomMargin = DesignScreen.getNewPxForDp(marginParams.bottomMargin);
            }
        }
    }

    public static class TextSize extends AttrType {

        @Override
        public void adapt(View view) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextSize(DesignScreen.getNewPxForSp(textView.getTextSize()));
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

package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.hua.framework.R;

/**
 * Created by hua on 2017/6/15.
 * 使用此布局时，假设有两个直接子view：view1，view2。如果view2设置layout_anchor属性的值为view1的id，
 * 则view2可以通过设置不同anchor_Gravity来达到自身相对于view1进行布局。此功能与CoordinatorLayout很相似
 * 不过进行了扩展，即支持设置Layout_anchorX和Layout_anchorY来任意决定view2的位置
 *
 *
 * 此类本来是用来编写我页面的布局的，不过后来我页面改实现方式，因此放弃
 *
 */
@Deprecated
public class AnchorViewLayout extends FrameLayout {

    private Context mContext;

    public AnchorViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnchorViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnchorViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutChildFromAnchor();
    }

    private void layoutChildFromAnchor() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            AnchorViewLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            View anchorView = lp.findAnchorView(this);
            if (anchorView != null) {
                layoutChildWithAnchorView(lp, child, anchorView);
            }
        }
    }

    private void layoutChildWithAnchorView(AnchorViewLayout.LayoutParams lp, View child, View anchorView) {
        int gravity = lp.mAnchorGravity;
        int anchorX = lp.mAnchorX;
        int anchorY = lp.mAnchorY;
        int left = child.getLeft();
        int top = child.getTop();
        int childWidth = child.getWidth();
        int childHeight = child.getHeight();
        int anchorWidth = anchorView.getWidth();
        int anchorHeight = anchorView.getHeight();

        switch (gravity) {
            case 1: //center
                left = anchorWidth / 2 - childWidth / 2 + anchorView.getLeft();
                top = anchorHeight / 2 - childHeight / 2 + anchorView.getTop();
                break;
            case 2: //center_vertical
                left = anchorView.getLeft() - childWidth / 2;
                top = anchorHeight / 2 - childHeight / 2 + anchorView.getTop();
                break;
            case 3: //center_horizontal
                left = anchorHeight / 2 - childHeight / 2 + anchorView.getTop();
                top = anchorView.getTop() - childHeight / 2;
                break;
            case 4: //left
                left = anchorView.getLeft() - childWidth / 2;
                top = anchorHeight / 2 - childHeight / 2 + anchorView.getTop();
                break;
            case 5: //top
                left = anchorWidth / 2 - childWidth / 2 + anchorView.getLeft();
                top = anchorView.getTop() - childHeight / 2;
                break;
            case 6: //right
                left = anchorView.getRight() - childWidth / 2;
                top = anchorHeight / 2 - childHeight / 2 + anchorView.getTop();
                break;
            case 7: //bottom
                left = anchorWidth / 2 - childWidth / 2 + anchorView.getLeft();
                top = anchorView.getBottom() - childHeight / 2;
                break;
        }
        left += anchorX;
        top += anchorY;
        child.layout(left, top, left + childWidth, top + childHeight);
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(mContext, attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        private final int mAnchorId;
        private View mAnchorView;
        private final int mAnchorGravity;
        private final int mAnchorX;
        private final int mAnchorY;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AnchorViewLayout);

            mAnchorId = a.getResourceId(R.styleable.AnchorViewLayout_layout_anchorId, View.NO_ID);

            mAnchorGravity = a.getInteger(R.styleable.AnchorViewLayout_layout_anchorGravity2,
                    Gravity.NO_GRAVITY);

            mAnchorX = a.getDimensionPixelSize(R.styleable.AnchorViewLayout_layout_anchorX, 0);

            mAnchorY = a.getDimensionPixelSize(R.styleable.AnchorViewLayout_layout_anchorY, 0);

            a.recycle();
        }

        /**
         * 找出当前的view的anchorView，无则返回null
         *
         * @param parent
         * @return
         */
        View findAnchorView(AnchorViewLayout parent) {
            if (mAnchorView != null) {
                return mAnchorView;
            } else {
                if (mAnchorId != View.NO_ID) {
                    mAnchorView = parent.findViewById(mAnchorId);
                }
            }
            return mAnchorView;
        }
    }

}

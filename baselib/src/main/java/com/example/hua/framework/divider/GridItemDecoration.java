package com.example.hua.framework.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.hua.framework.R;


/**
 * Created by hua on 2017/6/20.
 * 网格类型分割线，目前支持{@link GridLayoutManager}，后续会加上支持
 * {@link StaggeredGridLayoutManager}
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private Rect mBounds = new Rect();
    private int mDividerHeight;
    private final Context mContext;
    private boolean isVerticalContainPadding;
    private boolean isHorizontalContainPadding;
    public static final int ORITATION_VERTICAL = 0;
    public static final int ORITATION_HORIZONTAL = 1;

    public GridItemDecoration(Context context) {
        mContext = context;
        setDrawable(context.getResources().getDrawable(R.drawable.item_divider));
    }

    /**
     * 设置分割线的图片
     *
     * @param drawable
     */
    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
        mDividerHeight = mDivider.getIntrinsicHeight();
        if (mDividerHeight == -1) {
            mDividerHeight = mDivider.getIntrinsicWidth();
            if (mDividerHeight == -1) {
                throw new RuntimeException("invalid divider drawable");
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent == null) {
            return;
        }
        c.save();
        //画水平分割线
        drawHorizontal(c, parent);
        //画垂直分割线
        drawVertical(c, parent);
        c.restore();
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (isLastRow(parent, child)) { //这里就是直接不绘制
                break;
            }
            final int left = child.getLeft() + (isHorizontalContainPadding ? child.getPaddingLeft() : 0);
            final int right = child.getRight() - (isHorizontalContainPadding ? child.getRight() : 0);
            final int top = child.getBottom();
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            //这里选择下一个view为参考是因为如果item在xml中配置了具体的宽度时
            //通过getRight()得到的就不会是想要的值
            final View child = parent.getChildAt(i + 1);
            if (child == null) {
                break;
            }
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.left;
            final int left = right - mDividerHeight;
            final int top = mBounds.top + (isVerticalContainPadding ? child.getPaddingTop() : 0);
            final int bottom = mBounds.bottom - (isVerticalContainPadding ? child.getPaddingBottom() : 0);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int spanCount = -1;
        if (manager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) manager).getSpanCount();
        }
        return spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int bottomHeight = mDividerHeight;
        int rightWidth = mDividerHeight;
        if (isLastRow(parent, view)) {
            //是最后一行，不绘制底部。这里的不绘制原理是不预留绘制空间，实际上还是会绘制。下同
            bottomHeight -= mDividerHeight;
        }
        if (isLastColumn(parent, view)) {
            //是最后一列，不绘制右边
            rightWidth -= mDividerHeight;
        }
        outRect.set(0, 0, rightWidth, bottomHeight);
    }

    /**
     * 设置分割线是否包含padding距离。默认是不包含
     *
     * @param isContain true包含，false不包含
     */
    public void setDividerContainPadding(int oritation, boolean isContain) {
        if (oritation == ORITATION_HORIZONTAL) {
            isHorizontalContainPadding = isContain;
        } else if (oritation == ORITATION_VERTICAL) {
            isVerticalContainPadding = isContain;
        }
    }

    private boolean isLastRow(RecyclerView parent, View view) {
        final int childCount = parent.getAdapter().getItemCount();
        final int end = childCount % getSpanCount(parent);
        final int integerCount = childCount - (end == 0 ? getSpanCount(parent) : end);
        return (parent.getChildLayoutPosition(view) >= integerCount) &&
                isReachScreenBottom(parent);
    }

    private boolean isLastColumn(RecyclerView parent, View view) {
        final int position = parent.getChildLayoutPosition(view);
        return ((position + 1) % getSpanCount(parent) == 0) &&
                isReachScreenBottom(parent);
    }

    /**
     * 最初写这个方法的初衷是如果没有到屏幕底部，则最后一行需要绘制。很明显当recyclerView嵌在
     * 别的容器中时，底部是不能绘制的，但是此时并未到屏幕底部。
     *
     * 先注释吧。。。。
     */
    private boolean isReachScreenBottom(RecyclerView parent) {
        //return parent.getHeight() >= ScreenUtil.getScreenHeight(mContext);
        return true;
    }

}

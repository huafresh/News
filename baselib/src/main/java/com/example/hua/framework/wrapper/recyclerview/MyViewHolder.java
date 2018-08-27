package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hua.framework.R;
import com.example.hua.framework.wrapper.imageload.ImageLoad;

/**
 * recyclerView viewHolder封装
 *
 * @author hua
 * @date 2017/6/16
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewHashMap = new SparseArray<>();

    public MyViewHolder(View itemView) {
        super(itemView);
    }

    public static MyViewHolder createViewHolder(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View view = viewHashMap.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            if (view != null) {
                viewHashMap.put(id, view);
            }
        }
        return (T) view;
    }

    public MyViewHolder setText(@IdRes int id, String text) {
        TextView textView = getView(id);
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }

    public MyViewHolder setText(@IdRes int id, int text) {
        TextView textView = getView(id);
        if (textView != null) {
            textView.setText(String.valueOf(text));
        }
        return this;
    }

    public MyViewHolder setTextColor(@IdRes int id, int color) {
        TextView textView = getView(id);
        if (textView != null) {
            textView.setTextColor(color);
        }
        return this;
    }

    public MyViewHolder setVisibility(@IdRes int id, int visible) {
        View view = getView(id);
        if (view != null) {
            if (view.getVisibility() != visible) {
                view.setVisibility(visible);
            }
        }
        return this;
    }

    public int getVisibility(@IdRes int id) {
        View view = getView(id);
        if (view != null) {
            return view.getVisibility();
        }
        return -1;
    }

    public MyViewHolder setSwitchOnOff(@IdRes int id, boolean onOff) {
        SwitchCompat switchCompat = getView(id);
        if (switchCompat != null) {
            switchCompat.setSelected(onOff);
        }
        return this;
    }

    public MyViewHolder setSelected(@IdRes int id, boolean selected) {
        View view = getView(id);
        if (view != null) {
            view.setSelected(selected);
        }
        return this;
    }

    public MyViewHolder setImageResId(@IdRes int id, @DrawableRes int imgId) {
        ImageView imageView = getView(id);
        if (imageView != null) {
            imageView.setImageResource(imgId);
        }
        return this;
    }

    public MyViewHolder loadImage(@IdRes int id, String url) {
        final ImageView imageView = getView(id);
        if (imageView != null) {
            ImageLoad.loadNormalImage(imageView, url);
        }
        return this;
    }

    public MyViewHolder setBackgroundColor(@IdRes int id, int color) {
        final View view = getView(id);
        if (view != null) {
            view.setBackgroundColor(color);
        }
        return this;
    }

    public MyViewHolder setBackgroundRes(@IdRes int id, @DrawableRes int res) {
        final View view = getView(id);
        if (view != null) {
            view.setBackgroundResource(res);
        }
        return this;
    }

}

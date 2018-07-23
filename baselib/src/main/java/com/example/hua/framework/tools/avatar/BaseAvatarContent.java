package com.example.hua.framework.tools.avatar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hua.framework.R;
import com.example.hua.framework.interfaces.IWindow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 默认的图片选择对话框
 *
 * @author hua
 * @date 2017/7/12
 */

public class BaseAvatarContent implements View.OnClickListener, IWindow.IContentView {

    private View mContentView;

    /** 打开系统应用的类型 */
    public static final int CAMERA = 1000;
    public static final int ALBUM = 1001;

    @IntDef({CAMERA, ALBUM, -1})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Type {

    }

    /** 绑定的{@link AvatarHelper}对象 */
    private AvatarHelper mAvaterHelper;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (mAvaterHelper != null) {
            if (i == R.id.camera) {
                mAvaterHelper.openCamera();
            } else if (i == R.id.album) {
                mAvaterHelper.openAlbum();
            }
            mAvaterHelper.removeContentView(this);
        }
    }

    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(context).inflate(R.layout.change_avatar, null);

            TextView camera = (TextView) mContentView.findViewById(R.id.camera);
            TextView album = (TextView) mContentView.findViewById(R.id.album);
            TextView mCancel = (TextView) mContentView.findViewById(R.id.cancel);

            camera.setOnClickListener(this);
            album.setOnClickListener(this);
            mCancel.setOnClickListener(this);
        }
        return mContentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        if (container instanceof AvatarHelper) {
            mAvaterHelper = (AvatarHelper) container;
        }
    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        if (container instanceof AvatarHelper) {
            mAvaterHelper = null;
        }
    }

}

package hua.music.module_live.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hua.news.module_common.base.BaseFragment;
import hua.news.module_live.R;

/**
 * @author hua
 * @version 2018/3/26 17:48
 */

public class TempFragment extends BaseFragment {

    private TextView textView;
    private View mView;

    public static TempFragment getInstance(String name) {
        TempFragment fragment = new TempFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_temp, container, false);
            textView = mView.findViewById(R.id.fragment_name);
        }
        String name = getArguments().getString("name");
        textView.setText(name);
        return mView;
    }
}

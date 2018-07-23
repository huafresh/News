package hua.news.module_video.ifengdetail;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_video.R;

/**
 * @author hua
 * @version 2018/4/28 14:12
 */

public class IFengDetailFragment extends BaseFragment {

    public static final String TYPE_ID = "typeId";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String typeId;

    public static IFengDetailFragment newInstance(String typeId) {

        Bundle args = new Bundle();
        args.putString(TYPE_ID, typeId);

        IFengDetailFragment fragment = new IFengDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_detail;
    }

    @Override
    protected void initDatas() {
        Bundle args = getArguments();
        typeId = args.getString(TYPE_ID);
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity,DividerItemDecoration.HORIZONTAL));
        VideoDetailAdapter adapter = new VideoDetailAdapter()

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

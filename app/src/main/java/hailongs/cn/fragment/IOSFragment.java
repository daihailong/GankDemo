package hailongs.cn.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;
import hailongs.cn.gankdemo.BasicFragment;
import hailongs.cn.mvp.presenter.iimpl.IPostPresenter;
import hailongs.cn.mvp.presenter.impl.PostPresenter;
import hailongs.cn.mvp.view.iimpl.IPostView;
import hailongs.cn.utils.Constants;

/**
 * Created by dhl on 2016/12/4.
 */

public class IOSFragment extends BasicFragment implements IPostView {

    @Bind(R.id.srf_ly)
    SwipeRefreshLayout srf_layout;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    Context mContext;

    IPostPresenter presenter;
    static IOSFragment fragment = null;
    static Bundle bundle = null;

    int time = 0;

    public static IOSFragment newInstance(String tag) {
        if (fragment == null) {
            fragment = new IOSFragment();
        }
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString(Constants.IOS, tag);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.i("调用 onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.i("调用 onActivityCreated");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("调用 onActivityResult");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("调用 onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_content, container, false);
        ButterKnife.bind(this, view);
        Logger.i("IOS 的onCreateView 第 " + time++ + " 次创建");
        initViews(view);
        Logger.i("调用 onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.i("调用 onViewCreated");
        presenter = new PostPresenter(this, mContext);
        getDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("调用 onResume");
        this.isVisible = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.i("调用 onStop");
        this.isVisible = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("调用 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("调用 onDestroy");
    }

    @Override
    public void findView() {

    }

    @Override
    public void initViews(View view) {
        mContext = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int allItem = linearLayoutManager.getItemCount() - 2;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastItem >= allItem) {
                        //拉取数据
                        loadMore();
                    }
                    //根据返回数据条数设置
                }
            }
        });

        srf_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srf_layout.setColorSchemeColors(R.color.colorAccent, R.color.colorDivider, R.color.colorPrimary);
                getDatas();
            }
        });
    }

    @Override
    public void getDatas() {
        presenter.getPostList(Constants.IOS, mRecyclerView, true);
    }

    @Override
    public void lazyLoad() {
    }

    @Override
    public void loadMore() {
        presenter.getPostList(Constants.IOS, mRecyclerView, false);
    }

    @Override
    public void dismissRefreshLayout(boolean dismiss) {
        if (!(srf_layout.isRefreshing() == dismiss)) {
            srf_layout.setRefreshing(dismiss);
        }
    }
}

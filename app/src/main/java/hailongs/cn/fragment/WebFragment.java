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
import hailongs.cn.utils.Util;

/**
 * Created by dhl on 2016/12/4.
 */

public class WebFragment extends BasicFragment implements IPostView {

    @Bind(R.id.srf_ly)
    SwipeRefreshLayout srf_layout;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    private Context mContext;
    private View rootView = null;
    private boolean isPrepared = false;
    private boolean isHasLoadOnce = false;

    private IPostPresenter presenter;
    static WebFragment fragment = null;
    static Bundle bundle = null;

    int time = 0;

    public static WebFragment newInstance(String tag) {
        if (fragment == null) {
            fragment = new WebFragment();
        }
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString(Constants.WEB, tag);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.i("调 onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.i("调 onActivityCreated");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("调 onActivityResult");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("调 onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.post_content, container, false);
            ButterKnife.bind(this, rootView);
            mContext = rootView.getContext();
            initViews(rootView);
            isPrepared = true;
        } else {
            ButterKnife.bind(this, rootView);
        }
        
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        Logger.i("调用 onCreateView");
        lazyLoad();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.i("调 onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("调 onResume");
        this.isVisible = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.i("调 onStop");
        this.isVisible = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("调 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("调 onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ViewGroup parent = null;
        if (rootView != null) {
            parent = (ViewGroup) rootView.getParent();
        }
        if (parent != null) {
            parent.removeView(rootView);
        }
        if (rootView != null) {
            Logger.i("调用A onDetach");
            ButterKnife.unbind(this);
        }
    }

    @Override
    public void findView() {

    }

    @Override
    public void initViews(View view) {
        if (view == null) {
            Logger.i("rootView == null");
        } else {
            if (mRecyclerView == null) {
                Logger.i("recyclerview == null");
            }
        }
        presenter = new PostPresenter(this, mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                int allItem = linearLayoutManager.getItemCount() - 2;
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (lastItem >= allItem) {
//                        //拉取数据
//                        loadMore();
//                    }
//                }
                boolean isSlideToBottom = Util.isSlideToBottom(recyclerView);
                if (isSlideToBottom) {
                    loadMore();
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
        presenter.getPostList(Constants.WEB, mRecyclerView, true);
        isHasLoadOnce = true;
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || isHasLoadOnce) {
            return;
        }
        getDatas();
    }

    @Override
    public void loadMore() {
        presenter.getPostList(Constants.WEB, mRecyclerView, false);
    }

    @Override
    public void dismissRefreshLayout(boolean dismiss) {
        if (srf_layout != null)
            if (!(srf_layout.isRefreshing() == dismiss)) {
                srf_layout.setRefreshing(dismiss);
            }
    }
}

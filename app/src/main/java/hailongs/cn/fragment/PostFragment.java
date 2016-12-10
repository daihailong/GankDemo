package hailongs.cn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;
import hailongs.cn.adapter.PostVPAdapter;
import hailongs.cn.gankdemo.BasicFragment;
import hailongs.cn.mvp.view.iimpl.IPostView;
import hailongs.cn.utils.Constants;

/**
 * Created by dhl on 2016/12/4.
 */

public class PostFragment extends BasicFragment implements IPostView {

    @Bind(R.id.tab_post)
    TabLayout mTabLayout;
    @Bind(R.id.vp_post)
    ViewPager mViewPager;
    Context mContext;
    static PostFragment postFragment = null;
    static Bundle bundle = null;
    AndroidFragment androidFragment = null;
    IOSFragment iosFragment = null;
    WebFragment webFragment = null;
    ExtendFragment extendFragment = null;

    PostVPAdapter adapter = null;

    String titles[] = new String[]{
            "Android", "IOS", "前端", "拓展资源"
    };

    int time = 1;

    public static PostFragment newInstance(String tag) {
        if (postFragment == null) {
            postFragment = new PostFragment();
        }
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString(Constants.POST, tag);
            postFragment.setArguments(bundle);
        }
        return postFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment, container, false);
        ButterKnife.bind(this, view);
        Logger.i("Post 的onCreateView 第 " + time++ + " 次创建");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        if (androidFragment == null) {
            androidFragment = AndroidFragment.newInstance(Constants.ANDROID);
        }
        if (iosFragment == null) {
            iosFragment = IOSFragment.newInstance(Constants.IOS);
        }
        if (webFragment == null) {
            webFragment = WebFragment.newInstance(Constants.WEB);
        }
        if (extendFragment == null) {
            extendFragment = ExtendFragment.newInstance(Constants.EXTENDS);
        }
        fragments.add(androidFragment);
        fragments.add(iosFragment);
        fragments.add(webFragment);
        fragments.add(extendFragment);
        return fragments;
    }

    @Override
    public void findView() {
        initViews();
    }

    @Override
    public void initViews(View view) {

    }

    public void initViews() {
        mContext = mViewPager.getContext();
        List<Fragment> list = getFragmentList();
        if (adapter == null) {
            adapter = new PostVPAdapter(getChildFragmentManager(), list, titles, mContext);
        }
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.unSelectedColor), getResources().getColor(R.color.selectedColor));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setupWithViewPager(mViewPager);
        for (int index = 0; index < mTabLayout.getTabCount(); index++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(index);
            tab.setCustomView(adapter.getTabView(index));
        }
    }

    @Override
    public void getDatas() {

    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void dismissRefreshLayout(boolean dismiss) {

    }
}

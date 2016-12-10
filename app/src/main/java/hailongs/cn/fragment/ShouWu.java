package hailongs.cn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hailongs.cn.R;
import hailongs.cn.gankdemo.BasicFragment;
import hailongs.cn.utils.Constants;

/**
 * Created by dhl on 2016/12/5.
 */

public class ShouWu extends BasicFragment {

    static ShouWu shouWu = null;
    static Bundle bundle = null;

    public static ShouWu newInstance(String tag) {
        if (shouWu == null) {
            shouWu = new ShouWu();
        }
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString(Constants.POST, tag);
            shouWu.setArguments(bundle);
        }
        return shouWu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shouwu_content, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void findView() {

    }

    @Override
    public void initViews(View view) {
    }

    @Override
    public void getDatas() {

    }

    @Override
    public void lazyLoad() {

    }
}

package hailongs.cn.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import hailongs.cn.R;

/**
 * Fragment适配器
 * Created by dhl on 2016/12/4.
 */

public class PostVPAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;
    Context mContext;
    String titles[];
    int mImgIds[] = new int[]{
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
    };

    public PostVPAdapter(FragmentManager fm, List<Fragment> fragments, String titles[], Context mContext) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Logger.i("position = " + position);
        return titles[position];
    }

    //自定义返回 标题栏的方法
    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_content, null);
        //ImageView iv_tab = (ImageView) view.findViewById(R.id.iv_tab_title);
        TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab_title);
        tv_tab.setTextColor(view.getResources().getColor(R.color.selectedColor));
        //iv_tab.setImageResource(mImgIds[position]);
        tv_tab.setText(titles[position]);
        return view;
    }
}

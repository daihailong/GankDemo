package hailongs.cn.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;
import hailongs.cn.bean.FuliBean;
import hailongs.cn.gankdemo.ImageActivity;
import hailongs.cn.gankdemo.MainActivity;
import hailongs.cn.utils.Constants;
import hailongs.cn.utils.Util;

/**
 * Created by dhl on 2016/12/13.
 */

public class FuliAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity = null;
    List<FuliBean.ResultsBean> bean = new ArrayList<>();
    private int type;
    private boolean isNoMoreDatas = false;
    private int width = 0;

    public FuliAdapter(List<FuliBean.ResultsBean> bean) {
        this.bean = bean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        width = parent.getWidth();
        if (viewType == Constants.NORMAL_ITEM) {
            view = inflater.inflate(R.layout.fuli_item, parent, false);
            return new PicViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.loading, parent, false);
            return new FuliAdapter.FootViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            ((FootViewHolder) holder).setNoMoreDatas(isNoMoreDatas);
        } else if (holder instanceof PicViewHolder) {
            ((PicViewHolder) holder).bindDatas(bean.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return bean.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < bean.size()) {
            return Constants.NORMAL_ITEM;
        } else {
            return Constants.LOAD_MORE;
        }
    }

    public void isNoMoreDatas(boolean isNoMoreDatas) {
        this.isNoMoreDatas = isNoMoreDatas;
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ly_loading)
        LinearLayout ly_loading;
        @Bind(R.id.tv_loading)
        TextView tv_loading;
        @Bind(R.id.sdv_loading)
        SimpleDraweeView sdv_loading;
        DraweeController mDraweeController;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ly_loading.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            mDraweeController = Fresco.newDraweeControllerBuilder()
                    //加载drawable里的一张gif图
                    .setUri(Uri.parse("res://" + itemView.getContext().getPackageName() + "/" + R.drawable.loading))//设置uri
                    .setAutoPlayAnimations(true)
                    .build();
            //设置Controller
            sdv_loading.setController(mDraweeController);
        }

        public void setNoMoreDatas(boolean isNoMoreDatas) {
            if (sdv_loading != null && tv_loading != null)
                if (isNoMoreDatas) {
                    sdv_loading.setVisibility(View.GONE);
                    tv_loading.setText("我是有底线的!");
                } else {
                    sdv_loading.setVisibility(View.VISIBLE);
                    tv_loading.setText("拼命拉取中...");
                }
        }

        public void changeLoadingLayout(String res, String text) {
            mDraweeController = Fresco.newDraweeControllerBuilder()
                    //加载drawable里的一张gif图
                    .setUri(Uri.parse(res))//设置uri
                    .setAutoPlayAnimations(true)
                    .build();
            sdv_loading.setController(mDraweeController);
            tv_loading.setText(text);
        }
    }

}

class PicViewHolder extends RecyclerView.ViewHolder {

    Activity activity = null;

    Context mContext = null;
    @Bind(R.id.cv_item)
    CardView cv_item;
    @Bind(R.id.sdv_item)
    SimpleDraweeView sdv_item;
    @Bind(R.id.tv_author)
    TextView tv_author;
    @Bind(R.id.tv_desc)
    TextView tv_desc;

    ImageRequest request;

    public PicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void bindDatas(final FuliBean.ResultsBean bean) {
        String url = bean.getUrl();
        Logger.e("url = " + url);
        sdv_item.setImageURI(Uri.parse(url));
        tv_author.setText(bean.getWho());
        String date = Util.date(bean.getPublishedAt());
        tv_desc.setText(date);
        cv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.IMAGE_DETAIL, bean);
                intent.putExtra(Constants.IMAGE_DETAIL, bundle);
                //mContext.startActivity(intent);
                //共享元素动画
                //mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext), sdv_item, "mybtn").toBundle());
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext)).toBundle());
            }
        });
    }
}

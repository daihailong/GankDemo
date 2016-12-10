package hailongs.cn.mvp.model.iimpl.impl;

import android.content.Context;

import hailongs.cn.bean.Post;
import hailongs.cn.mvp.model.iimpl.IPostModel;
import hailongs.cn.mvp.presenter.iimpl.IPostPresenter;
import hailongs.cn.utils.Apis;
import rx.Observable;

/**
 * Created by dhl on 2016/11/29.
 */

public class PostModel implements IPostModel {

    IPostPresenter presenter;
    Context mContext;

    public PostModel(IPostPresenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
    }


    @Override
    public Observable<Post> getPostList(String type, int count, int page) {
        return Apis.Helper.getSimpleApi().getPostList(type, count, page);
    }
}

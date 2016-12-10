package hailongs.cn.mvp.model.iimpl;

import hailongs.cn.bean.Post;
import hailongs.cn.mvp.model.BasicModel;
import rx.Observable;

/**
 * Created by dhl on 2016/11/29.
 */

public interface IPostModel extends BasicModel {
    Observable<Post> getPostList(String type, int count, int page);
}

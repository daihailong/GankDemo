package hailongs.cn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl on 2016/12/14.
 */

public class FuliBean {

    /**
     * error : false
     * results : [{"_id":"585096f2421aa93437406727","createdAt":"2016-12-14T08:48:50.506Z","desc":"12-14","publishedAt":"2016-12-14T11:39:22.686Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034gw1faq15nnc0xj20u00u0wlq.jpg","used":true,"who":"代码家"},{"_id":"584f3bd6421aa934405ccfa7","createdAt":"2016-12-13T08:07:50.411Z","desc":"12-13","publishedAt":"2016-12-13T11:42:38.536Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1faoucp1idej20u011h0va.jpg","used":true,"who":"daimajia"},{"_id":"584dffdd421aa963eaaee172","createdAt":"2016-12-12T09:39:41.294Z","desc":"12-12","publishedAt":"2016-12-12T11:30:54.254Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1fanrdyaxi6j20u00k1ta9.jpg","used":true,"who":"daimajia"},{"_id":"584a0130421aa963f321b040","createdAt":"2016-12-09T08:56:16.913Z","desc":"12-9","publishedAt":"2016-12-09T11:33:12.481Z","source":"chrome","type":"福利","url":"http://ww2.sinaimg.cn/large/610dc034jw1fak99uh554j20u00u0n09.jpg","used":true,"who":"代码家"},{"_id":"5848c92e421aa963efd90da4","createdAt":"2016-12-08T10:45:02.271Z","desc":"12-8","publishedAt":"2016-12-08T11:42:08.186Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1faj6sozkluj20u00nt75p.jpg","used":true,"who":"代码家"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Parcelable {
        /**
         * _id : 585096f2421aa93437406727
         * createdAt : 2016-12-14T08:48:50.506Z
         * desc : 12-14
         * publishedAt : 2016-12-14T11:39:22.686Z
         * source : chrome
         * type : 福利
         * url : http://ww2.sinaimg.cn/large/610dc034gw1faq15nnc0xj20u00u0wlq.jpg
         * used : true
         * who : 代码家
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(_id);
            dest.writeString(createdAt);
            dest.writeString(desc);
            dest.writeString(publishedAt);
            dest.writeString(source);
            dest.writeString(type);
            dest.writeString(url);
            dest.writeInt((used ? 1 : 0));
            dest.writeString(who);
        }

        public static final Parcelable.Creator<FuliBean.ResultsBean> CREATOR = new Parcelable.Creator<FuliBean.ResultsBean>() {

            @SuppressWarnings("unchecked")
            @Override
            public FuliBean.ResultsBean createFromParcel(Parcel source) {
                return new FuliBean.ResultsBean(source);
            }

            @Override
            public FuliBean.ResultsBean[] newArray(int size) {
                return new FuliBean.ResultsBean[size];
            }

        };

        public ResultsBean(Parcel bean) {
            _id = bean.readString();
            createdAt = bean.readString();
            desc = bean.readString();
            publishedAt = bean.readString();
            source = bean.readString();
            type = bean.readString();
            url = bean.readString();
            used = (bean.readInt() == 1) ? true : false;
            who = bean.readString();
        }
    }
}

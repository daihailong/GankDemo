package hailongs.cn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dhl on 2016/11/25.
 */

public class Post {

    /**
     * error : false
     * results : [{"_id":"5833c3b3421aa926e43aef90","createdAt":"2016-11-22T12:04:03.555Z","desc":"随着 Android 引入 Java 8 的一些功能，请记住每一个标准库的 API 和语言特性都会带来一些相关的开销，这很重要。虽然设备越来越快而且内存越来越多，代码大小和性能优化之间仍然是有着紧密关联的。","images":["http://img.gank.io/b530a4e3-9ec8-4166-8c8f-fdd29e11c0d5","http://img.gank.io/8b3cf104-4b27-4dbd-8407-769d622ca077"],"publishedAt":"2016-11-23T11:27:52.847Z","source":"web","type":"Android","url":"https://realm.io/cn/news/360andev-jake-wharton-java-hidden-costs-android/","used":true,"who":"Chen Mulong"}]
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
         * _id : 5833c3b3421aa926e43aef90
         * createdAt : 2016-11-22T12:04:03.555Z
         * desc : 随着 Android 引入 Java 8 的一些功能，请记住每一个标准库的 API 和语言特性都会带来一些相关的开销，这很重要。虽然设备越来越快而且内存越来越多，代码大小和性能优化之间仍然是有着紧密关联的。
         * images : ["http://img.gank.io/b530a4e3-9ec8-4166-8c8f-fdd29e11c0d5","http://img.gank.io/8b3cf104-4b27-4dbd-8407-769d622ca077"]
         * publishedAt : 2016-11-23T11:27:52.847Z
         * source : web
         * type : Android
         * url : https://realm.io/cn/news/360andev-jake-wharton-java-hidden-costs-android/
         * used : true
         * who : Chen Mulong
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
        private List<String> images;


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

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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
            String[] imageArr = new String[images.size()];
            for (int index = 0; index < images.size(); index++) {
                imageArr[index] = images.get(index);
            }
            dest.writeStringArray(imageArr);
        }

        public static final Parcelable.Creator<ResultsBean> CREATOR = new Parcelable.Creator<ResultsBean>() {

            @SuppressWarnings("unchecked")
            @Override
            public ResultsBean createFromParcel(Parcel source) {
                return new ResultsBean(source);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
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
            String[] imageArr = bean.createStringArray();
            List<String> list = new ArrayList<>();
            for (int index = 0; index < imageArr.length; index++) {
                list.add(imageArr[index]);
            }
            images = list;
        }
    }
}

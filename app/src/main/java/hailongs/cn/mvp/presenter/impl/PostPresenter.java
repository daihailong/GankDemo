package hailongs.cn.mvp.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hailongs.cn.R;
import hailongs.cn.adapter.FuliAdapter;
import hailongs.cn.adapter.PostAdapter;
import hailongs.cn.bean.FuliBean;
import hailongs.cn.bean.Post;
import hailongs.cn.db.CacheDBHelper;
import hailongs.cn.gankdemo.MainActivity;
import hailongs.cn.gankdemo.PostDetail;
import hailongs.cn.mvp.presenter.iimpl.IPostPresenter;
import hailongs.cn.mvp.view.iimpl.IPostView;
import hailongs.cn.utils.Constants;
import hailongs.cn.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.POST;

/**
 * Created by dhl on 2016/11/29.
 */

public class PostPresenter implements IPostPresenter {

    public IPostView postView;
    private int allPage = 1;
    private int androidPage = 1;
    private int iOSPage = 1;
    private int webPage = 1;
    private int fuliPage = 1;
    private int extendsPage = 1;
    private int recommendPage = 1;
    private int relaxPage = 1;
    private int appPage = 1;
    private boolean isReload = false;
    private Context mContext;
    private Handler mHandler;

    private PostAdapter androidAdapter = null;
    private PostAdapter iOSAdapter = null;
    private PostAdapter webAdapter = null;
    private PostAdapter relaxAdapter = null;
    private FuliAdapter fuliAdapter = null;
    private PostAdapter extendsAdapter = null;
    private PostAdapter recommendAdapter = null;
    private PostAdapter appAdapter = null;
    private PostAdapter allAdapter = null;

    private List<Post.ResultsBean> allPost = new ArrayList<>();
    private List<Post.ResultsBean> androidPost = new ArrayList<>();
    private List<Post.ResultsBean> iOSPost = new ArrayList<>();
    private List<Post.ResultsBean> webPost = new ArrayList<>();
    private List<FuliBean.ResultsBean> fuliPost = new ArrayList<>();
    private List<Post.ResultsBean> relaxPost = new ArrayList<>();
    private List<Post.ResultsBean> recommendPost = new ArrayList<>();
    private List<Post.ResultsBean> appPost = new ArrayList<>();
    private List<Post.ResultsBean> extendsPost = new ArrayList<>();

    private SQLiteDatabase allDB;
    private SQLiteDatabase androidDB;
    private SQLiteDatabase iosDB;
    private SQLiteDatabase webDB;
    private SQLiteDatabase fuliDB;
    private SQLiteDatabase relaxDB;
    private SQLiteDatabase recommendDB;
    private SQLiteDatabase appDB;
    private SQLiteDatabase extendsDB;


    public PostPresenter(IPostView postView, Context mContext) {
        this.postView = postView;
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
        initDB();
    }

    public void initDB() {
        allDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        androidDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        iosDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        webDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        fuliDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        relaxDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        recommendDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        appDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
        extendsDB = ((MainActivity) mContext).getCacheDBHelper().getReadableDatabase();
    }

    @Override
    public void getPostList(String type, RecyclerView recyclerView, boolean isReload) {
        //这TM好像有问题， 貌似1.7才能用string
        switch (type) {
            case Constants.ALL:
                //全部
                getAllPost(recyclerView, isReload);
                break;
            case Constants.ANDROID:
                //android
                getAndroidPost(recyclerView, isReload);
                break;
            case Constants.IOS:
                //iOS
                getIOSPost(recyclerView, isReload);
                break;
            case Constants.WEB:
                //前端
                getWebPost(recyclerView, isReload);
                break;
            case Constants.FULI:
                //福利
                getFuliPost(recyclerView, isReload);
                break;
            case Constants.RELAX:
                //休息视频
                getRelaxPost(recyclerView, isReload);
                break;
            case Constants.EXTENDS:
                //拓展资源
                getExtendsPost(recyclerView, isReload);
                break;
            case Constants.RECOMMEND:
                getRecommendPost(recyclerView, isReload);
                break;
            case Constants.APP:
                getAppPost(recyclerView, isReload);
        }
    }


    /**
     * 获取全部post
     */
    private void getAllPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            allPage = 1;
            allPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/all/" + Constants.COUNT + "/" + allPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = allPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                allPost.addAll(from, beanList);
                                allAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (allAdapter != null) {
                                    allAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.ALL);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    allAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取Android分类的post
     */
    private void getAndroidPost(final RecyclerView recyclerView, final boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            androidPage = 1;
            androidPost.clear();
        }
        final int from = androidPost.size();
        //添加网络判断逻辑
        if (HttpUtils.isNetworkConnected(mContext)) {
            OkHttpClient client = new OkHttpClient();
            String url = Constants.baseUrl + "data/Android/" + Constants.COUNT + "/" + androidPage++;
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    if (json != null) {
                        if (isReload) {
                            androidPost.clear();
                        }
                        if (androidPage == 2) {
                            json.replace("’", "'");
                            androidDB.execSQL("replace into cache(type,json) values("     //直接replace
                                    + Constants.ANDROID_ID + ",'" + json + "')");
                        }
                        final Post post = new Gson().fromJson(json, Post.class);
                        final List<Post.ResultsBean> beanList = post.getResults();
                        if (post != null && beanList != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    androidPost.addAll(from, beanList);
                                    if (recyclerView != null) {
                                        androidAdapter = (PostAdapter) recyclerView.getAdapter();
                                    }
                                    if (androidAdapter != null && recyclerView != null) {
                                        if (isReload) {
                                            androidAdapter.notifyDataSetChanged();
                                        } else
                                            androidAdapter.notifyItemChanged(from, beanList.size());
                                    } else {
                                        initRecyclerView(recyclerView, Constants.ANDROID);
                                    }
                                    if (beanList.size() < Constants.COUNT) {
                                        androidAdapter.isNoMoreDatas(true);
                                    }
                                    postView.dismissRefreshLayout(false);
                                }
                            });
                        }
                    }
                }
            });
        } else {
            if (isReload) {
                Cursor cursor = androidDB.rawQuery("select * from cache where type = " + Constants.ANDROID_ID, null);
                if (cursor.getCount() == 0) {
                    Toast.makeText(mContext, "无缓存数据", Toast.LENGTH_SHORT).show();
                }
                if (cursor.moveToFirst()) {
                    postView.dismissRefreshLayout(false);
                    //做成对话框比较好点
                    Toast.makeText(mContext, "正在读取缓存数据...", Toast.LENGTH_LONG).show();
                    String json = cursor.getString(cursor.getColumnIndex("json"));
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            androidPost.addAll(0, beanList);
                            if (recyclerView != null) {
                                androidAdapter = (PostAdapter) recyclerView.getAdapter();
                            }
                            if (androidAdapter != null && recyclerView != null) {
                                androidAdapter.notifyDataSetChanged();
                            } else {
                                initRecyclerView(recyclerView, Constants.ANDROID);
                            }
                            postView.dismissRefreshLayout(false);
                        }
                    });
                }
            } else {
                String res = "res://" + mContext.getPackageName() + "/" + R.drawable.loading_failed;
                String text = "加载失败";
                androidAdapter.changeLoadingLayout(res, text);
                //Toast.makeText(mContext, "网络连接超时，请检查您的网络！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取iOS分类的post
     */
    private void getIOSPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            iOSPage = 1;
            iOSPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/iOS/" + Constants.COUNT + "/" + iOSPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = iOSPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iOSPost.addAll(from, beanList);
                                iOSAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (iOSAdapter != null) {
                                    iOSAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.IOS);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    iOSAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取前端分类的post
     */
    private void getWebPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            webPage = 1;
            webPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/前端/" + Constants.COUNT + "/" + webPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = webPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                webPost.addAll(from, beanList);
                                webAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (webAdapter != null) {
                                    webAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.WEB);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    webAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取福利分类的post
     */
    private void getFuliPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            fuliPage = 1;
            fuliPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/福利/" + Constants.COUNT + "/" + fuliPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = fuliPost.size();
                    final FuliBean post = new Gson().fromJson(json, FuliBean.class);
                    final List<FuliBean.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fuliPost.addAll(from, beanList);
                                fuliAdapter = (FuliAdapter) recyclerView.getAdapter();
                                if (fuliAdapter != null) {
                                    fuliAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.FULI);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    fuliAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取拓展资源分类的post
     */
    private void getExtendsPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            extendsPage = 1;
            extendsPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/拓展资源/" + Constants.COUNT + "/" + extendsPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = extendsPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                extendsPost.addAll(from, beanList);
                                extendsAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (extendsAdapter != null) {
                                    extendsAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.EXTENDS);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    extendsAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取休息视频分类的post
     */
    private void getRelaxPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            relaxPage = 1;
            relaxPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/休息视频/" + Constants.COUNT + "/" + relaxPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = relaxPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                relaxPost.addAll(from, beanList);
                                relaxAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (relaxAdapter != null) {
                                    relaxAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.RELAX);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    relaxAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    private void getRecommendPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            recommendPage = 1;
            recommendPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/瞎推荐/" + Constants.COUNT + "/" + recommendPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = recommendPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recommendPost.addAll(from, beanList);
                                recommendAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (recommendAdapter != null) {
                                    recommendAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.RECOMMEND);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    recommendAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    private void getAppPost(final RecyclerView recyclerView, boolean isReload) {
        if (isReload) {
            postView.dismissRefreshLayout(true);
            appPage = 1;
            appPost.clear();
        }
        //添加网络判断逻辑
        OkHttpClient client = new OkHttpClient();
        String url = Constants.baseUrl + "data/App/" + Constants.COUNT + "/" + appPage++;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    final int from = appPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    final List<Post.ResultsBean> beanList = post.getResults();
                    if (post != null && beanList != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                appPost.addAll(from, beanList);
                                appAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (appAdapter != null) {
                                    appAdapter.notifyItemChanged(from, beanList.size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.APP);
                                }
                                if (beanList.size() < Constants.COUNT) {
                                    appAdapter.isNoMoreDatas(true);
                                }
                                postView.dismissRefreshLayout(false);
                            }
                        });
                    }
                }
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView, final String type) {

        switch (type) {
            case Constants.ALL:
                //全部
                allAdapter = new PostAdapter(allPost);
                recyclerView.setAdapter(allAdapter);
                break;
            case Constants.ANDROID:
                //android
                androidAdapter = new PostAdapter(androidPost);
                recyclerView.setAdapter(androidAdapter);
                break;
            case Constants.IOS:
                //iOS
                iOSAdapter = new PostAdapter(iOSPost);
                recyclerView.setAdapter(iOSAdapter);
                break;
            case Constants.WEB:
                //前端
                webAdapter = new PostAdapter(webPost);
                recyclerView.setAdapter(webAdapter);
                break;
            case Constants.FULI:
                //福利
                fuliAdapter = new FuliAdapter(fuliPost);
                recyclerView.setAdapter(fuliAdapter);
                break;
            case Constants.RELAX:
                //休息视频
                relaxAdapter = new PostAdapter(relaxPost);
                recyclerView.setAdapter(relaxAdapter);
                break;
            case Constants.EXTENDS:
                //拓展资源
                extendsAdapter = new PostAdapter(extendsPost);
                recyclerView.setAdapter(extendsAdapter);
                break;
            case Constants.RECOMMEND:
                recommendAdapter = new PostAdapter(recommendPost);
                recyclerView.setAdapter(recommendAdapter);
                break;
            case Constants.APP:
                appAdapter = new PostAdapter(appPost);
                recyclerView.setAdapter(appAdapter);
                break;
        }

    }
}

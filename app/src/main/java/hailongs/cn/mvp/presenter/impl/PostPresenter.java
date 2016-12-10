package hailongs.cn.mvp.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hailongs.cn.adapter.PostAdapter;
import hailongs.cn.bean.Post;
import hailongs.cn.mvp.presenter.iimpl.IPostPresenter;
import hailongs.cn.mvp.view.iimpl.IPostView;
import hailongs.cn.utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private boolean isReload = false;
    private Context mContext;
    private Handler mHandler;

    private PostAdapter androidAdapter = null;
    private PostAdapter iOSAdapter = null;
    private PostAdapter webAdapter = null;
    private PostAdapter relaxAdapter = null;
    private PostAdapter fuliAdapter = null;
    private PostAdapter extendsAdapter = null;
    private PostAdapter recommendAdapter = null;
    private PostAdapter allAdapter = null;

    private List<Post.ResultsBean> allPost = new ArrayList<>();
    private List<Post.ResultsBean> androidPost = new ArrayList<>();
    private List<Post.ResultsBean> iOSPost = new ArrayList<>();
    private List<Post.ResultsBean> webPost = new ArrayList<>();
    private List<Post.ResultsBean> fuliPost = new ArrayList<>();
    private List<Post.ResultsBean> relaxPost = new ArrayList<>();
    private List<Post.ResultsBean> recommendPost = new ArrayList<>();
    private List<Post.ResultsBean> extendsPost = new ArrayList<>();

    public PostPresenter(IPostView postView, Context mContext) {
        this.postView = postView;
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                allPost.addAll(from, post.getResults());
                                allAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (allAdapter != null) {
                                    allAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.ALL);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
        //添加网络判断逻辑
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
                    final int from = androidPost.size();
                    final Post post = new Gson().fromJson(json, Post.class);
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                androidPost.addAll(from, post.getResults());
                                if (recyclerView != null) {
                                    androidAdapter = (PostAdapter) recyclerView.getAdapter();
                                }
                                if (androidAdapter != null && recyclerView != null) {
                                    if (isReload) {
                                        androidAdapter.notifyDataSetChanged();
                                    } else
                                        androidAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.ANDROID);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
                                    androidAdapter.isNoMoreDatas(true);
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iOSPost.addAll(from, post.getResults());
                                iOSAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (iOSAdapter != null) {
                                    iOSAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.IOS);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                webPost.addAll(from, post.getResults());
                                webAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (webAdapter != null) {
                                    webAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.WEB);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                    final Post post = new Gson().fromJson(json, Post.class);
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fuliPost.addAll(from, post.getResults());
                                fuliAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (fuliAdapter != null) {
                                    fuliAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.FULI);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                extendsPost.addAll(from, post.getResults());
                                extendsAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (extendsAdapter != null) {
                                    extendsAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.EXTENDS);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                relaxPost.addAll(from, post.getResults());
                                relaxAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (relaxAdapter != null) {
                                    relaxAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.RELAX);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                    if (post != null && post.getResults() != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                recommendPost.addAll(from, post.getResults());
                                recommendAdapter = (PostAdapter) recyclerView.getAdapter();
                                if (recommendAdapter != null) {
                                    recommendAdapter.notifyItemChanged(from, post.getResults().size());
                                } else {
                                    initRecyclerView(recyclerView, Constants.RECOMMEND);
                                }
                                if (post.getResults().size() < Constants.COUNT) {
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
                fuliAdapter = new PostAdapter(fuliPost);
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

        }

    }
}

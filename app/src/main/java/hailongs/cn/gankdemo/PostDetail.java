package hailongs.cn.gankdemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;
import hailongs.cn.bean.Post;
import hailongs.cn.utils.Constants;

public class PostDetail extends AppCompatActivity {

    @Bind(R.id.wv_detail)
    WebView mWebView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ly_loading)
    LinearLayout ly_loading;
    @Bind(R.id.sdv_loading)
    SimpleDraweeView sdv_loading;
    @Bind(R.id.tv_loading)
    TextView tv_loading;

    @Bind(R.id.fl_video)
    FrameLayout fl_video;
    VideoWebChromeClient videoWebChromeClient;
    private View videoView = null;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;

    //toolbar
    private LinearLayout toolbar;
    private ImageView iv_back;
    private TextView tv_title;

    private View ly_video_loading;
    private SimpleDraweeView sdv_video_loading;
    private TextView tv_video_loading;

    private DraweeController mDraweeController;

    private Post.ResultsBean bean = null;
    private String url = null;
    private String title = null;
    private final String TAG = "PostDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.ITEM_DATA);
        if (bundle != null) {
            bean = bundle.getParcelable(Constants.ITEM_DATA);
            if (bean != null) {
                url = bean.getUrl();
                title = bean.getDesc();
                Logger.e("url = " + url);
            }
        }

        initView();

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginsEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        String path = getFilesDir().getAbsolutePath() + Constants.APP_CACHE_DIRNAME;
        mWebView.getSettings().setAppCachePath(path);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ly_loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        videoWebChromeClient = new VideoWebChromeClient();
        mWebView.setWebChromeClient(videoWebChromeClient);
        mWebView.loadUrl(url);

        clearWebViewCache();
    }

    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.top_back, null, false);
        toolbar = (LinearLayout) view.findViewById(R.id.ly_toolbar);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_title.setText(title);

        mToolbar.setTitle("");
        mToolbar.addView(view);
        setSupportActionBar(mToolbar);


        String uri = "res://" + this.getPackageName() + "/" + R.drawable.loading;
        mDraweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(uri))
                .setAutoPlayAnimations(true)
                .build();
        sdv_loading.setController(mDraweeController);

        tv_loading.setText("拼命加载中...");
    }

    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            //deleteDatabase("webview.db");
            //deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + Constants.APP_CACHE_DIRNAME);
        Logger.i(TAG + "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        Logger.i(TAG + "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            Logger.i("webview缓存目录存在过！");
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            Logger.i("app缓存目录存在过！");
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Logger.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Logger.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isInVideoView()) {
                hideVideoView();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏

        }
    }


    public boolean isInVideoView() {
        return (videoView != null);
    }

    public void hideVideoView() {
        videoWebChromeClient.onHideCustomView();
    }

    public class VideoWebChromeClient extends WebChromeClient {
        DraweeController controller;

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (videoView == null)//不是全屏播放状态
                return;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fl_video.setVisibility(View.GONE);
            // Remove the custom view from its container.
            fl_video.removeView(videoView);
            videoView = null;
            fl_video.setVisibility(View.GONE);
            customViewCallback.onCustomViewHidden();
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            toolbar.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fl_video.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (videoView != null) {
                callback.onCustomViewHidden();
                return;
            }
            fl_video.addView(view);
            videoView = view;
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (ly_video_loading == null) {
                LayoutInflater inflater = LayoutInflater.from(PostDetail.this);
                ly_video_loading = inflater.inflate(R.layout.loading, null, false);
            }
            sdv_video_loading = (SimpleDraweeView) ly_video_loading.findViewById(R.id.sdv_loading);
            tv_video_loading = (TextView) ly_video_loading.findViewById(R.id.tv_loading);
            String uri = "res://" + getPackageName() + "/" + R.drawable.loading;
            controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(uri))
                    .setAutoPlayAnimations(true)
                    .build();
            sdv_video_loading.setController(controller);

            tv_video_loading.setText("视频加载中...");
            return ly_video_loading;
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            Logger.e("progress = " + i);
            if(i >= 50){
                ly_loading.setVisibility(View.GONE);
            }
        }
    }


}

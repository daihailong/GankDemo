package hailongs.cn.gankdemo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;
import hailongs.cn.bean.Post;
import hailongs.cn.utils.Constants;

public class PostDetail extends AppCompatActivity {

    @Bind(R.id.wv_detail)
    private WebView mWebView;
    @Bind(R.id.toolbar)
    private Toolbar mToolbar;
    @Bind(R.id.ly_loading)
    private LinearLayout ly_loading;
    @Bind(R.id.sdv_loading)
    private SimpleDraweeView sdv_loading;
    @Bind(R.id.tv_loading)
    private TextView tv_loading;

    //toolbar
    private ImageView iv_back;
    private TextView tv_title;

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
            }
        }

        initView();

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
                ly_loading.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(url);

        clearWebViewCache();
    }

    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.top_back, null, false);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_title.setText(title);

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
}

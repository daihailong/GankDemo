package hailongs.cn.gankdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import hailongs.cn.R;
import hailongs.cn.bean.FuliBean;
import hailongs.cn.utils.Constants;

public class ImageActivity extends AppCompatActivity {

    FuliBean.ResultsBean bean = null;
    String url = null;
    @Bind(R.id.sdv_detail)
    SimpleDraweeView sdv_detail;
    Postprocessor postprocessor;
    DraweeController controller;
    ImageRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.IMAGE_DETAIL);
        if (bundle != null) {
            bean = bundle.getParcelable(Constants.IMAGE_DETAIL);
            if (bean != null) {
                url = bean.getUrl();
                Logger.e("url = " + url);
            }
        }
        sdv_detail.setImageURI(Uri.parse(url));

        postprocessor = new Postprocessor() {
            @Override
            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(
                        sourceBitmap.getWidth() *3/ 4,
                        sourceBitmap.getHeight()*3/4);
                try {
                    Bitmap destBitmap = bitmapRef.get();
                    for (int x = 0; x < destBitmap.getWidth(); x += 2) {
                        for (int y = 0; y < destBitmap.getHeight(); y += 2) {
                            destBitmap.setPixel(x, y, sourceBitmap.getPixel(x, y));
                        }
                    }
                    return CloseableReference.cloneOrNull(bitmapRef);
                } finally {
                    CloseableReference.closeSafely(bitmapRef);
                }
            }

            @Override
            public String getName() {
                return null;
            }

            @Nullable
            @Override
            public CacheKey getPostprocessorCacheKey() {
                return null;
            }
        };
        request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();

        controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(sdv_detail.getController())
                // other setters as you need
                .build();
        sdv_detail.setController(controller);
    }

    @OnClick(R.id.sdv_detail)
    public void exit(View view) {
        onBackPressed();
    }

    @OnLongClick(R.id.sdv_detail)
    public boolean operateImage(View view) {
        Logger.e("长按图片");
        request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setPostprocessor(postprocessor)
                .build();

        controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(sdv_detail.getController())
                // other setters as you need
                .build();
        sdv_detail.setController(controller);
        return true;
    }
}

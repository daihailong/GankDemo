package hailongs.cn.gankdemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hailongs.cn.R;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.iv_test)
    SimpleDraweeView iv_load_more;

    DraweeController mDraweeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        mDraweeController = Fresco.newDraweeControllerBuilder()
                //加载drawable里的一张gif图
                .setUri(Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.test))//设置uri
                .setAutoPlayAnimations(true)
                .build();
        //设置Controller
        iv_load_more.setController(mDraweeController);
    }
}

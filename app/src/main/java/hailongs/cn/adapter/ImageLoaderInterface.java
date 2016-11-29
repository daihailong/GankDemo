package hailongs.cn.adapter;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by dhl on 2016/11/27.
 */

public interface ImageLoaderInterface {
    void displayImage(Context context, Object path, ImageView imageView);
    ImageView createImageView(Context context);
}

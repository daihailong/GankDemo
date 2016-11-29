package hailongs.cn.adapter;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by dhl on 2016/11/27.
 */

public abstract class ImageLoader implements ImageLoaderInterface {
    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}

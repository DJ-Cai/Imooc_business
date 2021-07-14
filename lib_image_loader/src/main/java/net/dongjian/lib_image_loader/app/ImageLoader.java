package net.dongjian.lib_image_loader.app;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import net.dongjian.lib_image_loader.R;
import net.dongjian.lib_image_loader.image.CustomRequestListener;

/**
 * 图像加载类，外界加载图片唯一调用的类
 * 可以为view、notification、appwidgt、viewGroup加载图片
 */
public class ImageLoader {
    private ImageLoader() {
    }

    private static volatile ImageLoader imageLoader = null;

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                if (imageLoader == null) {
                    imageLoader = new ImageLoader();
                }
            }
        }
        return imageLoader;
    }

    public void displayImageForView(ImageView imageView, String url, CustomRequestListener requestListener){
        //1、with(context)   2、load(url) 加载url  3、into(view) 加载进view中    图片加载的三要素
        Glide.with(imageView.getContext())
                .asBitmap()              //asBitmap ： 转化为bitmap  后可以使用  transition--图片加载的过渡效果
                .transition(BitmapTransitionOptions.withCrossFade())
                .apply(initCommonOptions()) //apply：使用自己设定的加载配置
                .load(url)
                .into(imageView);

    }

    private BaseRequestOptions<?> initCommonOptions() {
        RequestOptions options = new RequestOptions();
        //placeholder 指定占位图 ， error出错后的错误图 ，
        //diskCacheStrategy缓存策略 ，skipMemoryCache跳过内存缓存，priority图片下载优先级
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
    }

}

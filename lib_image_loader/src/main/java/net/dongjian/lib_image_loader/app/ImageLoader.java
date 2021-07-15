package net.dongjian.lib_image_loader.app;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

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

    public void displayImageForView(ImageView imageView, String url){
        //1、with(context)   2、load(url) 加载url  3、into(view) 加载进view中    图片加载的三要素
        Glide.with(imageView.getContext())
                .asBitmap()              //asBitmap ： 转化为bitmap  后可以使用  transition--图片加载的过渡效果
                .transition(BitmapTransitionOptions.withCrossFade())
                .apply(initCommonOptions()) //apply：使用自己设定的加载配置
                .load(url)
                .into(imageView);
    }

    public void displayImageForCircle(ImageView imageView , String url){
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(initCommonOptions())
                .load(url)
                .into(new BitmapImageViewTarget(imageView){
                    //将imageView包装成target
                    @Override
                    protected void setResource(Bitmap resource) {
                        //构造圆形drawable
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(),resource);
                        circularBitmapDrawable.setCircular(true);
                        //设置imageView的drawable
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

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
        return options;
    }

}

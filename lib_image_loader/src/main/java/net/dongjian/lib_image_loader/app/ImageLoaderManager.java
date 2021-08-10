package net.dongjian.lib_image_loader.app;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import net.dongjian.lib_image_loader.R;
import net.dongjian.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图像加载类，外界加载图片唯一调用的类
 * 可以为view、notification、appwidgt、viewGroup加载图片
 */
public class ImageLoaderManager {
    private ImageLoaderManager() {
    }

    private static volatile ImageLoaderManager imageLoaderManager = null;

    public static ImageLoaderManager getInstance() {
        if (imageLoaderManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (imageLoaderManager == null) {
                    imageLoaderManager = new ImageLoaderManager();
                }
            }
        }
        return imageLoaderManager;
    }

//    //这里用的是内部类的构造，换成DCL吧---静态内部类的单例是最好的
//    private static class SingletonHolder {
//        private static ImageLoaderManager instance = new ImageLoaderManager();
//    }
//
//    public static ImageLoaderManager getInstance() {
//        return ImageLoaderManager.SingletonHolder.instance;
//    }



    /**
     * 为view加载正常的图片
     *
     * @param imageView
     * @param url
     */
    public void displayImageForView(ImageView imageView, String url) {
        //1、with(context)   2、load(url) 加载url  3、into(view) 加载进view中    图片加载的三要素
        Glide.with(imageView.getContext())
                .asBitmap()              //asBitmap ： 转化为bitmap  后可以使用  transition--图片加载的过渡效果
                .transition(BitmapTransitionOptions.withCrossFade())
                .apply(initCommonOptions()) //apply：使用自己设定的加载配置
                .load(url)
                .into(imageView);
    }

    /**
     * 为view加载圆形的图片
     *
     * @param imageView
     * @param url
     */
    public void displayImageForCircle(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(initCommonOptions())
                .load(url)
                .into(new BitmapImageViewTarget(imageView) {
                    //将imageView包装成target
                    @Override
                    protected void setResource(Bitmap resource) {
                        //构造圆形drawable
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        //设置imageView的drawable
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 为ViewGroup设置背景并对其进行高斯模糊处理
     *
     * @param viewGroup
     * @param url
     */
    public void displayImageForViewGroup(ViewGroup viewGroup, String url) {
        Glide.with(viewGroup.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    //当调用这个方法时，线程已经回到主线程了，但直接加载图片并进行高斯模糊处理 是一个耗时操作
                    //所以需要用到rxJava来做线程的切换操作
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        Bitmap res = resource;
                        //1、只操作一个对象，所以用just
                        Observable.just(res)
                                //2-1、用map() 将bitmap转换为drawable
                                //2-2、同时用Utils.doBlur进行高斯模糊处理 Utils.doBlur(bitmap,模糊度数，是否复用)
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                                        Drawable drawable = new BitmapDrawable(Utils.doBlur(res, 100, true));
                                        return drawable;
                                    }
                                })
                                //3、指定上述耗时操作在子线程上进行
                                .subscribeOn(Schedulers.io())
                                //4、指定消费者在主线程上
                                .observeOn(AndroidSchedulers.mainThread())
                                //5、指定消费者消费drawable
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        viewGroup.setBackground(drawable);
                                    }
                                });
                    }
                });
    }

    /**
     * 为通知（非View）中的控件加载图片
     *
     * @param context
     * @param id
     * @param rv
     * @param notification
     * @param NOTIFICATION_ID
     * @param url
     */
    public void displayImageForNotification(Context context, int id, RemoteViews rv,
                                            Notification notification, int NOTIFICATION_ID, String url) {
        //初始化Notificaiton的Target
        NotificationTarget notificationTarget = new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
        displayImageForTarget(context, notificationTarget, url);
    }

    private void displayImageForTarget(Context context, Target target, String url) {
        Glide.with(context)
                .asBitmap()
                .apply(initCommonOptions())
                .load(url)
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .into(target);
    }


    /**
     * 默认规定一些加载图片的配置
     *
     * @return
     */
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

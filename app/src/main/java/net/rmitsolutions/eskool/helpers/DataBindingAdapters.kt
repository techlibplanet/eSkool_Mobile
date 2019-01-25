package net.rmitsolutions.eskool.helpers

import android.databinding.BindingAdapter
import android.support.annotation.Nullable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Madhu on 18-Jul-2017.
 */
class DataBindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("android:src")
        fun setImageSource(imageView: ImageView, imageResId: Int) {
            imageView.setImageDrawable(imageView.context.getDrawable(imageResId))
        }

        @JvmStatic
        @BindingAdapter("imgSrc")
        fun setImageResource(imageView: ImageView, imgSrc: Int) {
            val context = imageView.context
            val opts = RequestOptions()
            opts.dontAnimate()
            opts.fallback(imgSrc)
            Glide.with(context).load(null)
                    .apply(opts)
                    .into(imageView)
        }
    }


}
package kr.co.releasetech.kiosk.view.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

@BindingAdapter(value = ["imgUrl"])
fun setImageResource(v: ImageView, imgUrl: String){
    Glide.with(v.context)
        .load(File(imgUrl))
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(v)
}
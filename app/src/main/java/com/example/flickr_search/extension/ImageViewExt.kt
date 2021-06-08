package com.example.flickr_search.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.flickr_search.R

fun ImageView.load(url: String, placeholder: Int = R.drawable.ic_launcher_background) {


    Glide.with(context)
        .load(url)
        .centerCrop()
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)


}
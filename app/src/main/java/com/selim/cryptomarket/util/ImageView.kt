package com.selim.cryptomarket.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.selim.cryptomarket.R

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(this)
}

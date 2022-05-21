package com.selim.cryptomarket.util

import android.app.Activity

fun Activity.restart() {
    finish()
    startActivity(intent)
}

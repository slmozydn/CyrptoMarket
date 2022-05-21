package com.selim.cryptomarket.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

inline fun <T : ViewBinding> View.viewBinding(crossinline factory: (View) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(this)
    }

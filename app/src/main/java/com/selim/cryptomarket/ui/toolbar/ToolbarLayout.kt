package com.selim.cryptomarket.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.LayoutToolbarBinding
import com.selim.cryptomarket.util.viewBinding

class ToolbarLayout constructor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    val binding by viewBinding(LayoutToolbarBinding::bind)

    var onSettingsClicked: (() -> Unit)? = null
    var onThemeClicked: (() -> Unit)? = null
    var onSearchViewClicked: (() -> Unit)? = null
    var onBackButtonClicked: (() -> Unit)? = null
    var onCancelTextClicked: (() -> Unit)? = null

    init {
        inflate(context, R.layout.layout_toolbar, this)
    }

    fun render(toolbarConfig: ToolbarConfig) = with(binding) {
        with(toolbarConfig) {
            titleTextView.apply {
                val title = titleRes?.let { context.getString(it) }
                isVisible = title.isNullOrEmpty().not()
                text = title
            }
            searchView.apply {
                isVisible = searchVisible
                queryHint = context.getString(searchHintRes)
                setQuery("", false)
                setOnQueryTextFocusChangeListener { _, _ -> onSearchViewClicked?.invoke() }
            }
            settingsImageView.apply {
                isVisible = settingsVisible
                setOnClickListener { onSettingsClicked?.invoke() }
            }
            themeImageView.apply {
                isVisible = themeVisible
                if (themeVisible.not()) return@apply

                setOnClickListener { onThemeClicked?.invoke() }
                changeTheme(isDarkMode)
            }
            cancelTextView.apply {
                isVisible = cancelTextVisible
                setOnClickListener { onCancelTextClicked?.invoke() }
            }
            backButton.apply {
                isVisible = backButtonVisible
                setOnClickListener { onBackButtonClicked?.invoke() }
            }
        }
    }

    private fun changeTheme(isDarkMode: Boolean) {
        val (nightMode, themeResId) = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES to R.drawable.icon_sun
        } else {
            AppCompatDelegate.MODE_NIGHT_NO to R.drawable.icon_night
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        binding.themeImageView.setImageResource(themeResId)
    }
}

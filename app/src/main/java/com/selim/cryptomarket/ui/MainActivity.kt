package com.selim.cryptomarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.ActivityMainBinding
import com.selim.cryptomarket.ui.home.MainContent
import com.selim.cryptomarket.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(isDarkTheme = true) {
                MainContent(isDarkTheme = true)
            }
        }
    }
}

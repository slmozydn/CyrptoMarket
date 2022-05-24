package com.selim.cryptomarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentHomeBinding
import com.selim.cryptomarket.ui.MainActivity
import com.selim.cryptomarket.ui.toolbar.ToolbarConfig
import com.selim.cryptomarket.util.viewBinding
import com.selim.cryptomarket.util.withLoadStateAdapters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()
    private val coinAdapter = CoinAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun initUi() = with(binding) {
        coinsRecyclerView.apply {
            adapter = coinAdapter.withLoadStateAdapters(
                header = LoadingStateAdapter(coinAdapter::retry),
                footer = LoadingStateAdapter(coinAdapter::retry)
            )
        }
    }

    private fun initToolbar(isDarkMode: Boolean) {
        val toolbarBinding = (requireActivity() as MainActivity).binding.toolbarLayout
        val toolbarConfig =
            ToolbarConfig(searchVisible = true, settingsVisible = true, themeVisible = true, isDarkMode = isDarkMode)

        with(toolbarBinding) {
            render(toolbarConfig)
            onSearchViewClicked = {
                val directions = HomeFragmentDirections.actionHomeToSearch()
                findNavController().navigate(directions)
            }
            onSettingsClicked = {
                val directions = HomeFragmentDirections.actionHomeToSettings()
                findNavController().navigate(directions)
            }
            onThemeClicked = { homeViewModel.onChangeTheme() }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.coins.collectLatest {
                coinAdapter.submitData(it)
            }
        }
        homeViewModel.onChangeTheme.observe(viewLifecycleOwner, ::initToolbar)
    }
}

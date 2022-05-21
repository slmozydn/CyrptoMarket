package com.selim.cryptomarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentHomeBinding
import com.selim.cryptomarket.databinding.LayoutToolbarBinding
import com.selim.cryptomarket.ui.MainActivity
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
    private lateinit var toolbarBinding: LayoutToolbarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initUi() = with(binding) {
        coinsRecyclerView.apply {
            adapter = coinAdapter.withLoadStateAdapters(
                header = LoadingStateAdapter(coinAdapter::retry),
                footer = LoadingStateAdapter(coinAdapter::retry)
            )
        }
    }

    private fun initToolbar() {
        toolbarBinding = (requireActivity() as MainActivity).binding.toolbar
        with(toolbarBinding) {
            searchView.setQuery("", false)
            searchView.setOnQueryTextFocusChangeListener { _, _ ->
                val directions = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(directions)
            }
            settingsImageView.apply {
                isVisible = true
                setOnClickListener {
                    val directions = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                    findNavController().navigate(directions)
                }
            }
            themeImageView.isVisible = true
        }
    }

    private fun observeViewModel() = viewLifecycleOwner.lifecycleScope.launch {
        homeViewModel.coins.collectLatest {
            coinAdapter.submitData(it)
        }
    }
}

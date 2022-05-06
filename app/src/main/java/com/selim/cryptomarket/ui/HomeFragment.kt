package com.selim.cryptomarket.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentHomeBinding
import com.selim.cryptomarket.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
        coinsRecyclerView.adapter = coinAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.uiState.flowWithLifecycle(lifecycle).collect { uiState ->
                if (uiState.error != null) {
                    Toast.makeText(requireContext(), uiState.error.message, Toast.LENGTH_LONG).show()
                }
                coinAdapter.submitList(uiState.coins)
            }
        }
    }
}

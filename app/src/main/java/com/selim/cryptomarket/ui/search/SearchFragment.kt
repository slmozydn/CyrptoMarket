package com.selim.cryptomarket.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentSearchBinding
import com.selim.cryptomarket.databinding.LayoutToolbarBinding
import com.selim.cryptomarket.ui.MainActivity
import com.selim.cryptomarket.util.textChanges
import com.selim.cryptomarket.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@FlowPreview
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter()
    private lateinit var toolbarBinding: LayoutToolbarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                searchAdapter.submitList(uiState.searchResult)
            }
        }
    }

    private fun initUi() {
        toolbarBinding = (requireActivity() as MainActivity).binding.toolbar
        binding.searchRecyclerView.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 6)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (searchAdapter.getItemViewType(position)) {
                    R.layout.item_coin -> 6
                    else -> 3
                }
            }
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = searchAdapter
        }

        with(toolbarBinding) {
            searchViewModel.setQueryChanges(toolbarBinding.searchView.textChanges())
            searchView.setOnQueryTextFocusChangeListener { _, _ -> }
            themeImageView.isVisible = false
            settingsImageView.isVisible = false
        }
    }
}

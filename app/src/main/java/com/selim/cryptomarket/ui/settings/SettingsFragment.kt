package com.selim.cryptomarket.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.R.string
import com.selim.cryptomarket.databinding.FragmentSettingsBinding
import com.selim.cryptomarket.ui.MainActivity
import com.selim.cryptomarket.ui.toolbar.ToolbarConfig
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.Companion.ARG_CURRENCY
import com.selim.cryptomarket.util.restart
import com.selim.cryptomarket.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var currencyDataStore: CurrencyDataStore

    private val binding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val currencyPreference get() = runBlocking { currencyDataStore.currencyCode.first() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initUi()
        observeBackStackEntry()
        observeViewModel()
    }

    private fun initUi() = with(binding) {
        currencyLayout.apply {
            settingsValueTextView.text = currencyPreference
            root.setOnClickListener {
                val directions =
                    SettingsFragmentDirections.actionSettingsToSettingsBottomSheet(currencyArg = currencyPreference)
                findNavController().navigate(directions)
            }
        }
    }

    private fun initToolbar() {
        val toolbarBinding = (requireActivity() as MainActivity).binding.toolbarLayout
        val toolbarConfig = ToolbarConfig(titleRes = string.settings_title, backButtonVisible = true)

        with(toolbarBinding) {
            render(toolbarConfig)
            onBackButtonClicked = { findNavController().popBackStack() }
        }
    }

    private fun observeBackStackEntry() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>(ARG_CURRENCY)?.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                settingsViewModel.onCurrencySelected(data)
            }
        }
    }

    private fun observeViewModel() = viewLifecycleOwner.lifecycleScope.launch {
        settingsViewModel.onSettingsChanged.collectLatest {
            requireActivity().restart()
        }
    }
}

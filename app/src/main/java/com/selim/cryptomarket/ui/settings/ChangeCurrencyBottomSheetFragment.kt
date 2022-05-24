package com.selim.cryptomarket.ui.settings

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentSettingsBottomSheetBinding
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.EUR
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.TRY
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.USD
import com.selim.cryptomarket.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeCurrencyBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentSettingsBottomSheetBinding by viewBinding(FragmentSettingsBottomSheetBinding::bind)
    private val args: ChangeCurrencyBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_settings_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val selectedTextView = when (args.currencyArg) {
            USD.value -> binding.usdTextView
            TRY.value -> binding.tryTextView
            else -> binding.euroTextView
        }
        selectedTextView.apply {
            setTextColor(context.getColor(R.color.tertiary_color))
            setTypeface(selectedTextView.typeface, Typeface.BOLD)
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_gold_dot, 0, 0, 0);
        }

        binding.usdTextView.setOnClickListener {
            navigateBack(USD)
        }
        binding.tryTextView.setOnClickListener {
            navigateBack(TRY)
        }
        binding.euroTextView.setOnClickListener {
            navigateBack(EUR)
        }
    }

    private fun navigateBack(currencyType: CurrencyType) = with(findNavController()) {
        previousBackStackEntry?.savedStateHandle?.set(ARG_CURRENCY, currencyType.value)
        popBackStack()
    }

    enum class CurrencyType(val value: String) {
        USD("USD"),
        TRY("TRY"),
        EUR("EUR")
    }

    companion object {
        const val ARG_CURRENCY = "argCurrency"
    }
}

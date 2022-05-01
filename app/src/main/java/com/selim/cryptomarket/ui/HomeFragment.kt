package com.selim.cryptomarket.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.FragmentHomeBinding
import com.selim.cryptomarket.util.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            button.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        }
    }
}

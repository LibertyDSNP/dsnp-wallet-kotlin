package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentSeedPhraseBinding

class SeedPhraseFragment : Fragment() {

    lateinit var binding: FragmentSeedPhraseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeedPhraseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val words = listOf<String>(
            "01 Satisy",
            "02 Spike",
            "03 Lake",
            "04 Cupcake",
            "05 Bag",
            "06 Turmoil",
            "07 Sunny",
            "08 Rainbow",
            "09 Truck",
            "10 Train",
            "11 Running",
            "12 Spin"
        )
        binding.seedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = SeedPhraseAdapter(words)
        }
    }

}
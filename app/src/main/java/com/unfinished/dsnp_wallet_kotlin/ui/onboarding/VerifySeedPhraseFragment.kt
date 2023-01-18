package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentVerifySeedPhraseBinding


class VerifySeedPhraseFragment : Fragment() {

    lateinit var binding: FragmentVerifySeedPhraseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifySeedPhraseBinding.inflate(layoutInflater)
        return binding.root
    }

}
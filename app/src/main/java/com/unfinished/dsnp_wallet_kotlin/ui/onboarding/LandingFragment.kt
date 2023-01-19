package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentLandingBinding
import com.unfinished.dsnp_wallet_kotlin.util.setOnSafeClickListener
import com.unfinished.dsnp_wallet_kotlin.util.toast

class LandingFragment : Fragment() {

    lateinit var binding: FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.landingCreateDsnpId.setOnSafeClickListener {
            toast("In Progress")
        }
        binding.landingDsnpId.setOnSafeClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_lookupFragment)
        }
    }

}
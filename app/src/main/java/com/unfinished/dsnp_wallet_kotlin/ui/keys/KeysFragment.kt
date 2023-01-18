package com.unfinished.dsnp_wallet_kotlin.ui.keys

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.unfinished.dsnp_wallet_kotlin.data.model.Key
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentKeysBinding
import com.unfinished.dsnp_wallet_kotlin.ui.base.BaseFragment
import com.unfinished.dsnp_wallet_kotlin.util.hide
import com.unfinished.dsnp_wallet_kotlin.util.setOnSafeClickListener
import com.unfinished.dsnp_wallet_kotlin.util.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KeysFragment : BaseFragment<FragmentKeysBinding>(FragmentKeysBinding::inflate) {

    val TAG: String = "KeysFragment"

    override fun initialize() {
        binding.headerLayout.notification.hide()
        setUpKeysAdapter()
        val inAnim: Animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        val outAnim: Animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
        binding.contentViewFlipper.inAnimation = inAnim
        binding.contentViewFlipper.outAnimation = outAnim
        binding.keysLayout.backup.setOnSafeClickListener {
            binding.contentViewFlipper.displayedChild = 1
            binding.headerLayout.back.show()
            binding.headerLayout.logoLabel.hide()
        }
        binding.keysLayout.connectDevice.setOnSafeClickListener {
            binding.contentViewFlipper.displayedChild = 2
            binding.headerLayout.back.show()
            binding.headerLayout.logoLabel.hide()
        }
        binding.headerLayout.back.setOnSafeClickListener {
            binding.contentViewFlipper.displayedChild = 0
            binding.headerLayout.back.hide()
            binding.headerLayout.logoLabel.show()
        }
    }

    private fun setUpKeysAdapter(){
        val keys = arrayListOf<Key>().apply {
            add(Key("Ios","0x54...987498",true))
            add(Key("Ipad","0x54...987498",true))
            add(Key("Desktop","0x54...987498",false))
        }
        val adapter = KeysAdapter()
        binding.keysLayout.keysRecyclerView.adapter = adapter
        adapter.updateList(keys)
    }

    override fun observe() {

    }

}
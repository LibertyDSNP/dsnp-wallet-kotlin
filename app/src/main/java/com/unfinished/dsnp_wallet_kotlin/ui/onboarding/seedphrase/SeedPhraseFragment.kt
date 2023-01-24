package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.data.model.MnemonicWord
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentSeedPhraseBinding
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase.adapter.SeedPhraseAdapter
import com.unfinished.dsnp_wallet_kotlin.util.*

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

        binding.subTitle.apply {
            linksClickable = false
            isClickable = false
            movementMethod = LinkMovementMethod.getInstance()
            text = createSpannable(
                content = getString(R.string.seed_phrase_sub_title),
                typeface = ResourcesCompat.getFont(requireContext(),R.font.poppins_extrabold),
                highlightTextColor = ContextCompat.getColor(requireContext(),R.color.orange))  {
                clickable(getString(R.string.seed_phrase_sub_title_highlight)){}
            }
        }
        binding.seedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = SeedPhraseAdapter(orignalMnemonicWords.reArrangeWords())
        }
        binding.seedPhraseBtn.setOnSafeClickListener {
           val notifySeedPhraseFragment = NotifySeedPhraseFragment()
            notifySeedPhraseFragment.setDismissListener {
                when(it){
                    NotifySeedPhraseButton.YES -> {
                        notifySeedPhraseFragment.dismiss()
                        findNavController().navigate(R.id.action_seedPhraseFragment_to_confirmSeedPhraseFragment)
                    }
                    NotifySeedPhraseButton.CHECK_AGAIN -> {
                        notifySeedPhraseFragment.dismiss()
                    }
                }
            }
            notifySeedPhraseFragment.show(childFragmentManager,"notify_seed_phrase")
        }
    }



}
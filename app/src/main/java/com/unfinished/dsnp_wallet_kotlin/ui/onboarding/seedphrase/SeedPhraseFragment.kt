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
import com.unfinished.dsnp_wallet_kotlin.util.createSpannable
import com.unfinished.dsnp_wallet_kotlin.util.setOnSafeClickListener
import com.unfinished.dsnp_wallet_kotlin.util.showBrowser

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
        val words = listOf<MnemonicWord>(
            MnemonicWord(id = "01", content = "Satisy", indexDisplay = "",false),
            MnemonicWord(id = "02", content = "Spike", indexDisplay = "",false),
            MnemonicWord(id = "03", content = "Lake", indexDisplay = "",false),
            MnemonicWord(id = "04", content = "Cupcake", indexDisplay = "",false),
            MnemonicWord(id = "05", content = "Bag", indexDisplay = "",false),
            MnemonicWord(id = "06", content = "Turmoil", indexDisplay = "",false),
            MnemonicWord(id = "07", content = "Sunny", indexDisplay = "",false),
            MnemonicWord(id = "08", content = "Rainbow", indexDisplay = "",false),
            MnemonicWord(id = "09", content = "Truck", indexDisplay = "",false),
            MnemonicWord(id = "10", content = "Train", indexDisplay = "",false),
            MnemonicWord(id = "11", content = "Running", indexDisplay = "",false),
            MnemonicWord(id = "12", content = "Spin", indexDisplay = "",false),
        )
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
            adapter = SeedPhraseAdapter(words)
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
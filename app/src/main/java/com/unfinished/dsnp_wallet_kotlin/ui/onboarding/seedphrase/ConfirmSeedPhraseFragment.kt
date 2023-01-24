package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.data.model.MnemonicWord
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentConfirmSeedPhraseBinding
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase.adapter.DestinationSeedPhraseAdapter
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase.adapter.SourceSeedPhraseAdapter
import com.unfinished.dsnp_wallet_kotlin.util.*


class ConfirmSeedPhraseFragment : Fragment() {

    lateinit var binding: FragmentConfirmSeedPhraseBinding
    val destinationAdapter: DestinationSeedPhraseAdapter by lazy {
        DestinationSeedPhraseAdapter(
            onItemClicked = { pos, destinationWord ->
                destinationWordClicked(destinationWord)
            }
        )
    }

    val sourceAdapter: SourceSeedPhraseAdapter by lazy {
        SourceSeedPhraseAdapter(
            onItemClicked = { pos, sourceWord ->
                sourceWordClicked(sourceWord)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmSeedPhraseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmSeedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_offset))
            adapter = destinationAdapter.apply {
                updateList(destinationWords)
            }
        }
        binding.confirmSelectSeedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_offset))
            adapter = sourceAdapter.apply {
                updateList(sourceWords)
            }
        }

        binding.confirmSeedPhraseBtn.setOnSafeClickListener {
            if (validateMnemonicPhrase()){
                toast("Success! In progress other screens")
            }else{
                toast("Mnemonice phrase not matched!")
            }
        }
    }

    private fun sourceWordClicked(sourceWord: MnemonicWord) {
        val markedAsRemoved = sourceWord.copy(removed = true)

        val destinationWordsSnapshot = destinationWords
        val destinationWord = sourceWord.copy(
            indexDisplay = (destinationWordsSnapshot.size + 1).toString()
        )
        val mSourceWords = sourceWords.modified(markedAsRemoved, markedAsRemoved.byMyId())
        val mDestinationWords = destinationWordsSnapshot.addedNext(destinationWord)

        sourceWords = mSourceWords.toMutableList()
        destinationWords = mDestinationWords.sortByIndexOrder().toMutableList()

        sourceAdapter.updateList(mSourceWords)
        destinationAdapter.updateList(mDestinationWords)
    }

    private fun destinationWordClicked(destinationWord: MnemonicWord) {
        val sourceWord = sourceWords.first { it.content == destinationWord.content }
        val modifiedSourceWord = sourceWord.copy(removed = false)
        val markedAsRemoved = destinationWord.copy(removed = true, content = "")
        val mSourceWords = sourceWords.modified(modifiedSourceWord, modifiedSourceWord.byMyId())
        val mDestinationWords = destinationWords.modified(markedAsRemoved, markedAsRemoved.byMyId())

        sourceWords = mSourceWords.toMutableList()
        destinationWords = mDestinationWords.sortByIndexOrder().toMutableList()

        sourceAdapter.updateList(mSourceWords)
        destinationAdapter.updateList(mDestinationWords)
    }

}
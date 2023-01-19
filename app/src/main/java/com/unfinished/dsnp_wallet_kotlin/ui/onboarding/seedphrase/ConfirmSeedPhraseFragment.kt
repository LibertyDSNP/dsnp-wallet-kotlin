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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val orignalMnemonicWords = listOf<MnemonicWord>(
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

var destinationWords = listOf<MnemonicWord>(
    MnemonicWord(id = "01", content = "", indexDisplay = "",true),
    MnemonicWord(id = "02", content = "", indexDisplay = "",true),
    MnemonicWord(id = "03", content = "", indexDisplay = "",true),
    MnemonicWord(id = "04", content = "", indexDisplay = "",true),
    MnemonicWord(id = "05", content = "", indexDisplay = "",true),
    MnemonicWord(id = "06", content = "", indexDisplay = "",true),
    MnemonicWord(id = "07", content = "", indexDisplay = "",true),
    MnemonicWord(id = "08", content = "", indexDisplay = "",true),
    MnemonicWord(id = "09", content = "", indexDisplay = "",true),
    MnemonicWord(id = "10", content = "", indexDisplay = "",true),
    MnemonicWord(id = "11", content = "", indexDisplay = "",true),
    MnemonicWord(id = "12", content = "", indexDisplay = "",true),
).toMutableList()

var sourceWords = listOf<MnemonicWord>(
    MnemonicWord(id = "01", content = "Cupcake", indexDisplay = "", removed = false),
    MnemonicWord(id = "02", content = "Sunny", indexDisplay = "", removed = false),
    MnemonicWord(id = "03", content = "Bag", indexDisplay = "", removed = false),
    MnemonicWord(id = "04", content = "Truck", indexDisplay = "", removed = false),
    MnemonicWord(id = "05", content = "Train", indexDisplay = "", removed = false),
    MnemonicWord(id = "06", content = "Satisy", indexDisplay = "", removed = false),
    MnemonicWord(id = "07", content = "Spike", indexDisplay = "", removed = false),
    MnemonicWord(id = "08", content = "Rainbow", indexDisplay = "", removed = false),
    MnemonicWord(id = "09", content = "Turmoil", indexDisplay = "", removed = false),
    MnemonicWord(id = "10", content = "Spin", indexDisplay = "", removed = false),
    MnemonicWord(id = "11", content = "Running", indexDisplay = "", removed = false),
    MnemonicWord(id = "12", content = "Lake", indexDisplay = "", removed = false),
).toMutableList()

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
                toast("Success!")
            }else{
                toast("Mnemonice phrase not matched")
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
        destinationWords = mDestinationWords.toMutableList()

        sourceAdapter.updateList(mSourceWords)
        destinationAdapter.updateList(mDestinationWords)
    }

    private fun destinationWordClicked(destinationWord: MnemonicWord) {
        val sourceWord = sourceWords.first { it.content == destinationWord.content }
        val modifiedSourceWord = sourceWord.copy(removed = false)

        val markedAsRemoved = sourceWord.copy(removed = true, content = "")
        val mSourceWords = sourceWords.modified(modifiedSourceWord, modifiedSourceWord.byMyId())
        val mDestinationWords = destinationWords.modified(markedAsRemoved, markedAsRemoved.byMyId())

        sourceWords = mSourceWords.toMutableList()
        destinationWords = mDestinationWords.toMutableList()

        sourceAdapter.updateList(mSourceWords)
        destinationAdapter.updateList(mDestinationWords)
    }

    private fun validateMnemonicPhrase(): Boolean {
        if (orignalMnemonicWords.size != destinationWords.size)
            return false
        val orignalWords = orignalMnemonicWords.map { it.content }.joinToString(" ")
        val destinationWords = destinationWords.map { it.content }.joinToString(" ")
        if (!orignalWords.contentEquals(destinationWords))
            return false
        return true
    }

    private fun List<MnemonicWord>.addedNext(sourceWord: MnemonicWord): List<MnemonicWord> {
        val list = toMutableList()
        run breaking@{
            list.forEachIndexed { index, mnemonicWord ->
                if (mnemonicWord.content.isEmpty()) {
                    list[index] = sourceWord.copy(
                        removed = false, indexDisplay = if (index > 9)
                            "$index" else "0$index"
                    )
                    return@breaking
                }
            }
        }
        return list
    }

    private fun MnemonicWord.byMyId(): (MnemonicWord) -> Boolean = { it.id.equals(id) }

    private fun List<MnemonicWord>.fixIndices(): List<MnemonicWord> {
        return mapIndexed { index, word ->
            word.copy(indexDisplay = (index + 1).toString())
        }
    }

}
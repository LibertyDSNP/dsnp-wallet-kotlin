package com.unfinished.feature_account.presentation.mnemonic

import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord

fun Int.twoDigitIndex(): String = if (this > 9) "$this" else "0$this"

fun List<MnemonicWord>.validateMnemonicPhrase(destinationWordsList: List<MnemonicWord>): Boolean {
    if (size != destinationWordsList.size)
        return false
    val orignalWords = map { it.content }.joinToString(" ")
    val mDestinationWords = destinationWordsList.toMutableList()
    mDestinationWords.sortBy { it.id }
    val destinationWords = mDestinationWords.map { it.content }.joinToString(" ")
    if (!orignalWords.contentEquals(destinationWords))
        return false
    return true
}

fun List<MnemonicWord>.sortByIndexOrder(): List<MnemonicWord> {
    var oddIndex = 1
    var evenIndex = 7
    forEachIndexed { index, mnemonicWord ->
        if ( index == 0 || index%2 == 0){
            this[index].indexDisplay = "0$oddIndex"
            oddIndex += 1
        }else {
            this[index].indexDisplay = if (evenIndex >= 10)
                "$evenIndex" else "0$evenIndex"
            evenIndex += 1
        }
    }
    return this
}

fun List<MnemonicWord>.reArrangeWords(): List<MnemonicWord> {
    var oddIndex = 1
    var evenIndex = 7
    val words = arrayListOf<MnemonicWord>()
    forEachIndexed { index, _ ->
        if ( index == 0 || index%2 == 0){
            this[oddIndex - 1].indexDisplay = "0$oddIndex"
            words.add(this[oddIndex - 1])
            oddIndex += 1
        }else {
            this[evenIndex - 1].indexDisplay = if (evenIndex >= 10)
                "$evenIndex" else "0$evenIndex"
            words.add(this[evenIndex -1])
            evenIndex += 1
        }
    }
    return words
}

fun List<MnemonicWord>.findNextIndexForAdd(destinationWords: List<MnemonicWord>): Int {
    val firstColumn = destinationWords.filterIndexed { index, _ -> (index == 0 || index % 2 == 0) }
    val secondColumn = destinationWords.filterIndexed { index, _ -> index % 2 != 0 }
    val isFirstColmunEmpty = firstColumn.any { it.content.isEmpty() }
    var findIndex: Int = -1
    run breaking@{
        if (isFirstColmunEmpty) {
            forEachIndexed { index, _ ->
                if (index == 0 || index % 2 == 0) {
                    if (this[index].content.isEmpty()) {
                        findIndex = index
                        return@breaking
                    }
                }
            }
        }else{
            forEachIndexed { index, _ ->
                if (index % 2 != 0) {
                    if (this[index].content.isEmpty()) {
                        findIndex = index
                        return@breaking
                    }
                }
            }
        }
    }
    return findIndex
}

fun List<MnemonicWord>.addedNext(
    sourceWords: List<MnemonicWord>,
    sourceWord: MnemonicWord,
): List<MnemonicWord> {
    val list = toMutableList()

    val findNextIndexForDisplay = sourceWords.filter { it.removed }.size + 1
    val strFindNextIndexForDisplay = if (findNextIndexForDisplay > 9) "$findNextIndexForDisplay" else "0${findNextIndexForDisplay}"

    val findNextIndexForAdd = list.findNextIndexForAdd(this)
    val strFindNextIndexForAdd = if (findNextIndexForAdd > 9) "$findNextIndexForAdd" else "0${findNextIndexForAdd}"

    list[findNextIndexForAdd] = sourceWord.copy(
        id = strFindNextIndexForDisplay, removed = false, indexDisplay = strFindNextIndexForAdd
    )
    return list
}

fun MnemonicWord.byMyId(): (MnemonicWord) -> Boolean = {
    it.indexDisplay.equals(indexDisplay)
}

private fun List<MnemonicWord>.fixIndices(): List<MnemonicWord> {
    return mapIndexed { index, word ->
        word.copy(indexDisplay = (index + 1).toString())
    }
}

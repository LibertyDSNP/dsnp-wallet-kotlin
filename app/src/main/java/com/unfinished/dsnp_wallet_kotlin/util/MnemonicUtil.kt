package com.unfinished.dsnp_wallet_kotlin.util

import com.unfinished.dsnp_wallet_kotlin.data.model.MnemonicWord

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
).sortByIndexOrder().toMutableList()

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
).sortByIndexOrder().toMutableList()

fun validateMnemonicPhrase(): Boolean {
    if (orignalMnemonicWords.size != destinationWords.size)
        return false
    val orignalWords = orignalMnemonicWords.map { it.content }.joinToString(" ")
    val mDestinationWords = destinationWords
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

fun List<MnemonicWord>.findNextIndexForAdd(): Int {
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
    sourceWord: MnemonicWord,
): List<MnemonicWord> {
    val list = toMutableList()

    val findNextIndexForDisplay = sourceWords.filter { it.removed }.size + 1
    val strFindNextIndexForDisplay = if (findNextIndexForDisplay > 9) "$findNextIndexForDisplay" else "0${findNextIndexForDisplay}"

    val findNextIndexForAdd = list.findNextIndexForAdd()
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

//Test Github Action
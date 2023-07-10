package com.unfinished.common.resources

import com.unfinished.common.core.api.model.Language

class LanguagesHolder {

    companion object {
        private val RUSSIAN = Language("ru")
        private val ENGLISH = Language("en")

        private val availableLanguages = mutableListOf(RUSSIAN, ENGLISH)
    }

    fun getEnglishLang(): Language {
        return ENGLISH
    }

    fun getLanguages(): List<Language> {
        return availableLanguages
    }
}

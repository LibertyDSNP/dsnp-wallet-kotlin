package com.unfinished.data.db.converters

import androidx.room.TypeConverter
import com.unfinished.data.api.model.CryptoType

class CryptoTypeConverters {

    @TypeConverter
    fun from(cryptoType: com.unfinished.data.api.model.CryptoType?): String? = cryptoType?.name

    @TypeConverter
    fun to(name: String?): com.unfinished.data.api.model.CryptoType? = name?.let { enumValueOf<com.unfinished.data.api.model.CryptoType>(it) }
}

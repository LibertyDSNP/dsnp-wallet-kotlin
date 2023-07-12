package com.unfinished.data.db.converters

import androidx.room.TypeConverter
import com.unfinished.data.model.CryptoType

class CryptoTypeConverters {

    @TypeConverter
    fun from(cryptoType: CryptoType?): String? = cryptoType?.name

    @TypeConverter
    fun to(name: String?): CryptoType? = name?.let { enumValueOf<CryptoType>(it) }
}

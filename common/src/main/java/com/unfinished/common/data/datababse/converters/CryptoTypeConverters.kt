package com.unfinished.core.db.converters

import androidx.room.TypeConverter
import com.unfinished.common.core.api.model.CryptoType

class CryptoTypeConverters {

    @TypeConverter
    fun from(cryptoType: com.unfinished.common.core.api.model.CryptoType?): String? = cryptoType?.name

    @TypeConverter
    fun to(name: String?): com.unfinished.common.core.api.model.CryptoType? = name?.let { enumValueOf<com.unfinished.common.core.api.model.CryptoType>(it) }
}

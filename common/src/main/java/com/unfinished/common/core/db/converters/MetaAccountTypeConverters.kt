package com.unfinished.core.db.converters

import androidx.room.TypeConverter
import com.unfinished.core.db.model.chain.MetaAccountLocal

class MetaAccountTypeConverters {

    @TypeConverter
    fun fromEnum(type: MetaAccountLocal.Type): String {
        return type.name
    }

    @TypeConverter
    fun toEnum(name: String): MetaAccountLocal.Type {
        return MetaAccountLocal.Type.valueOf(name)
    }
}

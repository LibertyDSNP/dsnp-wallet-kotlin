package com.unfinished.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unfinished.data.db.converters.CryptoTypeConverters
import com.unfinished.data.db.converters.LongMathConverters
import com.unfinished.data.db.converters.MetaAccountTypeConverters
import com.unfinished.data.db.dao.AccountDao
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.dao.MetaAccountDao
import com.unfinished.data.db.dao.NodeDao
import com.unfinished.data.db.dao.StorageDao
import com.unfinished.data.db.model.AccountLocal
import com.unfinished.data.db.model.AssetLocal
import com.unfinished.data.db.model.NodeLocal
import com.unfinished.data.db.model.StorageEntryLocal
import com.unfinished.data.db.model.chain.ChainAccountLocal
import com.unfinished.data.db.model.chain.ChainAssetLocal
import com.unfinished.data.db.model.chain.ChainExplorerLocal
import com.unfinished.data.db.model.chain.ChainLocal
import com.unfinished.data.db.model.chain.ChainNodeLocal
import com.unfinished.data.db.model.chain.ChainRuntimeInfoLocal
import com.unfinished.data.db.model.chain.MetaAccountLocal

@Database(
    version = 1,
    entities = [
        AccountLocal::class,
        NodeLocal::class,
        AssetLocal::class,
        ChainLocal::class,
        ChainNodeLocal::class,
        ChainAssetLocal::class,
        ChainRuntimeInfoLocal::class,
        ChainExplorerLocal::class,
        MetaAccountLocal::class,
        ChainAccountLocal::class,
        StorageEntryLocal::class
    ],
)
@TypeConverters(
    CryptoTypeConverters::class,
    LongMathConverters::class,
    MetaAccountTypeConverters::class,
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun get(
            context: Context
        ): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun nodeDao(): NodeDao

    abstract fun userDao(): AccountDao

    abstract fun chainDao(): ChainDao

    abstract fun metaAccountDao(): MetaAccountDao

    abstract fun storageDao(): StorageDao

}

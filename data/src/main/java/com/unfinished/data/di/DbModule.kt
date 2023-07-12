package com.unfinished.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.unfinished.data.db.AppDatabase
import com.unfinished.data.db.dao.AccountDao
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.dao.MetaAccountDao
import com.unfinished.data.db.dao.NodeDao
import com.unfinished.data.db.dao.StorageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.get(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideNodeDao(appDatabase: AppDatabase): NodeDao {
        return appDatabase.nodeDao()
    }
    @Provides
    @Singleton
    fun provideChainDao(appDatabase: AppDatabase): ChainDao {
        return appDatabase.chainDao()
    }

    @Provides
    @Singleton
    fun provideMetaAccountDao(appDatabase: AppDatabase): MetaAccountDao {
        return appDatabase.metaAccountDao()
    }

    @Provides
    @Singleton
    fun provideStorageDao(appDatabase: AppDatabase): StorageDao {
        return appDatabase.storageDao()
    }

}

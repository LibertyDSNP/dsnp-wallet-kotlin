package io.novafoundation.nova.core_db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.novafoundation.nova.core_db.AppDatabase
import io.novafoundation.nova.core_db.dao.AccountDao
import io.novafoundation.nova.core_db.dao.AccountStakingDao
import io.novafoundation.nova.core_db.dao.AssetDao
import io.novafoundation.nova.core_db.dao.ChainDao
import io.novafoundation.nova.core_db.dao.ContributionDao
import io.novafoundation.nova.core_db.dao.CurrencyDao
import io.novafoundation.nova.core_db.dao.DappAuthorizationDao
import io.novafoundation.nova.core_db.dao.FavouriteDAppsDao
import io.novafoundation.nova.core_db.dao.GovernanceDAppsDao
import io.novafoundation.nova.core_db.dao.LockDao
import io.novafoundation.nova.core_db.dao.MetaAccountDao
import io.novafoundation.nova.core_db.dao.NftDao
import io.novafoundation.nova.core_db.dao.NodeDao
import io.novafoundation.nova.core_db.dao.OperationDao
import io.novafoundation.nova.core_db.dao.PhishingAddressDao
import io.novafoundation.nova.core_db.dao.PhishingSitesDao
import io.novafoundation.nova.core_db.dao.StakingTotalRewardDao
import io.novafoundation.nova.core_db.dao.StorageDao
import io.novafoundation.nova.core_db.dao.TokenDao
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
    fun provideAssetDao(appDatabase: AppDatabase): AssetDao {
        return appDatabase.assetDao()
    }

    @Provides
    @Singleton
    fun provideLockDao(appDatabase: AppDatabase): LockDao {
        return appDatabase.lockDao()
    }

    @Provides
    @Singleton
    fun provideContributionDao(appDatabase: AppDatabase): ContributionDao {
        return appDatabase.contributionDao()
    }

    @Provides
    @Singleton
    fun provideOperationHistoryDao(appDatabase: AppDatabase): OperationDao {
        return appDatabase.operationDao()
    }

    @Provides
    @Singleton
    fun providePhishingAddressDao(appDatabase: AppDatabase): PhishingAddressDao {
        return appDatabase.phishingAddressesDao()
    }

    @Provides
    @Singleton
    fun provideStorageDao(appDatabase: AppDatabase): StorageDao {
        return appDatabase.storageDao()
    }

    @Provides
    @Singleton
    fun provideTokenDao(appDatabase: AppDatabase): TokenDao {
        return appDatabase.tokenDao()
    }

    @Provides
    @Singleton
    fun provideAccountStakingDao(appDatabase: AppDatabase): AccountStakingDao {
        return appDatabase.accountStakingDao()
    }

    @Provides
    @Singleton
    fun provideStakingTotalRewardDao(appDatabase: AppDatabase): StakingTotalRewardDao {
        return appDatabase.stakingTotalRewardDao()
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
    fun provideDappAuthorizationDao(appDatabase: AppDatabase): DappAuthorizationDao {
        return appDatabase.dAppAuthorizationDao()
    }

    @Provides
    @Singleton
    fun provideNftDao(appDatabase: AppDatabase): NftDao {
        return appDatabase.nftDao()
    }

    @Provides
    @Singleton
    fun providePhishingSitesDao(appDatabase: AppDatabase): PhishingSitesDao {
        return appDatabase.phishingSitesDao()
    }

    @Provides
    @Singleton
    fun provideFavouriteDappsDao(appDatabase: AppDatabase): FavouriteDAppsDao {
        return appDatabase.favouriteDAppsDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    @Singleton
    fun provideGovernanceDAppDao(appDatabase: AppDatabase): GovernanceDAppsDao {
        return appDatabase.governanceDAppsDao()
    }
}

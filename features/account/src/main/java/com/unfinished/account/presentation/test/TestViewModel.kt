package com.unfinished.account.presentation.test

import com.unfinished.account.domain.account.add.AddAccountInteractor
import com.unfinished.account.domain.advancedEncryption.AdvancedEncryption
import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.account.domain.model.AddAccountType
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import com.unfinished.data.model.account.MetaAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.usecase.ChainUseCase
import com.unfinished.data.usecase.ConnectionUseCase
import com.unfinished.data.usecase.extrinsic.BalanceUseCase
import com.unfinished.data.usecase.extrinsic.MsaUseCase
import com.unfinished.data.usecase.rpc.AccountUseCase
import com.unfinished.data.usecase.rpc.BlockUseCase
import com.unfinished.data.usecase.rpc.FeeUseCase
import com.unfinished.data.usecase.rpc.RuntimeUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val addAccountInteractor: AddAccountInteractor,
    private val connectionUseCase: ConnectionUseCase,
    private val runtimeUseCase: RuntimeUseCase,
    private val blockUseCase: BlockUseCase,
    private val feeUseCase: FeeUseCase,
    private val accountUseCase: AccountUseCase,
    private val balanceUseCase: BalanceUseCase,
    private val msaUseCase: MsaUseCase,
    private val chainUseCase: ChainUseCase
) : BaseViewModel() {
    fun setUpNewConnection(chainUrl: String) = connectionUseCase.setUpNewConnection(chainUrl)

    suspend fun getChains() = withContext(Dispatchers.IO){
        chainUseCase.getChain()
    }

    fun getChain() = runBlocking {
        chainUseCase.getChain().firstOrNull()
    }

    suspend fun getRuntimeMetaData(chain: Chain) = withContext(Dispatchers.IO) {
        runtimeUseCase.getRuntimeMetaData(chain = chain)
    }

    suspend fun getRuntimeVersion(chain: Chain) = withContext(Dispatchers.IO) {
        runtimeUseCase.getRuntimeVersion(chain = chain)
    }

    suspend fun getBlock(chain: Chain, hash: String? = null) = withContext(Dispatchers.IO) {
        blockUseCase.getBlockEvents(chain = chain, blockHash = hash)
    }

    suspend fun getGenesisHash(chain: Chain): String = withContext(Dispatchers.IO) {
        blockUseCase.getGenesisHash(chain = chain)
    }

    suspend fun getBlockEvents(chain: Chain, blockHash: String? = null) = withContext(Dispatchers.IO) {
        blockUseCase.getBlockEvents(chain = chain, blockHash = blockHash)
    }
    suspend fun paymentQueryInfo(chain: Chain, extrinsic: String) = withContext(Dispatchers.IO){
        feeUseCase.getExtrinsicFee(chain = chain, extrinsic = extrinsic)
    }

    suspend fun createAccount(mnemonic: String, advancedEncryption: AdvancedEncryption, addAccountType: AddAccountType) = withContext(Dispatchers.IO) {
        addAccountInteractor.createAccount(
            mnemonic = mnemonic,
            advancedEncryption = advancedEncryption,
            addAccountType = addAccountType
        )
    }

    fun getMetaAccounts() = runBlocking {
        accountRepository.allMetaAccounts()
    }
    suspend fun checkAccountDetails(chain: Chain, metaAccount: MetaAccount) = withContext(Dispatchers.IO) {
        accountUseCase.getAccountInfo(chain = chain, metaAccount)
    }

    suspend fun testTransfer(
        chain: Chain,
        metaAccount: MetaAccount,
        destMetaAccount: MetaAccount,
        amount: Float
    ) = withContext(Dispatchers.IO) {
        balanceUseCase.transfer(chain = chain, metaAccount = metaAccount, destMetaAccount = destMetaAccount, amount = amount)
    }
    suspend fun createMsa(chain: Chain, metaAccount: MetaAccount) = withContext(Dispatchers.IO) {
        msaUseCase.createMsa(chain = chain, metaAccount = metaAccount)
    }

    suspend fun addKeyToMsa(
        chain: Chain,
        msaId: BigInteger,
        expiration: BigInteger,
        msaOwnerMetaAccount: MetaAccount,
        newKeyOwnerMetaAccount: MetaAccount
    ) = withContext(Dispatchers.IO) {
        msaUseCase.addKeyToMsa(
            chain = chain,
            msaId = msaId,
            expiration = expiration,
            msaOwnerMetaAccount = msaOwnerMetaAccount,
            newKeyOwnerMetaAccount = newKeyOwnerMetaAccount
        )
    }
    suspend fun getPublicKeyToMsaId(chain: Chain, metaAccount: MetaAccount) = withContext(Dispatchers.IO) {
        msaUseCase.getMsaId(chain = chain, metaAccount = metaAccount)
    }

    suspend fun deletePublicKeyToMsaId(
        chain: Chain,
        metaAccount: MetaAccount,
        newMetaAccount: MetaAccount
    ) = withContext(Dispatchers.IO) {
        msaUseCase.deletePublicKeyToMsaId(chain = chain, metaAccount = metaAccount, newMetaAccount = newMetaAccount)
    }

    suspend fun retireMsa(
        chain: Chain,
        metaAccount: MetaAccount
    ) = withContext(Dispatchers.IO) {
        msaUseCase.retireMsa(chain = chain, metaAccount = metaAccount)
    }

    suspend fun createProvider(
        chain: Chain,
        metaAccount: MetaAccount,
        name: String
    ) = withContext(Dispatchers.IO) {
        msaUseCase.createProvider(chain = chain, metaAccount = metaAccount, name = name)
    }
}

package com.unfinished.account.domain.validation

import com.unfinished.common.validation.Validation
import com.unfinished.common.validation.ValidationStatus
import com.unfinished.common.validation.ValidationSystemBuilder
import com.unfinished.common.validation.validationError
import com.unfinished.account.domain.model.LightMetaAccount.Type.LEDGER
import com.unfinished.account.domain.model.LightMetaAccount.Type.PARITY_SIGNER
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.domain.model.hasAccountIn
import com.unfinished.account.domain.validation.NoChainAccountFoundError.AddAccountState
import com.unfinished.runtime.multiNetwork.chain.model.Chain

interface NoChainAccountFoundError {
    val chain: Chain
    val account: MetaAccount
    val addAccountState: AddAccountState

    enum class AddAccountState {
        CAN_ADD, LEDGER_NOT_SUPPORTED, PARITY_SIGNER_NOT_SUPPORTED
    }
}

class HasChainAccountValidation<P, E>(
    private val chainExtractor: (P) -> Chain,
    private val metaAccountExtractor: (P) -> MetaAccount,
    private val errorProducer: (chain: Chain, account: MetaAccount, addAccountState: AddAccountState) -> E
) : Validation<P, E> {

    override suspend fun validate(value: P): ValidationStatus<E> {
        val account = metaAccountExtractor(value)
        val chain = chainExtractor(value)

        return when {
            account.hasAccountIn(chain) -> ValidationStatus.Valid()
            account.type == LEDGER && !SubstrateApplicationConfig.supports(chain.id) -> {
                errorProducer(chain, account, AddAccountState.LEDGER_NOT_SUPPORTED).validationError()
            }
            account.type == PARITY_SIGNER && chain.isEthereumBased -> {
                errorProducer(chain, account, AddAccountState.PARITY_SIGNER_NOT_SUPPORTED).validationError()
            }
            else -> errorProducer(chain, account, AddAccountState.CAN_ADD).validationError()
        }
    }
}

fun <P, E> ValidationSystemBuilder<P, E>.hasChainAccount(
    chain: (P) -> Chain,
    metaAccount: (P) -> MetaAccount,
    error: (chain: Chain, account: MetaAccount, addAccountState: AddAccountState) -> E
) {
    validate(HasChainAccountValidation(chain, metaAccount, error))
}


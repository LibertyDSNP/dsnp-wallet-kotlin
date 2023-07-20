package com.unfinished.data.usecase.extrinsic

import com.unfinished.data.R
import com.unfinished.data.model.event.EventType
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.extrinsic.calls.addPublicKeyToMsa
import com.unfinished.data.multiNetwork.extrinsic.calls.createMsa
import com.unfinished.data.multiNetwork.extrinsic.calls.createProvider
import com.unfinished.data.multiNetwork.extrinsic.calls.deletePublicKeyToMsa
import com.unfinished.data.multiNetwork.extrinsic.calls.retireMsa
import com.unfinished.data.multiNetwork.extrinsic.model.FeeResponse
import com.unfinished.data.multiNetwork.extrinsic.model.MsaResponse
import com.unfinished.data.multiNetwork.extrinsic.model.error.castToException
import com.unfinished.data.multiNetwork.extrinsic.service.ExtrinsicService
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.repository.event.EventsRepository
import com.unfinished.data.util.resource.ResourceManager
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toHexUntyped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MsaUseCase @Inject constructor(
    private val chainRegistry: ChainRegistry,
    private val realExtrinsicService: ExtrinsicService,
    private val eventsRepository: EventsRepository,
    private val resourceManager: ResourceManager
) {
    suspend fun createMsa(
        chain: Chain,
        metaAccount: MetaAccount
    ) = callbackFlow<Result<MsaResponse>> {
        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = metaAccount,
            chain = chain,
            extrinsicCall = { createMsa() },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.create_msa_error))))
            },
            result = { _, events ->
                val msaEvent = events.map { it as? EventType.MsaEvent }.filterNotNull()
                trySend(Result.success(MsaResponse(msaEvent.firstOrNull())))
            }
        )
        awaitClose()
    }

    suspend fun getMsaId(chain: Chain, metaAccount: MetaAccount) = callbackFlow {
        val runtime = chainRegistry.getRuntime(chain.id)
        val events = eventsRepository.getMsaIdFromFrequencyChain(
            chain,
            runtime,
            metaAccount.substrateAccountId!!
        )
        trySend(events)
        awaitClose()
    }
    suspend fun addKeyToMsa(
        chain: Chain,
        msaId: BigInteger,
        expiration: BigInteger,
        msaOwnerMetaAccount: MetaAccount,
        newKeyOwnerMetaAccount: MetaAccount,
    ) = callbackFlow<Result<Nothing>> {
        val msaOwnerProof = generateAddMsaKeyPayload(
            msaId = msaId,
            expiration = expiration,
            accountId = msaOwnerMetaAccount.substrateAccountId!!,
            chain = chain
        ).fromHex().run {
            realExtrinsicService.generateSignatureProof(this, msaOwnerMetaAccount)
        }
        val newKeyOwnerProof = generateAddMsaKeyPayload(
            msaId = msaId,
            expiration = expiration,
            accountId = newKeyOwnerMetaAccount.substrateAccountId!!,
            chain = chain
        ).fromHex().run {
            realExtrinsicService.generateSignatureProof(this, newKeyOwnerMetaAccount)
        }

        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = msaOwnerMetaAccount,
            chain = chain,
            extrinsicCall = {
                addPublicKeyToMsa(
                    msaOwnerPublicKey = msaOwnerMetaAccount.substrateAccountId,
                    msaOwnerProof = msaOwnerProof,
                    newKeyOwnerProof = newKeyOwnerProof,
                    addKeyPayload = Struct.Instance(
                        mapOf(
                            "msaId" to msaId,
                            "expiration" to expiration,
                            "newPublicKey" to newKeyOwnerMetaAccount.substrateAccountId
                        )
                    )
                )
            },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.add_public_key_to_msa_error))))
            },
            result = { _, events ->
                val msaEvent = events.map { it as? EventType.MsaEvent }.filterNotNull()
            }
        )
        awaitClose()
    }

    private suspend fun generateAddMsaKeyPayload(
        msaId: BigInteger,
        expiration: BigInteger,
        accountId: AccountId,
        chain: Chain
    ): String {
        val runtime = chainRegistry.getRuntime(chain.id)
        val addKeyData = runtime.typeRegistry.get("pallet_msa.types.AddKeyData")
        val prefix = "0x3c42797465733e" // <Bytes> -> Hex string
        val postfix = "3c2f42797465733e" // </Bytes> -> Hex string
        val payload = addKeyData!!.toHexUntyped(
            runtime, Struct.Instance(
                mapOf(
                    "msaId" to msaId,
                    "expiration" to expiration,
                    "newPublicKey" to accountId
                )
            )
        ).substring(2) //Exclude 0x
        return prefix + payload + postfix
    }

    suspend fun deletePublicKeyToMsaId(
        chain: Chain,
        metaAccount: MetaAccount,
        newMetaAccount: MetaAccount
    ) = callbackFlow<Result<Nothing>> {
        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = metaAccount,
            chain = chain,
            extrinsicCall = { deletePublicKeyToMsa(publicKeyToDelete = newMetaAccount.substrateAccountId) },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.delete_public_key_to_msa_error))))
            },
            result = { _, _ ->

            }
        )
        awaitClose()
    }

    suspend fun retireMsa(
        chain: Chain,
        metaAccount: MetaAccount
    ) = callbackFlow<Result<Nothing>> {
        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = metaAccount,
            chain = chain,
            extrinsicCall = { retireMsa() },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.retire_msa_error))))
            },
            result = { _, _ -> }
        )
        awaitClose()
    }
    suspend fun createProvider(
        chain: Chain,
        metaAccount: MetaAccount,
        name: String
    )  = callbackFlow<Result<Nothing>> {
        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = metaAccount,
            chain = chain,
            extrinsicCall = { createProvider(name.toByteArray()) },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.create_provider_error))))
            },
            result = { _, _ -> }
        )
        awaitClose()
    }

}
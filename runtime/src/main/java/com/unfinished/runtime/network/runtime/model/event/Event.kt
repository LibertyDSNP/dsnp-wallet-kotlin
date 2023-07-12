package com.unfinished.runtime.network.runtime.model.event

import java.math.BigInteger


enum class EventTypes(name: String) {
    System("System"),
    Balance("Balance"),
    Msa("Msa"),
    TransactionPayment("TransactionPayment"),
}

data class Event(
    val name: String,
    val value: System
)
//Event -> System ->

sealed class EventType {

    data class System(
        val name: String,
        val value: Extrinsic
    ): EventType()

    data class Balance(
        val name: String,
        val value: Withdraw
    ): EventType()

    data class MsaEvent(
        val name: String,
        val value: MsaCreated
    ): EventType()

    data class TransactionPayment(
        val name: String,
        val value: TransactionFee
    ): EventType()

    object Incompaitable: EventType()
}

//Transaction
data class TransactionFee(
    val name: String?,
    val value: TransactionFeeValue?
)

data class TransactionFeeValue(
    val who: ByteArray?,
    val actual_fee: BigInteger?,
    val tip: BigInteger?
)
//Msa
data class MsaCreated(
    val name: String?,
    val value: MsaValue?
)

data class MsaValue(
    val msa_id: BigInteger?,
    val key: ByteArray?
)

//Balance
data class Withdraw(
    val name: String?,
    val value: WithdrawValue?
)

data class WithdrawValue(
    val who: ByteArray?,
    val amount: BigInteger?
)

//System
data class Extrinsic(
    val name: String?,
    val value: DisptachInfo?,
    val error: DisptachError?
)

data class DisptachError(
    val moduleIndex: BigInteger?,
    val error: Pair<String, jp.co.soramitsu.fearless_utils.runtime.metadata.module.Error>?
)

data class DisptachInfo(
    val weight: DispatchInfoWeight?,
    val classs: DispatchInfoClass?,
    val paysFee: DispatchInfoPaysFee?
)

data class DispatchInfoWeight(
    val refTime: BigInteger?,
    val proofSize: BigInteger?
)

data class DispatchInfoClass(
    val name: String?,
    val value: Any?,
)

data class DispatchInfoPaysFee(
    val name: String?,
    val value: Any?
)

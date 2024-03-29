package com.unfinished.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.unfinished.data.db.model.chain.ChainAccountLocal
import com.unfinished.data.db.model.chain.MetaAccountLocal
import com.unfinished.data.db.model.chain.MetaAccountPositionUpdate
import com.unfinished.data.db.model.chain.RelationJoinedMetaAccountInfo
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Fetch meta account where
 * accountId = meta.substrateAccountId
 * or hex(accountId) = meta.ethereumAddress
 * or there is a child chain account which have child.accountId = accountId
 */
@Language("RoomSql")
private const val FIND_BY_ADDRESS_WHERE_CLAUSE = """
        WHERE substrateAccountId = :accountId
        OR ethereumAddress = :accountId
        OR  id = (
            SELECT id FROM meta_accounts AS m
                INNER JOIN chain_accounts as c ON m.id = c.metaId
                WHERE  c.accountId = :accountId
            )
        ORDER BY (CASE WHEN isSelected THEN 0 ELSE 1 END)
    """

@Language("RoomSql")
private const val FIND_ACCOUNT_BY_ADDRESS_QUERY = """
            SELECT * FROM meta_accounts 
            $FIND_BY_ADDRESS_WHERE_CLAUSE
"""

@Language("RoomSql")
private const val FIND_NAME_BY_ADDRESS_QUERY = """
            SELECT name FROM meta_accounts 
            $FIND_BY_ADDRESS_WHERE_CLAUSE
"""
@Dao
interface MetaAccountDao {

    @Insert
    suspend fun insertMetaAccount(metaAccount: MetaAccountLocal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChainAccount(chainAccount: ChainAccountLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChainAccounts(chainAccounts: List<ChainAccountLocal>)

    @Query("SELECT * FROM meta_accounts")
    fun getMetaAccounts(): List<MetaAccountLocal>

    @Query("SELECT * FROM meta_accounts")
    @Transaction
    suspend fun getJoinedMetaAccountsInfo(): List<RelationJoinedMetaAccountInfo>

    @Query("SELECT * FROM meta_accounts")
    fun getJoinedMetaAccountsInfoFlow(): Flow<List<RelationJoinedMetaAccountInfo>>

    @Query("UPDATE meta_accounts SET isSelected = (id = :metaId)")
    suspend fun selectMetaAccount(metaId: Long)

    @Update(entity = MetaAccountLocal::class)
    suspend fun updatePositions(updates: List<MetaAccountPositionUpdate>)

    @Query("SELECT * FROM meta_accounts WHERE id = :metaId")
    @Transaction
    suspend fun getJoinedMetaAccountInfo(metaId: Long): RelationJoinedMetaAccountInfo

    @Query("SELECT * FROM meta_accounts WHERE isSelected = 1")
    @Transaction
    fun selectedMetaAccountInfoFlow(): Flow<RelationJoinedMetaAccountInfo?>

    @Query("SELECT EXISTS ($FIND_ACCOUNT_BY_ADDRESS_QUERY)")
    fun isMetaAccountExists(accountId: AccountId): Boolean

    @Query(FIND_ACCOUNT_BY_ADDRESS_QUERY)
    @Transaction
    fun getMetaAccountInfo(accountId: AccountId): RelationJoinedMetaAccountInfo?

    @Query(FIND_NAME_BY_ADDRESS_QUERY)
    fun metaAccountNameFor(accountId: AccountId): String?

    @Query("UPDATE meta_accounts SET name = :newName WHERE id = :metaId")
    suspend fun updateName(metaId: Long, newName: String)

    @Query("DELETE FROM meta_accounts WHERE id = :metaId")
    suspend fun delete(metaId: Long)

    @Query("SELECT COALESCE(MAX(position), 0)  + 1 FROM meta_accounts")
    suspend fun nextAccountPosition(): Int
}

class MetaAccountWithBalanceLocal(
    val id: Long,
    val freeInPlanks: BigInteger,
    val reservedInPlanks: BigInteger,
    val offChainBalance: BigInteger?,
    val precision: Int,
    val rate: BigDecimal?
)

package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAccount(account: UserAccount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<UserAccount>)

    @Query("SELECT * FROM user_accounts WHERE id = :id LIMIT 1")
    suspend fun getAccountById(id: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE LOWER(email) = LOWER(:email) AND email != '' LIMIT 1")
    suspend fun getAccountByEmail(email: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE normalizedPhone = :normalizedPhone OR phone = :phone LIMIT 1")
    suspend fun getAccountByPhone(normalizedPhone: String, phone: String): UserAccount?

    @Query("SELECT * FROM user_accounts")
    suspend fun getAllAccounts(): List<UserAccount>

    @Query("SELECT * FROM user_accounts")
    fun observeAllAccounts(): Flow<List<UserAccount>>

    @Query("DELETE FROM user_accounts WHERE id = :id")
    suspend fun deleteAccountById(id: String)
}

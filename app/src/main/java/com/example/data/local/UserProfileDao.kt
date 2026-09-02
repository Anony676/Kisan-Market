package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    fun getUserProfile(id: String = "usr_001"): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getUserProfileDirect(id: String = "usr_001"): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getProfileCount(): Int

    @Query("DELETE FROM user_profiles WHERE id = :id")
    suspend fun deleteProfile(id: String)
}

package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<AppNotification>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotifications(notifications: List<AppNotification>)

    @Update
    suspend fun updateNotification(notification: AppNotification)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    @Delete
    suspend fun deleteNotification(notification: AppNotification)

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationsCount(): Int
}

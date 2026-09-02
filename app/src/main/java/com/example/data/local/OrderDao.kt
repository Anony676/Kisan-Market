package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY timestamp DESC")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: String): Order?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrders(orders: List<Order>)

    @Update
    suspend fun updateOrder(order: Order)

    @Query("UPDATE orders SET status = :newStatus WHERE id = :id")
    suspend fun updateOrderStatus(id: String, newStatus: String)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM orders WHERE id LIKE 'KM-849%'")
    suspend fun deleteDefaultOrders()

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrdersCount(): Int
}

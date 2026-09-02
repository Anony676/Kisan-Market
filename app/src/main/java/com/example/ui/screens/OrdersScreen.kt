package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.screens.components.OrderCard
import com.example.ui.screens.components.PurchaseItemCard
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber

enum class OrderPageTab {
    MY_PURCHASES,
    ORDER_INBOX
}

@Composable
fun OrdersScreen(
    orders: List<Order>,
    activeStatusFilter: String,
    onSelectStatusFilter: (String) -> Unit,
    onOrderClick: (Order) -> Unit,
    onAdvanceStatus: (orderId: String, newStatus: String) -> Unit,
    onNavigateToMarket: () -> Unit = {},
    onNavigateToSell: () -> Unit = {},
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    var selectedOrderTab by remember { mutableStateOf(OrderPageTab.MY_PURCHASES) }

    // Separate orders into Purchases vs Inbox without falling back to all orders
    val purchaseOrders = remember(orders) {
        orders.filter {
            it.buyerName.contains("You", ignoreCase = true) ||
            it.buyerName.contains("Buyer", ignoreCase = true) ||
            it.buyerName.contains("Retail", ignoreCase = true)
        }
    }

    val inboxOrders = remember(orders) {
        orders.filter {
            !it.buyerName.contains("You", ignoreCase = true) &&
            !it.buyerName.contains("Buyer", ignoreCase = true) &&
            !it.buyerName.contains("Retail", ignoreCase = true)
        }
    }

    val currentTabOrders = if (selectedOrderTab == OrderPageTab.MY_PURCHASES) {
        purchaseOrders
    } else {
        inboxOrders
    }

    val filteredTabOrders = remember(currentTabOrders, activeStatusFilter) {
        when (activeStatusFilter) {
            "All" -> currentTabOrders
            "Active" -> currentTabOrders.filter { it.status == "Placed" || it.status == "Confirmed" || it.status == "Packed" }
            "In Transit" -> currentTabOrders.filter { it.status == "In Transit" }
            "Delivered" -> currentTabOrders.filter { it.status == "Delivered" }
            else -> currentTabOrders
        }
    }

    val orderStatusTabs = listOf(
        "All" to "📦 ${strings.orderStatusAll}",
        "Active" to "⏳ ${strings.orderStatusPlaced}",
        "In Transit" to "🚚 ${strings.orderStatusInTransit}",
        "Delivered" to "✅ ${strings.orderStatusDelivered}"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 0.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Scrollable Top Header (App Identity, Location, Language, Notifications)
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Dual Segmented Tabs: 'My Purchases' and 'Order Inbox'
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 'My Purchases' Tab
                    val isPurchasesSelected = selectedOrderTab == OrderPageTab.MY_PURCHASES
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isPurchasesSelected) AgroGreenPrimary else Color.Transparent
                            )
                            .clickable { selectedOrderTab = OrderPageTab.MY_PURCHASES }
                            .testTag("tab_my_purchases")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = if (isPurchasesSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "My Purchases",
                                fontSize = 13.sp,
                                fontWeight = if (isPurchasesSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isPurchasesSelected) Color.White else Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (isPurchasesSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFE2E8F0)
                            ) {
                                Text(
                                    text = "${purchaseOrders.size}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPurchasesSelected) Color.White else Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // 'Order Inbox' Tab
                    val isInboxSelected = selectedOrderTab == OrderPageTab.ORDER_INBOX
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isInboxSelected) AgroGreenPrimary else Color.Transparent
                            )
                            .clickable { selectedOrderTab = OrderPageTab.ORDER_INBOX }
                            .testTag("tab_order_inbox")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = null,
                                tint = if (isInboxSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Order Inbox",
                                fontSize = 13.sp,
                                fontWeight = if (isInboxSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isInboxSelected) Color.White else Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (isInboxSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFE2E8F0)
                            ) {
                                Text(
                                    text = "${inboxOrders.size}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isInboxSelected) Color.White else Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Summary Header Card for Active Tab
        item {
            AnimatedContent(
                targetState = selectedOrderTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "orders_header_transition"
            ) { targetTab ->
                if (targetTab == OrderPageTab.ORDER_INBOX) {
                    // Farm orders received from buyers
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AgroGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "📥 Order Inbox (Farm Orders)",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Incoming wholesale & trade orders from buyers",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.18f)
                                ) {
                                    Text(
                                        text = "${inboxOrders.size} Orders",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            val totalTurnover = inboxOrders.sumOf { it.totalAmount }
                            val activeCount = inboxOrders.count { it.status != "Delivered" && it.status != "Cancelled" }
                            val deliveredCount = inboxOrders.count { it.status == "Delivered" }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OrderStatBox(
                                    title = strings.orderStatusAll,
                                    value = "${inboxOrders.size}",
                                    subtitle = "₹${totalTurnover.toInt()}",
                                    modifier = Modifier.weight(1f)
                                )
                                OrderStatBox(
                                    title = strings.orderStatusPlaced,
                                    value = "$activeCount",
                                    subtitle = "In Pipeline",
                                    modifier = Modifier.weight(1f)
                                )
                                OrderStatBox(
                                    title = strings.orderStatusDelivered,
                                    value = "$deliveredCount",
                                    subtitle = "100% Settled",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                } else {
                    // 'My Purchases' Header Card
                    val purchases = purchaseOrders
                    val totalSpend = purchases.sumOf { it.totalAmount }
                    val inTransitCount = purchases.count { it.status == "In Transit" || it.status == "Confirmed" || it.status == "Placed" }
                    val deliveredCount = purchases.count { it.status == "Delivered" }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🛍️ My Purchased Crops",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Direct farm produce orders & live shipment tracking",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.18f)
                                ) {
                                    Text(
                                        text = "${purchases.size} Purchases",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OrderStatBox(
                                    title = "Total Spent",
                                    value = "₹${totalSpend.toInt()}",
                                    subtitle = "${purchases.size} Lots Bought",
                                    modifier = Modifier.weight(1f)
                                )
                                OrderStatBox(
                                    title = "On The Way",
                                    value = "$inTransitCount",
                                    subtitle = "Live Tracking",
                                    modifier = Modifier.weight(1f)
                                )
                                OrderStatBox(
                                    title = "Delivered",
                                    value = "$deliveredCount",
                                    subtitle = "Received & Verified",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Status Tabs Row
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                items(orderStatusTabs) { (tabKey, tabLabel) ->
                    val isSelected = activeStatusFilter == tabKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectStatusFilter(tabKey) },
                        label = {
                            Text(
                                text = tabLabel,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (selectedOrderTab == OrderPageTab.MY_PURCHASES) Color(0xFF1E3A8A) else AgroGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("order_filter_tab_$tabKey")
                    )
                }
            }
        }

        // Orders / Purchases List
        if (filteredTabOrders.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("empty_orders_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (selectedOrderTab == OrderPageTab.MY_PURCHASES) "🛍️" else "📦",
                            fontSize = 44.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (selectedOrderTab == OrderPageTab.MY_PURCHASES) {
                                "No Crop Purchases Found"
                            } else {
                                strings.noOrdersFound
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedOrderTab == OrderPageTab.MY_PURCHASES) {
                                "You have not purchased any crop lots yet. Browse the live marketplace to buy directly from verified farmers."
                            } else {
                                "No incoming trade or wholesale orders received yet. List crops to sell or share your farm profile."
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        if (selectedOrderTab == OrderPageTab.MY_PURCHASES) {
                            Button(
                                onClick = onNavigateToMarket,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1E3A8A),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.testTag("browse_market_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Explore Market",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Button(
                                onClick = onNavigateToSell,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AgroGreenPrimary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.testTag("orders_sell_crop_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = strings.tabSellProduce,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (selectedOrderTab == OrderPageTab.MY_PURCHASES) {
                // Render 'My Purchases' with PurchaseItemCard
                items(filteredTabOrders, key = { "purchase_${it.id}" }) { order ->
                    PurchaseItemCard(
                        order = order,
                        onClick = { onOrderClick(order) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                // Render 'Order Inbox' with original OrderCard layout
                items(filteredTabOrders, key = { "inbox_${it.id}" }) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOrderClick(order) },
                        onAdvanceStatus = { nextStatus -> onAdvanceStatus(order.id, nextStatus) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStatBox(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0x22FFFFFF),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = HarvestAmber, maxLines = 1)
        }
    }
}

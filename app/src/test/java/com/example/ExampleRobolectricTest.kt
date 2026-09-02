package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.KisanMarketDatabase
import com.example.data.model.CropListing
import com.example.data.model.Order
import com.example.data.repository.KisanMarketRepository
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: KisanMarketDatabase
    private lateinit var repository: KisanMarketRepository

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, KisanMarketDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = KisanMarketRepository(
            cropListingDao = database.cropListingDao(),
            marketPriceDao = database.marketPriceDao(),
            orderDao = database.orderDao(),
            farmerProfileDao = database.farmerProfileDao(),
            notificationDao = database.notificationDao(),
            userProfileDao = database.userProfileDao()
        )
        repository.seedDatabaseIfEmpty()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun read_app_name_from_context() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Kisan Market", appName)
    }

    @Test
    fun database_seed_and_retrieve_listings() = runBlocking {
        val testListing = CropListing(
            id = "TEST-LISTING-01",
            title = "Fresh Organic Tomatoes",
            category = "Vegetables",
            variety = "Abhinav",
            farmerName = "Your Name",
            farmerId = "farmer_test",
            farmerPhone = "+91 00000 00000",
            farmLocation = "Your Location",
            mandiRegion = "Pune Mandi",
            pricePerUnit = 30.0,
            unit = "kg",
            quantityAvailable = 50.0,
            minOrderQty = 5.0,
            qualityGrade = "Grade A",
            isOrganic = true,
            isVerifiedFarmer = true,
            harvestDate = "Today",
            description = "Test description",
            mandiBenchmarkPrice = 28.0,
            emojiIcon = "🍅"
        )
        repository.insertListing(testListing)
        val listings = repository.allListings.first()
        assertTrue("Listings should contain inserted test listing", listings.isNotEmpty())

        val found = listings.find { it.id == "TEST-LISTING-01" }
        assertNotNull(found)
        assertEquals("Fresh Organic Tomatoes", found?.title)
        assertTrue((found?.pricePerUnit ?: 0.0) > 0)
    }

    @Test
    fun database_seed_and_retrieve_mandi_prices() = runBlocking {
        val prices = repository.allPrices.first()
        assertTrue("Mandi prices should be populated", prices.isNotEmpty())
    }

    @Test
    fun place_order_and_update_status() = runBlocking {
        val testOrder = Order(
            id = "ORD-TEST-101",
            listingId = "LIST-1",
            cropName = "Sharbati Wheat",
            cropCategory = "Grains",
            variety = "Sharbati MP",
            farmerName = "Ramesh Patil",
            farmerPhone = "+91 98220 11223",
            buyerName = "Test Buyer",
            buyerPhone = "+91 99999 88888",
            deliveryAddress = "Wholesale Market, Sector 18",
            mandiRegion = "Nashik APMC",
            quantity = 50.0,
            unit = "kg",
            unitPrice = 36.0,
            totalProduceAmount = 1800.0,
            transportCess = 150.0,
            totalAmount = 1950.0,
            status = "Placed",
            orderDate = "24 Oct 2024",
            expectedDeliveryDate = "Delivery in 2 days",
            paymentMethod = "Direct UPI",
            paymentStatus = "Escrow Secured",
            vehicleOrTransportNote = "Assigned Vehicle",
            emojiIcon = "🌾"
        )

        repository.placeOrder(testOrder)
        val orders = repository.allOrders.first()
        val found = orders.find { it.id == "ORD-TEST-101" }
        assertNotNull(found)
        assertEquals("Placed", found?.status)

        // Advance Status
        repository.updateOrderStatus("ORD-TEST-101", "In Transit")
        val updatedOrders = repository.allOrders.first()
        val updatedOrder = updatedOrders.find { it.id == "ORD-TEST-101" }
        assertEquals("In Transit", updatedOrder?.status)
    }

    @Test
    fun place_order_records_farmer_contact_for_dialog() = runBlocking {
        val testOrder = Order(
            id = "ORD-TEST-202",
            listingId = "LIST-2",
            cropName = "Bhima Red Onion",
            cropCategory = "Vegetables",
            variety = "Bhima Super",
            farmerName = "Balasaheb Kadam",
            farmerPhone = "+91 94172 88390",
            buyerName = "Direct Buyer",
            buyerPhone = "+91 99999 77777",
            deliveryAddress = "Nashik Road APMC",
            mandiRegion = "Nashik APMC",
            quantity = 100.0,
            unit = "quintal",
            unitPrice = 24.0,
            totalProduceAmount = 2400.0,
            transportCess = 150.0,
            totalAmount = 2550.0,
            status = "Placed",
            orderDate = "25 Oct 2024",
            expectedDeliveryDate = "Delivery in 2 days",
            paymentMethod = "Direct UPI",
            paymentStatus = "Escrow Secured",
            vehicleOrTransportNote = "Assigned Logistics",
            emojiIcon = "🧅"
        )

        repository.placeOrder(testOrder)
        val orders = repository.allOrders.first()
        val found = orders.find { it.id == "ORD-TEST-202" }
        assertNotNull(found)
        assertEquals("+91 94172 88390", found?.farmerPhone)
        assertEquals("Balasaheb Kadam", found?.farmerName)
    }

    @Test
    fun find_nearest_mandi_region_from_coordinates() {
        val regions = SampleData.MANDI_REGIONS
        // Coordinates near Pune (18.53, 73.84)
        val nearest = com.example.util.LocationHelper.findNearestRegion(18.53, 73.84, regions)
        assertNotNull(nearest)
        assertEquals("Pune District Grain Hub", nearest?.first?.name)

        // Coordinates near Delhi (28.70, 77.10)
        val nearestDelhi = com.example.util.LocationHelper.findNearestRegion(28.70, 77.10, regions)
        assertNotNull(nearestDelhi)
        assertEquals("Azadpur APMC Mega Mandi", nearestDelhi?.first?.name)
    }

    @Test
    fun session_manager_persists_user_preferences() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sessionManager = com.example.data.session.SessionManager(context)

        sessionManager.selectedRegionId = "mandi_azadpur"
        sessionManager.selectedTab = com.example.ui.viewmodel.NavigationTab.MANDI_RATES
        sessionManager.selectedCategory = "Vegetables"
        sessionManager.isOrganicOnly = true
        sessionManager.buyerName = "Kisan Buyer Test"

        assertEquals("mandi_azadpur", sessionManager.selectedRegionId)
        assertEquals(com.example.ui.viewmodel.NavigationTab.MANDI_RATES, sessionManager.selectedTab)
        assertEquals("Vegetables", sessionManager.selectedCategory)
        assertTrue(sessionManager.isOrganicOnly)
        assertEquals("Kisan Buyer Test", sessionManager.buyerName)
    }

    @Test
    fun mandi_region_multi_field_case_insensitive_search() {
        val regions = SampleData.MANDI_REGIONS

        // 1. Search by state in lowercase
        val maharashtraResults = regions.filter { it.matchesQuery("maharashtra") }
        assertTrue(maharashtraResults.size >= 3)
        assertTrue(maharashtraResults.any { it.name.contains("Nashik") })
        assertTrue(maharashtraResults.any { it.name.contains("Pune") })
        assertTrue(maharashtraResults.any { it.name.contains("Nagpur") })

        // 2. Search by state in UPPERCASE
        val gujaratResults = regions.filter { it.matchesQuery("GUJARAT") }
        assertTrue(gujaratResults.isNotEmpty())
        assertEquals("Ahmedabad", gujaratResults.first().city)

        // 3. Search by city name
        val indoreResults = regions.filter { it.matchesQuery("indore") }
        assertEquals(1, indoreResults.size)
        assertEquals("Madhya Pradesh", indoreResults.first().state)

        // 4. Search by district name
        val northDelhiResults = regions.filter { it.matchesQuery("north delhi") }
        assertEquals(1, northDelhiResults.size)
        assertEquals("Azadpur APMC Mega Mandi", northDelhiResults.first().name)

        // 5. Search by mandi name
        val gunturResults = regions.filter { it.matchesQuery("Mirchi Yard") }
        assertEquals(1, gunturResults.size)
        assertEquals("Andhra Pradesh", gunturResults.first().state)

        // 6. Search with multi-word terms across fields (e.g., "pune maharashtra")
        val combinedResults = regions.filter { it.matchesQuery("pune maharashtra") }
        assertEquals(1, combinedResults.size)
        assertEquals("Pune", combinedResults.first().city)
    }

    @Test
    fun user_profile_update_and_state_management() = runBlocking {
        val viewModel = com.example.ui.viewmodel.KisanMarketViewModel(repository)
        val initialProfile = viewModel.uiState.value.userProfile
        assertEquals("Your Name", initialProfile.name)
        assertFalse(viewModel.uiState.value.isEditingProfile)

        // Enter edit mode
        viewModel.startEditingProfile()
        assertTrue(viewModel.uiState.value.isEditingProfile)

        // Cancel edit mode
        viewModel.cancelEditingProfile()
        assertFalse(viewModel.uiState.value.isEditingProfile)

        // Update profile
        viewModel.updateUserProfile(
            name = "Vikram Sharma",
            email = "vikram.sharma@kisanmarket.in",
            phone = "+91 98765 43210",
            bio = "Certified organic grower of Alphonso mangoes and spices in Konkan belt.",
            location = "Ratnagiri, Maharashtra",
            role = "Organic Fruit Producer",
            avatarEmoji = "🌾"
        )

        val updatedProfile = viewModel.uiState.value.userProfile
        assertEquals("Vikram Sharma", updatedProfile.name)
        assertEquals("vikram.sharma@kisanmarket.in", updatedProfile.email)
        assertEquals("+91 98765 43210", updatedProfile.phone)
        assertEquals("Certified organic grower of Alphonso mangoes and spices in Konkan belt.", updatedProfile.bio)
        assertEquals("Ratnagiri, Maharashtra", updatedProfile.location)
        assertEquals("Organic Fruit Producer", updatedProfile.role)
        assertEquals("🌾", updatedProfile.avatarEmoji)
        assertFalse(viewModel.uiState.value.isEditingProfile)
        assertEquals("Profile updated successfully!", viewModel.uiState.value.userMessage)
    }

    @Test
    fun notifications_clear_all_persists_across_database_reseeding() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sessionManager = com.example.data.session.SessionManager(context)
        sessionManager.clear()

        val repo = KisanMarketRepository(
            cropListingDao = database.cropListingDao(),
            marketPriceDao = database.marketPriceDao(),
            orderDao = database.orderDao(),
            farmerProfileDao = database.farmerProfileDao(),
            notificationDao = database.notificationDao(),
            userProfileDao = database.userProfileDao(),
            sessionManager = sessionManager
        )

        // Seed notifications initially
        repo.seedDatabaseIfEmpty()
        val initialNotifs = repo.allNotifications.first()
        assertTrue(initialNotifs.isNotEmpty())

        // Clear all notifications
        repo.clearAllNotifications()
        val clearedNotifs = repo.allNotifications.first()
        assertTrue(clearedNotifs.isEmpty())
        assertTrue(sessionManager.isNotificationsCleared)
        assertTrue(sessionManager.isNotificationsInitialized)

        // Simulate app restart / re-seeding
        repo.seedDatabaseIfEmpty()
        val afterRestartNotifs = repo.allNotifications.first()
        assertTrue("Notifications should remain cleared after restart", afterRestartNotifs.isEmpty())
    }

    @Test
    fun userProfile_roomPersistence_updatesAndReadsSuccessfully() = runBlocking {
        val customProfile = com.example.data.model.UserProfile(
            id = "usr_001",
            name = "Ramesh Kumar",
            phone = "+91 98765 43210",
            location = "Nashik, Maharashtra",
            role = "Organic Farmer & Wholesaler"
        )
        repository.updateUserProfile(customProfile)

        val retrievedProfile = repository.getUserProfile()
        assertEquals("Ramesh Kumar", retrievedProfile.name)
        assertEquals("+91 98765 43210", retrievedProfile.phone)
        assertEquals("Nashik, Maharashtra", retrievedProfile.location)
    }

    @Test
    fun cropListings_roomPersistence_insertAndDeleteSuccessfully() = runBlocking {
        val newCrop = com.example.data.model.CropListing(
            id = "test_crop_001",
            title = "Fresh Organic Turmeric",
            category = "Spices",
            variety = "Salem Special",
            farmerName = "Ramesh Kumar",
            farmerId = "usr_001",
            farmerPhone = "+91 98765 43210",
            farmLocation = "Nashik",
            mandiRegion = "Nashik Mandi",
            pricePerUnit = 120.0,
            unit = "kg",
            quantityAvailable = 500.0,
            minOrderQty = 10.0,
            qualityGrade = "Grade A+",
            isOrganic = true,
            isVerifiedFarmer = true,
            harvestDate = "August 2026",
            description = "Freshly harvested organic Salem turmeric directly from farm.",
            mandiBenchmarkPrice = 145.0,
            emojiIcon = "🌿",
            distanceKm = 8.5
        )
        repository.insertListing(newCrop)

        val listings = repository.allListings.first()
        assertTrue(listings.any { it.id == "test_crop_001" && it.title == "Fresh Organic Turmeric" })

        repository.deleteListing(newCrop)
        val listingsAfterDelete = repository.allListings.first()
        assertTrue(listingsAfterDelete.none { it.id == "test_crop_001" })
    }
}

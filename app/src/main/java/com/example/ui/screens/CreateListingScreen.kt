package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.ui.screens.components.MultiImagePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.ui.i18n.LocalAppStrings
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FarmerProfile
import com.example.data.model.MandiRegion
import com.example.data.model.UserProfile
import com.example.data.sample.AgriculturalProduceCatalog
import com.example.data.sample.VarietySeedItem
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.AgroInputHintDark
import com.example.ui.theme.AgroInputLabelDark
import com.example.ui.theme.AgroInputTextDark
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val POPULAR_FARM_LOCATIONS = listOf(
    "Dindori, Nashik",
    "Niphad, Nashik",
    "Yeola, Nashik",
    "Sinnar, Nashik",
    "Lasalgaon, Nashik",
    "Baramati, Pune",
    "Haveli, Pune",
    "Junnar, Pune",
    "Sanwer, Indore",
    "Mhow, Indore",
    "Karnal, Haryana",
    "Azadpur, Delhi",
    "Ludhiana, Punjab",
    "Guntur, Andhra Pradesh",
    "Kolar, Karnataka"
)

val CROP_PRESETS = listOf(
    "Sharbati Wheat" to ("Grains" to "₹34 - ₹38/kg"),
    "Nashik Red Onion" to ("Vegetables" to "₹26 - ₹32/kg"),
    "Desi Hybrid Tomato" to ("Vegetables" to "₹20 - ₹25/kg"),
    "Basmati 1121 Rice" to ("Grains" to "₹44 - ₹48/kg"),
    "Alphonso Mango" to ("Fruits" to "₹420 - ₹500/crate"),
    "Bell Peppers" to ("Vegetables" to "₹60 - ₹70/kg"),
    "Guntur S4 Chilli" to ("Spices" to "₹185 - ₹210/kg"),
    "Yellow Soybean" to ("Grains" to "₹42 - ₹46/kg")
)

val UNITS = listOf("kg", "quintal", "crate", "bag")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    currentFarmer: FarmerProfile?,
    userProfile: UserProfile = UserProfile(),
    selectedRegion: MandiRegion,
    onSubmitListing: (
        title: String,
        category: String,
        variety: String,
        pricePerUnit: Double,
        unit: String,
        quantity: Double,
        minOrderQty: Double,
        qualityGrade: String,
        isOrganic: Boolean,
        harvestDate: String,
        farmerPhone: String,
        description: String,
        farmLocation: String,
        mandiRegion: String,
        imageUris: List<Uri>
    ) -> Unit,
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val profileName = if (userProfile.name.isNotBlank()) userProfile.name else (currentFarmer?.name ?: "Ramesh Patil")
    val profilePhone = if (userProfile.phone.isNotBlank()) userProfile.phone else (currentFarmer?.phone ?: "+91 98220 12345")
    val defaultLocation = if (userProfile.location.isNotBlank()) userProfile.location else (currentFarmer?.let { "${it.village}, ${it.district}" } ?: "Dindori, Nashik")

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Vegetables") }
    var variety by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var quantityText by remember { mutableStateOf("") }
    var minOrderText by remember { mutableStateOf("") }
    var qualityGrade by remember { mutableStateOf("Grade A (Premium Uniform)") }
    var isOrganic by remember { mutableStateOf(false) }
    var harvestDate by remember { mutableStateOf("Fresh Today") }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    var description by remember { mutableStateOf("") }
    var farmLocation by remember {
        mutableStateOf(defaultLocation)
    }
    var locationDropdownExpanded by remember { mutableStateOf(false) }
    var varietyDropdownExpanded by remember { mutableStateOf(false) }
    var isDetectingLocation by remember { mutableStateOf(false) }

    val requestLocationDetection = {
        isDetectingLocation = true
        farmLocation = "" // Clear any existing text before auto-detecting as requested
        locationDropdownExpanded = false
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                val location: Location? = try {
                    locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        ?: locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                } catch (_: SecurityException) {
                    null
                }

                var resolvedLocation: String? = null
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val addr = addresses[0]
                            val subLoc = addr.subLocality ?: addr.locality ?: addr.subAdminArea
                            val district = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                            resolvedLocation = if (subLoc != null && district != null && subLoc != district) {
                                "$subLoc, $district"
                            } else {
                                subLoc ?: district ?: addr.adminArea
                            }
                        }
                    } catch (_: Exception) {
                        // Network lookup failed
                    }

                    if (resolvedLocation.isNullOrBlank()) {
                        resolvedLocation = "${String.format(Locale.ENGLISH, "%.2f", location.latitude)}°N, ${String.format(Locale.ENGLISH, "%.2f", location.longitude)}°E (${selectedRegion.name})"
                    }
                } else {
                    resolvedLocation = "${selectedRegion.city}, ${selectedRegion.district}"
                }

                withContext(Dispatchers.Main) {
                    isDetectingLocation = false
                    if (resolvedLocation != null) {
                        farmLocation = resolvedLocation
                        Toast.makeText(context, "Location detected: $resolvedLocation", Toast.LENGTH_SHORT).show()
                    } else {
                        farmLocation = selectedRegion.displayName
                        Toast.makeText(context, "Set location to: ${selectedRegion.displayName}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isDetectingLocation = false
                    Toast.makeText(context, "Could not detect location: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (isGranted) {
            requestLocationDetection()
        } else {
            Toast.makeText(context, "Location permission denied. Please select or type location manually.", Toast.LENGTH_SHORT).show()
        }
    }

    fun triggerAutoDetect() {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasFine || hasCoarse) {
            requestLocationDetection()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    var showVarietySelector by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Scrollable Top Header (App Identity, Location, Language, Notifications)
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgroGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👨‍🌾", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = strings.sellProduceTitle,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${strings.orderFarmerLabel}: $profileName • ${selectedRegion.name}",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = strings.sellProduceSubtitle,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Quick Preset Suggestions
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Popular Regional Commodities:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(CROP_PRESETS) { (cropTitle, details) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    title = cropTitle
                                    category = details.first
                                    variety = cropTitle.split(" ").firstOrNull() ?: ""
                                    if (cropTitle.contains("Mango", true)) unit = "crate"
                                }
                                .testTag("crop_preset_${cropTitle.replace(" ", "_")}")
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                                Text(text = cropTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text(text = "Guideline: ${details.second}", fontSize = 10.sp, color = AgroGreenAccent)
                            }
                        }
                    }
                }
            }
        }

        // Produce Photos Multi-Image Picker
        item {
            MultiImagePicker(
                imageUris = imageUris,
                onImagesSelected = { imageUris = it },
                onRemoveImage = { removeIdx ->
                    imageUris = imageUris.filterIndexed { idx, _ -> idx != removeIdx }
                },
                maxImages = 3
            )
        }

        // Form Fields
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Crop & Variety Specifications",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(strings.fieldCropTitle) },
                        placeholder = { Text("e.g. Nashik A-Grade Garwa Red Onions", color = AgroInputHintDark) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AgroInputTextDark,
                            unfocusedTextColor = AgroInputTextDark,
                            focusedBorderColor = AgroGreenPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedLabelColor = AgroGreenPrimary,
                            unfocusedLabelColor = AgroInputLabelDark,
                            focusedPlaceholderColor = AgroInputHintDark,
                            unfocusedPlaceholderColor = AgroInputHintDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_listing_title_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Variety & Seed Selection / Manual Input Field
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = variety,
                            onValueChange = {
                                variety = it
                            },
                            label = { Text(strings.fieldVariety) },
                            placeholder = { Text("e.g. Garwa / Sharbati (or type custom seed)", color = AgroInputHintDark) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = "Variety / Seed",
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    if (variety.isNotBlank()) {
                                        IconButton(
                                            onClick = { variety = "" },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear variety",
                                                tint = Color(0xFF94A3B8),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { varietyDropdownExpanded = !varietyDropdownExpanded },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("toggle_variety_dropdown_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select variety from list",
                                            tint = AgroGreenPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AgroInputTextDark,
                                unfocusedTextColor = AgroInputTextDark,
                                focusedBorderColor = AgroGreenPrimary,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = AgroGreenPrimary,
                                unfocusedLabelColor = AgroInputLabelDark,
                                focusedPlaceholderColor = AgroInputHintDark,
                                unfocusedPlaceholderColor = AgroInputHintDark
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_listing_variety_input")
                        )

                        val allVarieties = remember { AgriculturalProduceCatalog.VARIETY_CATALOG.values.flatten() }
                        val matchingVarieties = remember(variety, category) {
                            if (variety.isBlank()) {
                                AgriculturalProduceCatalog.VARIETY_CATALOG[category]?.take(6)
                                    ?: allVarieties.take(6)
                            } else {
                                allVarieties.filter {
                                    it.varietyName.contains(variety, ignoreCase = true) ||
                                    it.cropName.contains(variety, ignoreCase = true) ||
                                    it.category.contains(variety, ignoreCase = true)
                                }.take(6)
                            }
                        }

                        DropdownMenu(
                            expanded = varietyDropdownExpanded,
                            onDismissRequest = { varietyDropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White)
                        ) {
                            if (variety.isNotBlank()) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                tint = AgroGreenPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Use custom variety: \"$variety\"",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AgroGreenPrimary
                                            )
                                        }
                                    },
                                    onClick = {
                                        varietyDropdownExpanded = false
                                    }
                                )
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                            }

                            Text(
                                text = "Suggested Certified Seed Varieties",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )

                            matchingVarieties.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = item.varietyName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "${item.cropName} • ${item.seedCompanyOrType}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    },
                                    onClick = {
                                        variety = item.varietyName
                                        category = item.category
                                        val cleanCropName = item.cropName.substringBefore(" (")
                                        if (title.isBlank() || title.contains("Fresh") || title.contains("Harvest")) {
                                            title = "$cleanCropName - ${item.varietyName}"
                                        }
                                        unit = item.typicalUnit
                                        varietyDropdownExpanded = false
                                    }
                                )
                            }

                            HorizontalDivider(color = Color(0xFFF1F5F9))

                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = AgroGreenAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Browse Full Seed Catalog (40+ items) ›",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AgroGreenAccent
                                        )
                                    }
                                },
                                onClick = {
                                    varietyDropdownExpanded = false
                                    showVarietySelector = true
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "💡 Type your own custom variety name manually, or pick from the certified catalog.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Variety & Seed Catalog Dropdown Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showVarietySelector = true }
                            .testTag("browse_variety_seeds_catalog_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Browse Agricultural Varieties & Seeds Menu",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AgroGreenPrimary
                                )
                            }
                            Text(
                                text = "Select ›",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Commodity Category Selection Chips
                    Text(text = "Commodity Category:", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(listOf("Vegetables", "Fruits", "Grains", "Pulses", "Spices", "Organic")) { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = {
                                    category = cat
                                    if (cat == "Organic") isOrganic = true
                                },
                                label = { Text(cat) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgroGreenPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("create_listing_category_chip_${cat.lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Unit Selection Chips
                    Text(text = "Measurement Unit:", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UNITS.forEach { u ->
                            FilterChip(
                                selected = unit == u,
                                onClick = { unit = u },
                                label = { Text(u.uppercase()) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgroGreenPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Price & Stock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Direct Price (₹/$unit)") },
                            placeholder = { Text("e.g. 28.5", color = AgroInputHintDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AgroInputTextDark,
                                unfocusedTextColor = AgroInputTextDark,
                                focusedBorderColor = AgroGreenPrimary,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = AgroGreenPrimary,
                                unfocusedLabelColor = AgroInputLabelDark,
                                focusedPlaceholderColor = AgroInputHintDark,
                                unfocusedPlaceholderColor = AgroInputHintDark
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("create_listing_price_input")
                        )

                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            label = { Text("Total Quantity ($unit)") },
                            placeholder = { Text("e.g. 2500", color = AgroInputHintDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AgroInputTextDark,
                                unfocusedTextColor = AgroInputTextDark,
                                focusedBorderColor = AgroGreenPrimary,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = AgroGreenPrimary,
                                unfocusedLabelColor = AgroInputLabelDark,
                                focusedPlaceholderColor = AgroInputHintDark,
                                unfocusedPlaceholderColor = AgroInputHintDark
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("create_listing_quantity_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = minOrderText,
                        onValueChange = { minOrderText = it },
                        label = { Text("Minimum Order Quantity ($unit)") },
                        placeholder = { Text("e.g. 25", color = AgroInputHintDark) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AgroInputTextDark,
                            unfocusedTextColor = AgroInputTextDark,
                            focusedBorderColor = AgroGreenPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedLabelColor = AgroGreenPrimary,
                            unfocusedLabelColor = AgroInputLabelDark,
                            focusedPlaceholderColor = AgroInputHintDark,
                            unfocusedPlaceholderColor = AgroInputHintDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_listing_min_order_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Harvest Info & Mobile Number section
                    Text(
                        text = "Harvest & Contact Info",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Farm Location Input with Dropdown and Auto-Detect GPS Button
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = farmLocation,
                            onValueChange = {
                                farmLocation = it
                            },
                            label = { Text("Farm / Dispatch Location") },
                            placeholder = { Text(if (isDetectingLocation) "Detecting location..." else "e.g. Dindori, Nashik", color = AgroInputHintDark) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Farm Location",
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    if (farmLocation.isNotBlank() && !isDetectingLocation) {
                                        IconButton(
                                            onClick = { farmLocation = "" },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear location",
                                                tint = Color(0xFF94A3B8),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    if (isDetectingLocation) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = AgroGreenPrimary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    } else {
                                        IconButton(
                                            onClick = { triggerAutoDetect() },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .testTag("auto_detect_location_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MyLocation,
                                                contentDescription = "Auto-detect GPS Location",
                                                tint = AgroGreenPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { locationDropdownExpanded = !locationDropdownExpanded },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("toggle_location_dropdown_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select from popular locations",
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = AgroInputTextDark,
                                unfocusedTextColor = AgroInputTextDark,
                                focusedBorderColor = AgroGreenPrimary,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = AgroGreenPrimary,
                                unfocusedLabelColor = AgroInputLabelDark,
                                focusedPlaceholderColor = AgroInputHintDark,
                                unfocusedPlaceholderColor = AgroInputHintDark
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_listing_location_input")
                        )

                        val filteredLocations = remember(farmLocation) {
                            if (farmLocation.isBlank()) POPULAR_FARM_LOCATIONS
                            else POPULAR_FARM_LOCATIONS.filter {
                                it.contains(farmLocation, ignoreCase = true)
                            }.ifEmpty { POPULAR_FARM_LOCATIONS }
                        }

                        DropdownMenu(
                            expanded = locationDropdownExpanded,
                            onDismissRequest = { locationDropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White)
                        ) {
                            Text(
                                text = "Suggested Regional Locations",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                            filteredLocations.take(8).forEach { locationOption ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Outlined.Place,
                                                contentDescription = null,
                                                tint = AgroGreenAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = locationOption,
                                                fontSize = 13.sp,
                                                color = Color(0xFF0F172A),
                                                fontWeight = if (farmLocation == locationOption) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    },
                                    onClick = {
                                        farmLocation = locationOption
                                        locationDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Auto-Detect Quick Action Row
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFECFDF5),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { triggerAutoDetect() }
                            .testTag("auto_detect_location_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = AgroGreenPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDetectingLocation) "Detecting GPS location..." else "Auto-detect current GPS farm location",
                                fontSize = 11.sp,
                                color = AgroGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = harvestDate,
                        onValueChange = { harvestDate = it },
                        label = { Text("Harvest Date") },
                        placeholder = { Text("e.g. 27 Aug 2026 / Fresh Today", color = AgroInputHintDark) },
                        trailingIcon = {
                            IconButton(
                                onClick = { showDatePickerDialog = true },
                                modifier = Modifier.testTag("open_harvest_date_picker_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Pick Harvest Date",
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AgroInputTextDark,
                            unfocusedTextColor = AgroInputTextDark,
                            focusedBorderColor = AgroGreenPrimary,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedLabelColor = AgroGreenPrimary,
                            unfocusedLabelColor = AgroInputLabelDark,
                            focusedPlaceholderColor = AgroInputHintDark,
                            unfocusedPlaceholderColor = AgroInputHintDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_listing_harvest_date_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Contact Phone automatically displayed from User Profile
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_listing_phone_display")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AgroGreenPrimary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Contact Phone",
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Contact Mobile (From Profile)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = profilePhone,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFECFDF5)
                            ) {
                                Text(
                                    text = "Profile",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgroGreenAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "Please enter crop title"
                                return@Button
                            }
                            val price = priceText.toDoubleOrNull()
                            if (price == null || price <= 0) {
                                errorMessage = "Please enter valid price per $unit"
                                return@Button
                            }
                            val qty = quantityText.toDoubleOrNull() ?: 100.0
                            val minOrder = minOrderText.toDoubleOrNull() ?: 10.0

                            errorMessage = null
                            onSubmitListing(
                                title,
                                category.ifBlank { "Vegetables" },
                                variety.ifBlank { "Standard" },
                                price,
                                unit,
                                qty,
                                minOrder,
                                qualityGrade,
                                isOrganic,
                                harvestDate.ifBlank { "Fresh Harvest" },
                                profilePhone,
                                description.ifBlank { "Fresh harvest direct from verified farm gate." },
                                farmLocation,
                                selectedRegion.name,
                                imageUris
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("publish_listing_submit_button")
                    ) {
                        Icon(imageVector = Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = strings.btnPublishListing, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                            harvestDate = formatter.format(Date(millis))
                        }
                        showDatePickerDialog = false
                    },
                    modifier = Modifier.testTag("confirm_date_picker_btn")
                ) {
                    Text("Select", fontWeight = FontWeight.Bold, color = AgroGreenPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false },
                    modifier = Modifier.testTag("cancel_date_picker_btn")
                ) {
                    Text("Cancel", color = Color(0xFF64748B))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = AgroGreenPrimary,
                    todayDateBorderColor = AgroGreenPrimary,
                    todayContentColor = AgroGreenPrimary,
                    selectedYearContainerColor = AgroGreenPrimary
                )
            )
        }
    }

    if (showVarietySelector) {
        VarietySeedSelectionSheet(
            onDismiss = { showVarietySelector = false },
            onSelectVariety = { item ->
                variety = item.varietyName
                category = item.category
                val cleanCropName = item.cropName.substringBefore(" (")
                if (title.isBlank() || title.contains("Fresh") || title.contains("Harvest")) {
                    title = "$cleanCropName - ${item.varietyName}"
                }
                unit = item.typicalUnit
                if (description.isBlank()) {
                    description = "${item.keyTraits} Seed lineage: ${item.seedCompanyOrType}."
                }
                showVarietySelector = false
            },
            onSelectCustomVariety = { customVarietyName ->
                variety = customVarietyName
                if (title.isBlank() || title.contains("Fresh") || title.contains("Harvest")) {
                    title = "$customVarietyName Produce"
                }
                showVarietySelector = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VarietySeedSelectionSheet(
    onDismiss: () -> Unit,
    onSelectVariety: (VarietySeedItem) -> Unit,
    onSelectCustomVariety: (String) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val categories = AgriculturalProduceCatalog.CATEGORIES
    val currentCategory = categories.getOrElse(selectedCategoryIndex) { "Vegetables" }

    val allVarietiesForCategory = AgriculturalProduceCatalog.VARIETY_CATALOG[currentCategory] ?: emptyList()
    val filteredVarieties = remember(searchQuery, currentCategory) {
        if (searchQuery.isBlank()) {
            allVarietiesForCategory
        } else {
            // Search across the selected category first or all categories
            AgriculturalProduceCatalog.VARIETY_CATALOG.values.flatten().filter {
                it.varietyName.contains(searchQuery, ignoreCase = true) ||
                        it.cropName.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true) ||
                        it.seedCompanyOrType.contains(searchQuery, ignoreCase = true) ||
                        it.keyTraits.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {
            // Top Header
            Surface(
                color = AgroGreenPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌱", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Agricultural Variety & Seeds Catalog",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Verified commercial & certified farm seed lineages",
                                    fontSize = 11.5.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search variety, crop, or type custom seed...", fontSize = 13.sp, color = AgroInputHintDark) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = AgroGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = AgroGreenPrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedPlaceholderColor = AgroInputHintDark,
                            unfocusedPlaceholderColor = AgroInputHintDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("variety_catalog_search_input")
                    )
                }
            }

            // Category Tabs (Only if not actively searching globally)
            if (searchQuery.isBlank()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    edgePadding = 12.dp,
                    containerColor = Color(0xFFF8FAFC),
                    contentColor = AgroGreenPrimary,
                    divider = {}
                ) {
                    categories.forEachIndexed { index, catName ->
                        val isSelected = selectedCategoryIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedCategoryIndex = index },
                            text = {
                                Text(
                                    text = catName,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) AgroGreenPrimary else Color(0xFF64748B)
                                )
                            }
                        )
                    }
                }
            } else {
                Surface(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Showing results across all agricultural categories (${filteredVarieties.size} found)",
                        fontSize = 11.5.sp,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Varieties List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Custom variety entry card if user typed a search query
                if (searchQuery.isNotBlank()) {
                    item(key = "custom_variety_entry_action") {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                            border = BorderStroke(1.5.dp, Color(0xFF86EFAC)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectCustomVariety(searchQuery.trim()) }
                                .testTag("select_custom_variety_card")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = AgroGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Use Custom Variety: \"$searchQuery\"",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AgroGreenPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Not in catalog? Tap here to use this custom seed name.",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF15803D)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AgroGreenPrimary
                                ) {
                                    Text(
                                        text = "Set Custom ➔",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (filteredVarieties.isEmpty() && searchQuery.isNotBlank()) {
                    item(key = "no_matching_varieties_notice") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No catalog varieties match \"$searchQuery\"",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You can still use it as your custom variety using the button above!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = AgroGreenPrimary
                            )
                        }
                    }
                }

                items(filteredVarieties, key = { it.varietyName + it.cropName }) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectVariety(item) }
                            .testTag("variety_item_${item.varietyName.replace(" ", "_")}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.varietyName,
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = item.cropName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AgroGreenAccent
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Text(
                                        text = "Select ➔",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Traits & Seed Lineage
                            Text(
                                text = item.keyTraits,
                                fontSize = 12.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.5.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🔬 Seed Lineage: ", fontSize = 11.sp, color = Color(0xFF64748B))
                                    Text(
                                        text = item.seedCompanyOrType,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1E293B)
                                    )
                                }

                                Text(
                                    text = item.benchmarkPriceRange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HarvestAmber
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

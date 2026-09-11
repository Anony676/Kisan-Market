package com.example.ui.screens.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Search
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MandiRegion
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedDistrict
import com.example.ui.i18n.localizedName
import com.example.ui.i18n.localizedState
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenLight
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.AgroInputHintDark
import com.example.ui.theme.HarvestAmber
import com.example.util.LocationHelper
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun RegionSelectorDialog(
    currentRegion: MandiRegion,
    regions: List<MandiRegion>,
    onSelectRegion: (MandiRegion) -> Unit,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val safeDismiss = {
        keyboardController?.hide()
        focusManager.clearFocus()
        onDismiss()
    }

    val safeSelectRegion: (MandiRegion) -> Unit = { region ->
        keyboardController?.hide()
        focusManager.clearFocus()
        onSelectRegion(region)
    }

    var searchQuery by remember { mutableStateOf("") }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var detectionStatusMessage by remember { mutableStateOf<String?>(null) }
    var detectedCoordinates by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    val performLocationDetection = {
        isDetectingLocation = true
        detectionStatusMessage = "Fetching GPS coordinates..."

        LocationHelper.requestSingleLocation(
            context = context,
            onLocationFound = { lat, lng ->
                detectedCoordinates = Pair(lat, lng)
                coroutineScope.launch {
                    val address = LocationHelper.reverseGeocode(context, lat, lng)
                    val nearestMatch = LocationHelper.findNearestRegion(lat, lng, regions)

                    isDetectingLocation = false
                    if (nearestMatch != null) {
                        val (nearestRegion, distanceKm) = nearestMatch
                        val distFormatted = "%.1f".format(distanceKm)
                        detectionStatusMessage = if (address != null) {
                            "📍 Detected: $address • Nearest: ${nearestRegion.name} (${distFormatted} km)"
                        } else {
                            "📍 Nearest Mandi: ${nearestRegion.name} (${distFormatted} km away)"
                        }
                        // Automatically select the nearest detected Mandi
                        safeSelectRegion(nearestRegion)
                    } else {
                        detectionStatusMessage = "Location acquired (${"%.2f".format(lat)}, ${"%.2f".format(lng)})"
                    }
                }
            },
            onError = { error ->
                isDetectingLocation = false
                detectionStatusMessage = "Unable to fetch location: $error"
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            performLocationDetection()
        } else {
            isDetectingLocation = false
            detectionStatusMessage = "Location permission denied. Please search or pick a region below."
        }
    }

    val onDetectLocationClicked = {
        if (LocationHelper.hasLocationPermission(context)) {
            performLocationDetection()
        } else {
            isDetectingLocation = true
            detectionStatusMessage = "Requesting location permission..."
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Filter regions based on search query across name, city, district, state, and commodities
    val filteredRegions = remember(searchQuery, regions) {
        if (searchQuery.isBlank()) {
            regions
        } else {
            regions.filter { it.matchesQuery(searchQuery) }
        }
    }

    Dialog(
        onDismissRequest = safeDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AgroGreenLight,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = strings.regionSelectorTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = strings.regionSelectorSubtitle,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(
                        onClick = safeDismiss,
                        modifier = Modifier.testTag("close_region_selector_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = strings.close,
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature 1: Location Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = strings.searchRegionPlaceholder,
                            fontSize = 13.sp,
                            color = AgroInputHintDark
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = AgroGreenAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.testTag("clear_mandi_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8FAF5),
                        unfocusedContainerColor = Color(0xFFF8FAF5),
                        focusedBorderColor = AgroGreenPrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = Color(0xFF0F172A),
                        unfocusedTextColor = Color(0xFF0F172A),
                        focusedPlaceholderColor = AgroInputHintDark,
                        unfocusedPlaceholderColor = AgroInputHintDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("mandi_search_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick state filter chips
                val popularStates = remember { listOf("All", "Maharashtra", "Delhi", "Gujarat", "Punjab", "Karnataka", "Rajasthan", "Madhya Pradesh", "Uttar Pradesh") }
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(popularStates) { stateName ->
                        val isSelected = if (stateName == "All") searchQuery.isBlank() else searchQuery.equals(stateName, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) AgroGreenPrimary else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    searchQuery = if (stateName == "All") "" else stateName
                                }
                        ) {
                            Text(
                                text = stateName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Feature 2: Detect My Location Button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isDetectingLocation) Color(0xFFFEF3C7) else Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isDetectingLocation) HarvestAmber else AgroGreenAccent.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isDetectingLocation) { onDetectLocationClicked() }
                        .testTag("detect_location_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (isDetectingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = AgroGreenPrimary
                            )
                        } else {
                            Surface(
                                shape = CircleShape,
                                color = AgroGreenPrimary,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = "Detect My Location",
                                        tint = Color.White,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isDetectingLocation) "Detecting Location..." else "Detect My Location",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenPrimary
                            )
                            Text(
                                text = if (isDetectingLocation) "Connecting to GPS sensor..." else "Find nearest APMC Mandi automatically",
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        if (!isDetectingLocation) {
                            Icon(
                                imageVector = Icons.Outlined.NearMe,
                                contentDescription = null,
                                tint = AgroGreenAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Detection status / result banner
                AnimatedVisibility(
                    visible = detectionStatusMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    detectionStatusMessage?.let { msg ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = msg,
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "SEARCH RESULTS (${filteredRegions.size})" else "AVAILABLE MANDI REGIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )

                    if (searchQuery.isNotBlank()) {
                        Text(
                            text = "${filteredRegions.size} found",
                            fontSize = 11.sp,
                            color = AgroGreenAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List of Mandi Regions
                if (filteredRegions.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAF5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Explore,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Mandi Hubs Found",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "No APMC matching \"$searchQuery\". Try searching by state or commodity name.",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AgroGreenPrimary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { searchQuery = "" }
                                    .testTag("reset_search_button")
                            ) {
                                Text(
                                    text = "View All Regions",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        items(filteredRegions, key = { it.id }) { region ->
                            val isSelected = region.id == currentRegion.id

                            val distanceText = remember(detectedCoordinates, region) {
                                detectedCoordinates?.let { coords ->
                                    if (region.latitude != 0.0 && region.longitude != 0.0) {
                                        val dist = LocationHelper.calculateDistanceKm(
                                            coords.first, coords.second,
                                            region.latitude, region.longitude
                                        )
                                        "%.1f km".format(dist)
                                    } else null
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFFDCFCE7) else Color(0xFFF8FAF5),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AgroGreenAccent) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { safeSelectRegion(region) }
                                    .testTag("region_item_${region.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            val lang = com.example.ui.i18n.LocalAppLanguage.current
                                            Text(
                                                text = region.localizedName(lang),
                                                fontSize = 14.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isSelected) AgroGreenPrimary else Color(0xFF0F172A)
                                            )

                                            if (distanceText != null) {
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = Color(0xFFE2E8F0)
                                                ) {
                                                    Text(
                                                        text = distanceText,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = Color(0xFF475569),
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }

                                        val lang = com.example.ui.i18n.LocalAppLanguage.current
                                        val locDistrict = region.localizedDistrict(lang)
                                        val locState = region.localizedState(lang)
                                        val locationSubtitle = if (region.city.isNotBlank() && !region.city.equals(region.district, ignoreCase = true)) {
                                            "${region.city} • $locDistrict, $locState"
                                        } else {
                                            "$locDistrict, $locState"
                                        }
                                        Text(
                                            text = locationSubtitle,
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Key Crops: ${region.majorCommodities.joinToString(", ")}",
                                            fontSize = 11.sp,
                                            color = AgroGreenAccent
                                        )
                                    }

                                    if (isSelected) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(AgroGreenPrimary)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

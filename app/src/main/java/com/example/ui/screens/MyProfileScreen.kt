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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.data.model.CropListing
import com.example.data.model.UserProfile
import com.example.ui.screens.components.ProducePhotoProvider
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

@Composable
fun MyProfileScreen(
    userProfile: UserProfile,
    activeListings: List<CropListing> = emptyList(),
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onSaveProfile: (name: String, email: String, phone: String, bio: String, location: String, role: String, avatarEmoji: String) -> Unit,
    onDeleteListing: (CropListing) -> Unit = {},
    onAddNewListing: () -> Unit = {},
    onLogout: () -> Unit = {},
    onSwitchAccount: () -> Unit = {},
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Pure My Profile Screen with smooth animated transition between View mode and Edit mode
    AnimatedContent(
        targetState = isEditing,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ProfileModeTransition",
        modifier = modifier.fillMaxSize()
    ) { inEditMode ->
        if (inEditMode) {
            EditProfileForm(
                initialProfile = userProfile,
                onCancel = onCancelEditing,
                onSave = onSaveProfile,
                topHeader = topHeader
            )
        } else {
            ProfileViewContent(
                profile = userProfile,
                activeListings = activeListings,
                onEditClick = onStartEditing,
                onDeleteListing = onDeleteListing,
                onAddNewListing = onAddNewListing,
                onLogout = onLogout,
                onSwitchAccount = onSwitchAccount,
                topHeader = topHeader
            )
        }
    }
}

/**
 * Clean & Modern Material 3 Display View of My Profile
 */
@Composable
private fun ProfileViewContent(
    profile: UserProfile,
    activeListings: List<CropListing>,
    onEditClick: () -> Unit,
    onDeleteListing: (CropListing) -> Unit,
    onAddNewListing: () -> Unit,
    onLogout: () -> Unit = {},
    onSwitchAccount: () -> Unit = {},
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showAboutAppDialog by rememberSaveable { mutableStateOf(false) }

    if (showAboutAppDialog) {
        AboutAppDialog(
            onDismiss = { showAboutAppDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 0.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Scrollable Top Header (App Identity, Location, Language, Notifications)
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Hero Profile Header Card with circular profile image, name, role & KYC badge
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("profile_hero_card")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Settings icon button in top right corner
                    IconButton(
                        onClick = { showAboutAppDialog = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .testTag("profile_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "About App Settings",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    // Circular Profile Image with Decorative Ring & Verified Checkmark Overlay
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        // Decorative outer glow/ring
                        Box(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(AgroGreenPrimary, Color(0xFF86EFAC), AgroGreenAccent)
                                    )
                                )
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Circular Profile Avatar Container
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7))
                                    .testTag("profile_avatar_image")
                            ) {
                                Text(
                                    text = profile.avatarEmoji,
                                    fontSize = 54.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Verified checkmark badge overlaid at bottom right
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Profile",
                                    tint = PriceUpGreen,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }

                    // User Full Name
                    Text(
                        text = profile.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("profile_user_name")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Role / Designation
                    Text(
                        text = profile.role,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgroGreenPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Verified Badge & Kisan ID Chips
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color(0xFF1D4ED8),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = profile.verifiedStatus,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "ID: ${profile.kisanId}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prominent 'Edit Profile' Button
                    Button(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgroGreenPrimary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Profile",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                }
            }
        }

        // Contact Details Section Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("profile_contact_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = AgroGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Contact Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Number
                    ContactDetailRow(
                        icon = Icons.Default.Call,
                        label = "Phone Number",
                        value = profile.phone,
                        iconTint = AgroGreenPrimary,
                        testTag = "contact_phone_value"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Address
                    ContactDetailRow(
                        icon = Icons.Default.Email,
                        label = "Email Address",
                        value = profile.email,
                        iconTint = Color(0xFF2563EB),
                        testTag = "contact_email_value"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Farm & Trade Location
                    ContactDetailRow(
                        icon = Icons.Default.LocationOn,
                        label = "Farm / Trade Location",
                        value = profile.location,
                        iconTint = Color(0xFFEA580C),
                        testTag = "contact_location_value"
                    )
                }
            }
        }

        // Active Produce Listings Card (Placed immediately before 'About' section)
        item {
            ActiveListingsCard(
                listings = activeListings,
                onDeleteListing = onDeleteListing,
                onAddNewListing = onAddNewListing
            )
        }

        // Multi-line 'About' Section Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("profile_about_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "About",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Multi-line Bio / About Text
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAF5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = profile.bio.ifBlank { "No bio added yet. Click 'Edit Profile' to add your agricultural background, crops grown, or trade specializations." },
                            fontSize = 13.5.sp,
                            color = Color(0xFF334155),
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .padding(14.dp)
                                .testTag("profile_about_bio_text")
                        )
                    }
                }
            }
        }

        // Trading Reputation & Account Activity Stats
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Market Activity & Trust",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProfileMetricItem(
                            title = "Trust Score",
                            value = "★ ${profile.rating}",
                            subtitle = "Verified P2P",
                            accentColor = HarvestAmber,
                            modifier = Modifier.weight(1f)
                        )
                        ProfileMetricItem(
                            title = "Completed Deals",
                            value = "${profile.completedDeals}",
                            subtitle = "Direct Dispatches",
                            accentColor = AgroGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        ProfileMetricItem(
                            title = "Active Lots",
                            value = "${profile.activeListings}",
                            subtitle = "In Marketplace",
                            accentColor = Color(0xFF2563EB),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Account Management Card (Switch Account / Logout)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("profile_account_actions_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Account Security & Session",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Switch Account / Login with different number
                    OutlinedButton(
                        onClick = onSwitchAccount,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AgroGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("profile_switch_account_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AgroGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Switch Account / Login",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = AgroGreenPrimary
                        )
                    }

                    // Logout Button
                    Button(
                        onClick = onLogout,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFEE2E2),
                            contentColor = Color(0xFFDC2626)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("profile_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out of Kisan Market",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Edit Profile Form Screen with State Management and Validation
 */
@Composable
private fun EditProfileForm(
    initialProfile: UserProfile,
    onCancel: () -> Unit,
    onSave: (name: String, email: String, phone: String, bio: String, location: String, role: String, avatarEmoji: String) -> Unit,
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    // Local State Management for Editable Fields
    var name by rememberSaveable { mutableStateOf(initialProfile.name) }
    var email by rememberSaveable { mutableStateOf(initialProfile.email) }
    var phone by rememberSaveable { mutableStateOf(initialProfile.phone) }
    var location by rememberSaveable { mutableStateOf(initialProfile.location) }
    var role by rememberSaveable { mutableStateOf(initialProfile.role) }
    var bio by rememberSaveable { mutableStateOf(initialProfile.bio) }
    var selectedEmoji by rememberSaveable { mutableStateOf(initialProfile.avatarEmoji) }

    // Validation State
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var bioError by remember { mutableStateOf<String?>(null) }

    val availableEmojis = listOf("👨‍🌾", "👩‍🌾", "🚜", "🌾", "🧑‍🌾", "🏢")

    fun validateAndSave() {
        var isValid = true

        if (name.trim().isBlank()) {
            nameError = "Name cannot be empty"
            isValid = false
        } else {
            nameError = null
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (email.trim().isBlank()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!email.trim().matches(emailRegex) && !email.trim().lowercase().contains("gmail") && !email.trim().contains("@")) {
            emailError = "Please enter a valid email address"
            isValid = false
        } else {
            emailError = null
        }

        if (phone.trim().isBlank()) {
            phoneError = "Phone number cannot be empty"
            isValid = false
        } else if (phone.trim().length < 10) {
            phoneError = "Phone number must be at least 10 digits"
            isValid = false
        } else {
            phoneError = null
        }

        if (bio.trim().length > 500) {
            bioError = "Bio must be under 500 characters"
            isValid = false
        } else {
            bioError = null
        }

        if (isValid) {
            onSave(
                name.trim(),
                email.trim(),
                phone.trim(),
                bio.trim(),
                location.trim(),
                role.trim(),
                selectedEmoji
            )
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Scrollable Top Header (App Identity, Location, Language, Notifications)
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Form Title Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFDCFCE7))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = AgroGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Edit Profile",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Update your identity & contact information",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        IconButton(
                            onClick = onCancel,
                            modifier = Modifier.testTag("close_edit_profile_icon_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel Editing",
                                tint = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Avatar Selector
                    Text(
                        text = "Choose Profile Avatar:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(availableEmojis) { emoji ->
                            val isSelected = emoji == selectedEmoji
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, AgroGreenPrimary) else null,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .clickable { selectedEmoji = emoji }
                                    .testTag("avatar_option_$emoji")
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = emoji, fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Editable Input Fields Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Personal & Contact Information",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // 1. Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (nameError != null) nameError = null
                        },
                        label = { Text("Full Name *") },
                        placeholder = { Text("Your Name") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = AgroGreenPrimary)
                        },
                        isError = nameError != null,
                        supportingText = nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_name_input")
                    )

                    // 2. Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError != null) emailError = null
                        },
                        label = { Text("Email Address *") },
                        placeholder = { Text("yourgmail gmail com") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFF2563EB))
                        },
                        isError = emailError != null,
                        supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_email_input")
                    )

                    // 3. Phone Number Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            if (phoneError != null) phoneError = null
                        },
                        label = { Text("Phone Number *") },
                        placeholder = { Text("+91 00000 00000") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = AgroGreenPrimary)
                        },
                        isError = phoneError != null,
                        supportingText = phoneError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_phone_input")
                    )

                    // 4. Role / Specialty Designation
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role / Specialty") },
                        placeholder = { Text("Progressive Farmer & Trader") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = Color(0xFF8B5CF6))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_role_input")
                    )

                    // 5. Location Field
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Farm / Business Location") },
                        placeholder = { Text("Your Location") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFEA580C))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_location_input")
                    )

                    // 6. Multi-line 'About' Bio Field
                    OutlinedTextField(
                        value = bio,
                        onValueChange = {
                            bio = it
                            if (bioError != null) bioError = null
                        },
                        label = { Text("About Bio (Multi-line) *") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706))
                        },
                        placeholder = {
                            Text("Tell buyers and fellow farmers about your farm, crop varieties, farming practices, or trade experience...")
                        },
                        minLines = 4,
                        maxLines = 8,
                        isError = bioError != null,
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = bioError ?: "Describe your farming experience & produce",
                                    color = if (bioError != null) MaterialTheme.colorScheme.error else Color(0xFF64748B),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${bio.length}/500",
                                    color = if (bio.length > 500) MaterialTheme.colorScheme.error else Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgroGreenPrimary,
                            focusedLabelColor = AgroGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_bio_input")
                    )
                }
            }
        }

        // Action Buttons: Cancel and Save Changes
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B)),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("cancel_edit_profile_button")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        validateAndSave()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AgroGreenPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(50.dp)
                        .testTag("save_profile_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun ContactDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = iconTint.copy(alpha = 0.1f),
            modifier = Modifier.size(38.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}

@Composable
private fun ProfileMetricItem(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAF5),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1
            )
        }
    }
}

/**
 * Active Listings Card displaying crops/produce listed by the farmer
 * with direct delete listing option and confirmation dialog.
 */
@Composable
private fun ActiveListingsCard(
    listings: List<CropListing>,
    onDeleteListing: (CropListing) -> Unit,
    onAddNewListing: () -> Unit,
    modifier: Modifier = Modifier
) {
    var listingToDelete by remember { mutableStateOf<CropListing?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("profile_active_listings_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = AgroGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Active Listings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (listings.isNotEmpty()) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = if (listings.isNotEmpty()) "${listings.size} Active" else "0 Listed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (listings.isNotEmpty()) AgroGreenPrimary else Color(0xFF64748B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            if (listings.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🌾", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "No active harvest listings yet",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "List your crops to sell directly to verified buyers in your Mandi.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onAddNewListing,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgroGreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("add_produce_listing_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "List Produce", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listings.forEach { listing ->
                        ActiveListingItem(
                            listing = listing,
                            onDelete = { listingToDelete = listing }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    listingToDelete?.let { listing ->
        AlertDialog(
            onDismissRequest = { listingToDelete = null },
            title = {
                Text(
                    text = "Delete Listing?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${listing.title}' (${listing.quantityAvailable.toInt()} ${listing.unit})? This will immediately remove it from Kisan Market.",
                    fontSize = 14.sp,
                    color = Color(0xFF475569)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toDelete = listing
                        listingToDelete = null
                        onDeleteListing(toDelete)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_delete_listing_button")
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { listingToDelete = null },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("cancel_delete_listing_button")
                ) {
                    Text("Cancel", color = Color(0xFF475569))
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

@Composable
private fun ActiveListingItem(
    listing: CropListing,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF8FAF5),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_listing_item_${listing.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Produce Photo Avatar
                val photoUrl = ProducePhotoProvider.getPrimaryPhotoForListing(listing)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDCFCE7))
                ) {
                    if (photoUrl.isNotBlank()) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = listing.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = listing.emojiIcon,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Title & Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = listing.title,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1
                    )
                    Text(
                        text = "${listing.category} • ${listing.variety}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1
                    )
                }

                // Delete Action Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEE2E2))
                        .testTag("delete_listing_button_${listing.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Listing",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0).copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${listing.pricePerUnit.toInt()}/${listing.unit}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgroGreenPrimary
                    )
                    Text(
                        text = " • ${listing.quantityAvailable.toInt()} ${listing.unit} in stock",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (listing.isOrganic) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "🌱 Organic",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenPrimary,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = listing.qualityGrade,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * About App Dialog pop-up
 */
@Composable
private fun AboutAppDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        icon = {
            Surface(
                shape = CircleShape,
                color = Color(0xFFDCFCE7),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About App Info",
                        tint = AgroGreenPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        title = {
            Text(
                text = "About App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_app_headline")
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "App Version v1.0",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("about_app_version")
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = Color(0xFFE2E8F0)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Developers",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Lukesh Raj, Lunesh Raj",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("about_app_developer")
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AgroGreenPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("about_app_close_button")
            ) {
                Text(
                    text = "Close",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        modifier = Modifier.testTag("about_app_dialog")
    )
}


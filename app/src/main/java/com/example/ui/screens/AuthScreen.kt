package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AgroCanvasLight
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenBright
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.AgroInputHintDark
import com.example.ui.theme.AgroInputLabelDark
import com.example.ui.theme.AgroInputTextDark
import com.example.ui.theme.AgroTextPrimary
import com.example.ui.theme.AgroTextSecondary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.HarvestGoldLight

/**
 * Aesthetic, professional login and registration screen for Kisan Market.
 * Strict credential verification against registered phone/email and saved passwords.
 */
@Composable
fun AuthScreen(
    onLogin: (emailOrPhone: String, password: String) -> Unit,
    onRegister: (name: String, email: String, phone: String, password: String, role: String, location: String) -> Unit,
    errorMessage: String? = null,
    isLoading: Boolean = false,
    onClearError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) } // 0 = Login, 1 = Register

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AgroGreenPrimary,
                        Color(0xFF0F3223),
                        AgroCanvasLight
                    ),
                    startY = 0f,
                    endY = 1200f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Hero Section (Emblem, Title & Tagline)
            item {
                AuthHeader()
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Auth Card Container (Login & Register Toggle with Forms)
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = AgroGreenPrimary.copy(alpha = 0.2f))
                        .testTag("auth_container_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Custom Segmented Tab Bar (Login vs Register)
                        AuthSegmentedTabs(
                            selectedTab = selectedTabIndex,
                            onTabSelected = {
                                selectedTabIndex = it
                                onClearError()
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Error Banner if authentication or registration fails
                        if (!errorMessage.isNullOrBlank()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF2F2),
                                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .testTag("auth_error_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF991B1B)
                                    )
                                }
                            }
                        }

                        // Animated Switch between Login and Register Forms
                        AnimatedContent(
                            targetState = selectedTabIndex,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> -width } + fadeOut()
                                    )
                                } else {
                                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> width } + fadeOut()
                                    )
                                }
                            },
                            label = "auth_form_transition"
                        ) { tab ->
                            if (tab == 0) {
                                LoginForm(
                                    isLoading = isLoading,
                                    onLoginSubmit = onLogin,
                                    onSwitchToRegister = {
                                        selectedTabIndex = 1
                                        onClearError()
                                    },
                                    onFieldChanged = onClearError
                                )
                            } else {
                                RegisterForm(
                                    isLoading = isLoading,
                                    onRegisterSubmit = onRegister,
                                    onSwitchToLogin = {
                                        selectedTabIndex = 0
                                        onClearError()
                                    },
                                    onFieldChanged = onClearError
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Trust & Security Guarantee Banner
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.90f))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = AgroGreenBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Secure APMC & Farm Direct Gateway • Zero Brokerage",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Top Header section with decorative Emblem and App Identity
 */
@Composable
private fun AuthHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // App Logo Emblem - Custom Circular Logo scaled to fit existing circular container without cropping or distortion
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(76.dp)
                .shadow(8.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.3f))
                .clip(CircleShape)
                .background(Color.White)
                .testTag("auth_custom_logo_container")
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_kisan_custom_logo),
                contentDescription = "Kisan Market Custom Circular Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .testTag("auth_custom_logo_image")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "KISAN MARKET",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "India's Direct Farm Gate & Mandi Network",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFD1FAE5),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Modern Segmented Tab switcher for Login / Register
 */
@Composable
private fun AuthSegmentedTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F5F9),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("auth_tab_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            // Login Tab
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedTab == 0) AgroGreenPrimary else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(0) }
                    )
                    .testTag("auth_tab_login")
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 14.5.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedTab == 0) Color.White else Color(0xFF64748B)
                )
            }

            // Register Tab
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedTab == 1) AgroGreenPrimary else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(1) }
                    )
                    .testTag("auth_tab_register")
            ) {
                Text(
                    text = "Register",
                    fontSize = 14.5.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedTab == 1) Color.White else Color(0xFF64748B)
                )
            }
        }
    }
}

/**
 * Login Form Component
 */
@Composable
private fun LoginForm(
    isLoading: Boolean,
    onLoginSubmit: (emailOrPhone: String, password: String) -> Unit,
    onSwitchToRegister: () -> Unit,
    onFieldChanged: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var identifier by rememberSaveable { mutableStateOf("") } // Email or Phone
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var identifierError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateAndSubmit() {
        var isValid = true

        if (identifier.trim().isBlank()) {
            identifierError = "Please enter registered Phone number or Email"
            isValid = false
        } else {
            identifierError = null
        }

        if (password.trim().isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else if (password.length < 4) {
            passwordError = "Password must be at least 4 characters"
            isValid = false
        } else {
            passwordError = null
        }

        if (isValid) {
            focusManager.clearFocus()
            onLoginSubmit(identifier.trim(), password)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AgroTextPrimary
        )

        Text(
            text = "Sign in using your registered mobile number / email and password",
            fontSize = 12.5.sp,
            color = AgroTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Phone or Email Input
        OutlinedTextField(
            value = identifier,
            onValueChange = {
                identifier = it
                if (identifierError != null) identifierError = null
                onFieldChanged()
            },
            label = { Text("Registered Mobile Number or Email") },
            placeholder = { Text("+91 98765 43210 or email@domain.com", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = if (identifier.any { it.isLetter() || it == '@' }) Icons.Default.Email else Icons.Default.Phone,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            isError = identifierError != null,
            supportingText = identifierError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("login_input_identifier")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
                onFieldChanged()
            },
            label = { Text("Password (Set at Registration)") },
            placeholder = { Text("Enter your account password", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF64748B)
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { validateAndSubmit() }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("login_input_password")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sign In Button
        Button(
            onClick = { validateAndSubmit() },
            enabled = !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AgroGreenPrimary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp))
                .testTag("login_submit_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Verifying Credentials...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(
                    text = "Sign In to Kisan Market",
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch to Register prompt
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "Register Free",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = AgroGreenPrimary,
                modifier = Modifier
                    .clickable(onClick = onSwitchToRegister)
                    .testTag("login_switch_to_register")
            )
        }
    }
}

/**
 * Register Form Component
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RegisterForm(
    isLoading: Boolean,
    onRegisterSubmit: (name: String, email: String, phone: String, password: String, role: String, location: String) -> Unit,
    onSwitchToLogin: () -> Unit,
    onFieldChanged: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var selectedRole by rememberSaveable { mutableStateOf("Farmer / Producer") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val roles = listOf(
        "Farmer / Producer",
        "Mandi Trader / Merchant",
        "Wholesale Buyer / Mill",
        "Retail Consumer"
    )

    fun validateAndRegister() {
        var isValid = true

        if (name.trim().isBlank()) {
            nameError = "Please enter your full name or farm enterprise"
            isValid = false
        } else {
            nameError = null
        }

        val cleanPhone = phone.trim()
        val digits = cleanPhone.filter { it.isDigit() }
        if (cleanPhone.isBlank()) {
            phoneError = "Mobile number is required"
            isValid = false
        } else if (digits.length < 10) {
            phoneError = "Please enter a valid 10-digit mobile number"
            isValid = false
        } else {
            phoneError = null
        }

        val cleanEmail = email.trim()
        if (cleanEmail.isNotBlank() && (!cleanEmail.contains("@") || !cleanEmail.contains("."))) {
            emailError = "Please enter a valid email address (e.g. name@gmail.com)"
            isValid = false
        } else {
            emailError = null
        }

        if (password.trim().isBlank()) {
            passwordError = "Create a password for your account"
            isValid = false
        } else if (password.length < 4) {
            passwordError = "Password must be at least 4 characters"
            isValid = false
        } else {
            passwordError = null
        }

        if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordError = null
        }

        if (isValid) {
            focusManager.clearFocus()
            onRegisterSubmit(
                name.trim(),
                cleanEmail,
                cleanPhone,
                password,
                selectedRole,
                location.trim()
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Free Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AgroTextPrimary
        )

        Text(
            text = "Your credentials and password will be securely saved for instant sign-in",
            fontSize = 12.5.sp,
            color = AgroTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Selector Chips
        Text(
            text = "Select Your Role:",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            roles.forEach { role ->
                val isSelected = selectedRole == role
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) AgroGreenPrimary else Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, if (isSelected) AgroGreenPrimary else Color(0xFFCBD5E1)),
                    modifier = Modifier
                        .clickable {
                            selectedRole = role
                            onFieldChanged()
                        }
                        .testTag("role_chip_${role.take(6).lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = role,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF334155)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Full Name Input
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                if (nameError != null) nameError = null
                onFieldChanged()
            },
            label = { Text("Full Name / Farm Enterprise *") },
            placeholder = { Text("e.g. Ramesh Patil", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            isError = nameError != null,
            supportingText = nameError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_name")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Mobile Phone Number
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                if (phoneError != null) phoneError = null
                onFieldChanged()
            },
            label = { Text("Mobile Number (Used to Login) *") },
            placeholder = { Text("+91 98765 43210", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            isError = phoneError != null,
            supportingText = phoneError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_phone")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Email Address
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) emailError = null
                onFieldChanged()
            },
            label = { Text("Email Address (Used to Login)") },
            placeholder = { Text("farmer@gmail.com", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_email")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Location / Village / City
        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
                onFieldChanged()
            },
            label = { Text("Village / Mandi / District") },
            placeholder = { Text("e.g. Nashik, Maharashtra", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_location")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Create Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
                onFieldChanged()
            },
            label = { Text("Create Password *") },
            placeholder = { Text("At least 4 characters", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF64748B)
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_password")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                if (confirmPasswordError != null) confirmPasswordError = null
                onFieldChanged()
            },
            label = { Text("Confirm Password *") },
            placeholder = { Text("Re-enter your password", color = AgroInputHintDark) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AgroGreenAccent
                )
            },
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF64748B)
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            supportingText = confirmPasswordError?.let { { Text(it, color = Color(0xFFDC2626)) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { validateAndRegister() }),
            shape = RoundedCornerShape(14.dp),
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
                .testTag("register_input_confirm_password")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Register Button
        Button(
            onClick = { validateAndRegister() },
            enabled = !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AgroGreenPrimary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp))
                .testTag("register_submit_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Creating Account...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(
                    text = "Complete Registration & Save Password",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch to Login prompt
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already registered? ",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "Sign In",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = AgroGreenPrimary,
                modifier = Modifier
                    .clickable(onClick = onSwitchToLogin)
                    .testTag("register_switch_to_login")
            )
        }
    }
}

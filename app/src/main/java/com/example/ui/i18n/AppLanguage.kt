package com.example.ui.i18n

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val scriptSample: String
) {
    ENGLISH("en", "English", "English", "Welcome"),
    HINDI("hi", "हिन्दी", "Hindi", "नमस्ते"),
    MARATHI("mr", "मराठी", "Marathi", "नमस्कार"),
    GUJARATI("gu", "ગુજરાતી", "Gujarati", "નમસ્તે"),
    PUNJABI("pa", "ਪੰਜਾਬੀ", "Punjabi", "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ"),
    BENGALI("bn", "বাংলা", "Bengali", "স্বাগতম"),
    TELUGU("te", "తెలుగు", "Telugu", "నమస్కారం"),
    TAMIL("ta", "தமிழ்", "Tamil", "வணக்கம்"),
    KANNADA("kn", "ಕನ್ನಡ", "Kannada", "ನಮಸ್ಕಾರ"),
    MALAYALAM("ml", "മലയാളം", "Malayalam", "നമസ്കാരം"),
    ODIA("or", "ଓଡ଼ିଆ", "Odia", "ନମସ୍କାର")
}

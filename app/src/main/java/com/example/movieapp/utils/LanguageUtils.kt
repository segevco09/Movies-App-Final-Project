package com.example.movieapp.utils

import java.util.Locale

object LanguageUtils {
    fun getDeviceLanguage(): String {
        return Locale.getDefault().toLanguageTag().lowercase().let { locale ->
            // Convert language tags to TMDB format (e.g., "he-IL" to "he")
            when {
                locale.startsWith("he") -> "he"  // Hebrew
                locale.startsWith("en") -> "en"  // English
                else -> "en"  // Default to English
            }
        }
    }
} 
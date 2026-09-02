package com.example.ui.screens.components

import com.example.data.model.CropListing

object ProducePhotoProvider {

    /**
     * Resolves the primary cover photo for cards, headers, and previews.
     */
    fun getPrimaryPhotoForListing(listing: CropListing): String {
        val userPrimary = listing.primaryPhoto?.takeIf { it.isNotBlank() }
        if (userPrimary != null) return userPrimary

        return getGalleryPhotosForListing(listing).firstOrNull() ?: ""
    }

    /**
     * Resolves a guaranteed list of at least 3 photos for the given listing.
     * Prioritizes user-uploaded photos from Firebase Storage / local cache,
     * and fills with realistic high-resolution agricultural produce images matching crop type and variety.
     */
    fun getGalleryPhotosForListing(listing: CropListing): List<String> {
        val userPhotos = listing.images.filter { it.isNotBlank() }
        if (userPhotos.size >= 3) {
            return userPhotos
        }

        val fallbackPhotos = getFallbackPhotosForCrop(listing.title, listing.variety, listing.category)

        val combined = mutableListOf<String>()
        combined.addAll(userPhotos)

        // Supplement remaining slots up to at least 3 photos
        for (fallback in fallbackPhotos) {
            if (combined.size >= 3) break
            if (!combined.contains(fallback)) {
                combined.add(fallback)
            }
        }

        // If still fewer than 3, duplicate/pad existing
        while (combined.size < 3 && combined.isNotEmpty()) {
            combined.add(combined.first())
        }

        return combined.ifEmpty {
            listOf(
                "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1542838132-92c53300491e?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1595855759920-86582396756a?w=800&auto=format&fit=crop&q=80"
            )
        }
    }

    private fun getFallbackPhotosForCrop(title: String, variety: String, category: String): List<String> {
        val lower = "${title.lowercase()} ${variety.lowercase()} ${category.lowercase()}"
        return when {
            lower.contains("onion") || lower.contains("garwa") || lower.contains("bhima") || lower.contains("pyaz") -> listOf(
                "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1618512496248-a07fe83aa8cb?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("tomato") || lower.contains("abhinav") || lower.contains("vaishali") || lower.contains("tamatar") -> listOf(
                "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1546470427-e26264be0b11?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1561136594-7f68413baa99?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("potato") || lower.contains("kufri") || lower.contains("jyoti") || lower.contains("aloo") -> listOf(
                "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1590165482129-1b8b27698780?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("chilli") || lower.contains("mirchi") || lower.contains("guntur") || lower.contains("teja") -> listOf(
                "https://images.unsplash.com/photo-1588252303782-cb80119abd6d?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1526346698789-224a79100823?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("pepper") || lower.contains("capsicum") || lower.contains("shimla") -> listOf(
                "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1525607551316-4a8e16d1f9ba?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("garlic") || lower.contains("lahsun") -> listOf(
                "https://images.unsplash.com/photo-1540148426945-6cf22a6b2383?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1584949591568-1934c114389d?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("ginger") || lower.contains("adrak") || lower.contains("turmeric") || lower.contains("haldi") -> listOf(
                "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1588600878108-578307a3cc9d?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("mango") || lower.contains("alphonso") || lower.contains("kesar") || lower.contains("dasheri") || lower.contains("aam") -> listOf(
                "https://images.unsplash.com/photo-1553279768-865429fa0078?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1591073113125-e46713c829ed?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1601493700631-2b16ec4b4716?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("banana") || lower.contains("kela") || lower.contains("g9") -> listOf(
                "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1543218024-57a70143c369?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1603833665858-e61d17a86224?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("apple") || lower.contains("seb") || lower.contains("kinnaur") -> listOf(
                "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1570913149827-d2ac84ab3f9a?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("pomegranate") || lower.contains("anar") || lower.contains("bhagwa") -> listOf(
                "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1541344999736-83eca872f241?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1595855759920-86582396756a?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("wheat") || lower.contains("sharbati") || lower.contains("lokwan") || lower.contains("gehun") -> listOf(
                "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1530595467537-0b5996c41f2d?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("rice") || lower.contains("basmati") || lower.contains("chawal") || lower.contains("paddy") -> listOf(
                "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1536304993881-ff6e9eefa2a6?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("soybean") || lower.contains("gram") || lower.contains("chana") || lower.contains("dal") || lower.contains("pulses") || category == "Pulses" -> listOf(
                "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1515543237350-b3eea1ec8082?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1508747703725-719777637510?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("fruit") -> listOf(
                "https://images.unsplash.com/photo-1619566636858-adf3ef46400b?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1553279768-865429fa0078?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1601493700631-2b16ec4b4716?w=800&auto=format&fit=crop&q=80"
            )
            lower.contains("vegetable") -> listOf(
                "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800&auto=format&fit=crop&q=80"
            )
            else -> listOf(
                "https://images.unsplash.com/photo-1615485290382-441e4d049cb5?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1542838132-92c53300491e?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1595855759920-86582396756a?w=800&auto=format&fit=crop&q=80"
            )
        }
    }
}

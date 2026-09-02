package com.example.data.model

data class MandiRegion(
    val id: String,
    val name: String,
    val district: String,
    val state: String,
    val city: String = district,
    val majorCommodities: List<String>,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isDefault: Boolean = false
) {
    val displayName: String get() = "$name, $district"

    fun matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val terms = query.trim().lowercase().split("\\s+".toRegex()).filter { it.isNotBlank() }
        val searchableString = buildString {
            append(name.lowercase()).append(" ")
            append(city.lowercase()).append(" ")
            append(district.lowercase()).append(" ")
            append(state.lowercase()).append(" ")
            majorCommodities.forEach { append(it.lowercase()).append(" ") }
        }
        return terms.all { term -> searchableString.contains(term) }
    }
}

package com.hika.data.model

data class Perfume(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val category: String = "",
    val description: String = "",
    val strength: Int = 0,
    val price: Int = 0,
    val rating: Double = 0.0,
    @field:JvmField
    val isAvailable: Boolean = false
)

data class PerfumeBody(
    val name: String = "",
    val image: String = "",
    val category: String = "",
    val description: String = "",
    val strength: Int = 0,
    val price: Int = 0,
    @field:JvmField
    val isAvailable: Boolean = false
)

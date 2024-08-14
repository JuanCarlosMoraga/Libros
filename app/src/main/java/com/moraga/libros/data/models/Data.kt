package com.moraga.libros.data.models

class Data (
    val id: Int,
    val name: String,
    val author: String,
    val year_of_publication: String,
    val description: String,
    val category: String,
    val image: String,
    val created_at: String? = null,
    val updated_at: String? = null
)
package com.cookingassistant.data.DTO

data class ReviewPostDTO(
    val value: Int,
    val description: String?
)

data class ReviewGetDTO(
    val id: Int,
    val value: Int,
    val description: String?,
    val reviewAuthor: String?,
    val dateCreated: String,
    val dateModified: String?
)
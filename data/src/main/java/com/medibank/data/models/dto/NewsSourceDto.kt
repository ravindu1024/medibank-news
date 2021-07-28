package com.medibank.data.models.dto

import com.medibank.data.models.domain.NewsSource

data class NewsSourceResponseDto(
    val status: String,
    val sources: List<NewsSourceDto>
)

data class NewsSourceDto(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String
)


fun NewsSourceResponseDto.toDomain(): List<NewsSource>{
    return this.sources.map {
        NewsSource(
            id = it.id,
            name = it.name,
            description = it.description
        )
    }
}
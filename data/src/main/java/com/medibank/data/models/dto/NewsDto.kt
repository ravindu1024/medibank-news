package com.medibank.data.models.dto

import com.medibank.data.models.domain.NewsHeadline

data class NewsResponseDto(
    val status: String,
    val articles: List<NewsDto>
)


data class NewsDto(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val source: NewsHeadlineSourceDto
)

data class NewsHeadlineSourceDto(
    val name: String
)

fun NewsResponseDto.toDomain(): List<NewsHeadline> {
    return this.articles.map {
        return@map NewsHeadline(
            author = it.author ?: "",
            sourceName = it.source.name,
            title = it.title,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}

package com.medibank.data.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsHeadline(
    val author: String,
    val sourceName: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String
): Parcelable
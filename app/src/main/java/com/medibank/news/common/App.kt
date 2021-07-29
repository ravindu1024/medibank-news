package com.medibank.news.common

import android.app.Application
import android.content.Context
import com.medibank.data.api.NewsApi
import com.medibank.data.api.RequestInterceptor
import com.medibank.data.api.ResponseInterceptor
import com.medibank.data.api.RetrofitClient
import com.medibank.data.usecases.AppPreferences
import com.medibank.data.usecases.news.GetNewsUseCase
import com.medibank.data.usecases.saveditems.SavedItemsUseCase
import com.medibank.data.usecases.sources.GetSourcesUseCase
import com.medibank.news.R

class App : Application() {

    lateinit var getSourcesUseCase: GetSourcesUseCase
    lateinit var getNewsUseCase: GetNewsUseCase
    lateinit var appPreferences: AppPreferences
    lateinit var savedItemsUseCase: SavedItemsUseCase

    override fun onCreate() {
        super.onCreate()

        // Manual DI since I'm not using Dagger for this project

        val retrofit = RetrofitClient.init(
            getString(R.string.base_url),
            RequestInterceptor(apiKey = getString(R.string.news_api_key)),
            ResponseInterceptor()
        )
        appPreferences = AppPreferences(getSharedPreferences("default", Context.MODE_PRIVATE))

        getSourcesUseCase = GetSourcesUseCase(retrofit.create(NewsApi::class.java), appPreferences)
        getNewsUseCase = GetNewsUseCase(retrofit.create(NewsApi::class.java), appPreferences)
        savedItemsUseCase = SavedItemsUseCase(appPreferences)
    }

}
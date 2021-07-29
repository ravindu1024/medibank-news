package com.medibank.news

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.medibank.data.api.NewsApi
import com.medibank.data.models.dto.NewsSourceDto
import com.medibank.data.models.dto.NewsSourceResponseDto
import com.medibank.data.usecases.AppPreferences
import com.medibank.data.usecases.sources.GetSourcesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class SourcesUseCaseTest {

    private val prefs = mockk<AppPreferences>()
    private val api = mockk<NewsApi>()
    private lateinit var useCase: GetSourcesUseCase

    @Rule
    @JvmField
    var rx2OverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        useCase = GetSourcesUseCase(api, prefs)
    }

    @Test
    fun `get sources - saved sources`(){
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        val s1 = NewsSourceDto(id = "abc-news", "", "", "", "")
        val s2 = NewsSourceDto(id = "cnn", "", "", "", "")
        val s3 = NewsSourceDto(id = "bbc", "", "", "", "")
        val response = NewsSourceResponseDto(status = "ok", sources = listOf(s1, s2, s3))

        every { prefs.getSavedSources() } returns listOf("abc-news")
        every { api.getSources(any(), any()) } returns Single.just(response)

        val t = useCase.getAllSources().test()
        t.await(5, TimeUnit.SECONDS)

        val sources = t.values().first()
        Assert.assertEquals(sources.size, 3)
        Assert.assertTrue(sources.first { it.id == "abc-news" }.saved)
        Assert.assertFalse(sources.first { it.id == "cnn" }.saved)
    }
}

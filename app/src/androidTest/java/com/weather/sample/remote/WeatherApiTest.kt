package com.weather.sample.remote

import com.sample.core.data.api.ApiServices
import com.sample.core.data.remote.GsonProvider
import com.sample.core.domain.entity.WeatherCityResponse
import com.weather.sample.utils.CoroutineTestRule
import com.weather.sample.utils.MockResponseFileReader
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class WeatherApiTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private var mockWebServer = MockWebServer()
    private lateinit var weatherApi: ApiServices

    @Before
    fun setUp() {
        mockWebServer.start()
        weatherApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create(GsonProvider().instance)
            )
            .create(ApiServices::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetch lat lon response is successful`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("response.json").content)

        mockWebServer.enqueue(response)
        runBlocking(coroutineTestRule.testDispatcher) {
            val weatherResponse: Single<WeatherCityResponse> =
                weatherApi.getWeatherByLatLon(
                    1.0.toDouble(),
                    2.0.toDouble(),
                    "098cf02493bef97c9207ef2daefddd34"
                )
            Truth.assertThat(weatherResponse.isSuccessful).isTrue()
        }
    }

    @Test
    fun `fetch weather ciy response body has desired num_results`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("response.json").content)

        mockWebServer.enqueue(response)
        runBlocking(coroutineTestRule.testDispatcher) {
            val weatherResponse: Single<WeatherCityResponse> =
                weatherApi.getWeatherByCityName("dubai", "098cf02493bef97c9207ef2daefddd34")
            Truth.assertThat(weatherResponse.body()?.numResults).isEqualTo(5)
        }
    }
}
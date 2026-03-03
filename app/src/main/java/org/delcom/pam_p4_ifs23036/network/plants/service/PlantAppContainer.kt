package org.delcom.pam_p4_ifs23036.network.plants.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import org.delcom.pam_p4_ifs23036.BuildConfig
import java.util.concurrent.TimeUnit

class PlantAppContainer: IPlantAppContainer {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.HEADERS // Changed from BODY to HEADERS for speed in debug
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
        // Optimized Timeouts: Faster detection of connection issues
        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_PANTS_API)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    private val retrofitService: PlantApiService by lazy {
        retrofit.create(PlantApiService::class.java)
    }

    private val movieRetrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    override val plantRepository: IPlantRepository by lazy {
        PlantRepository(retrofitService)
    }

    override val movieRepository: IMovieRepository by lazy {
        MovieRepository(movieRetrofitService)
    }
}

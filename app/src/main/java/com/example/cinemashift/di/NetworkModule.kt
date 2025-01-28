package com.example.cinemashift.di

import android.util.Log
import com.example.cinemashift.data.api.CinemaApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .build()

                Log.d("NetworkModule", "Request URL: ${request.url}")
                val response = chain.proceed(request)
                Log.d("NetworkModule", "Response code: ${response.code}")
                Log.d("NetworkModule", "Response headers: ${response.headers}")

                response
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://shift-intensive.ru/api/") // добавим /api/ в базовый URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideCinemaApiService(retrofit: Retrofit): CinemaApiService {
        return retrofit.create(CinemaApiService::class.java)
    }
}
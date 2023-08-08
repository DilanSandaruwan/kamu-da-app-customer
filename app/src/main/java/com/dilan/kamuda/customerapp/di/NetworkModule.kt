package com.dilan.kamuda.customerapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the converter factory for JSON parsing.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory{
        return GsonConverterFactory.create()
    }

    /**
     * Provides the HTTP client for making network requests.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.build()
    }

    /**
     * Provides the Retrofit instance for API communication.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param baseUrl The base URL for the API.
     * @param converterFactory The converter factory for JSON parsing.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl:String,
        converterFactory: Converter.Factory
    ):Retrofit{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client((okHttpClient))

        return retrofit.build()
    }

}
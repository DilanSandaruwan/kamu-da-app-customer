package com.dilan.kamuda.customerapp.di

import com.dilan.kamuda.customerapp.constant.NetworkConstant.BASE_URL
import com.dilan.kamuda.customerapp.network.OnBoardingApiService
import com.dilan.kamuda.customerapp.network.OrderApiService
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

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
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
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

    @Singleton
    @Provides
    fun provideOnBoardingApiService(retrofit: Retrofit): OnBoardingApiService {
        return retrofit.create(OnBoardingApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(onBoardingApiService: OnBoardingApiService,orderApiService: OrderApiService): MainRepository {
        return MainRepository(onBoardingApiService,orderApiService)
    }

}
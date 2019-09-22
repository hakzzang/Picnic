package hbs.com.picnic.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


//Singleton
object RetrofitProvider {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val httpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    private fun makeRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    fun provideMapApi(baseUrl: String): MapAPI = RetrofitProvider.makeRetrofit(baseUrl).create(MapAPI::class.java)
    fun provideFcmApi(baseUrl: String): FcmAPI = RetrofitProvider.makeRetrofit(baseUrl).create(FcmAPI::class.java)
    fun provideTourApi(baseUrl:String) :TourAPI = RetrofitProvider.makeRetrofit(baseUrl).create(TourAPI::class.java)
}
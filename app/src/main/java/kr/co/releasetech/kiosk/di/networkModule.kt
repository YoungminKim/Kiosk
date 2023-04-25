package kr.co.releasetech.kiosk.di

import com.google.gson.GsonBuilder
import kr.co.releasetech.kiosk.http.UrlInfo.URL_HEADER
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L


val networkModule = module {

    single { GsonBuilder().create() }


    single {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            /*addInterceptor(Interceptor { chain ->
                chain.proceed(chain.request().newBuilder().apply {
                    header("Accept", "application/vnd.github.mercy-preview+json")
                }.build())
            })*/
            addInterceptor(HttpLoggingInterceptor().apply {
                /*if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }*/
            })
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(URL_HEADER)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }


}
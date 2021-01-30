package andrefigas.com.github.pokemon.injection.modules

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Module
class NetworkModule @Inject constructor() {

    companion object{

        const val TAG : String = "NetworkModule"
        const val CACHE_DIRECTORY = "responses"
        const val CACHE_SIZE = 10L * 1024 * 1024
    }

    private lateinit var httpClient: OkHttpClient

    private lateinit var apiClient: ApiClient

    @Singleton
    @Provides
    fun providesHttpClient(context: Context): OkHttpClient {

        val httpCacheDirectory = File(context.getCacheDir(), CACHE_DIRECTORY)

        var cache: Cache? = null
        try {
            cache = Cache(httpCacheDirectory, CACHE_SIZE)
        } catch (e: IOException) {
            Log.e(TAG, "Could not create http cache", e)
        }

        httpClient = OkHttpClient.Builder().cache(cache).build()

        return httpClient
    }

    @Singleton
    @Provides
    fun provideApiClient(context: Context): ApiClient {
        apiClient = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(providesHttpClient(context))
            .build()
            .create(ApiClient::class.java)
        return apiClient
    }


}
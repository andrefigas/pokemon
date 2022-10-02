package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


abstract class Repository(val context: Context, map: Map<String, Class<out ApiClient>>) {

    companion object {
        const val CACHE_DIRECTORY = "responses"
        const val CACHE_SIZE = 10L * 1024 * 1024
    }

    protected abstract fun processClient(api: ApiClient)

    init {

        map.forEach {
            processClient(initializeClient(it.key, it.value))
        }
    }

    private fun initializeClient(url : String, interfaceClass : Class<out ApiClient>) = createApiClient(
        url,
        interfaceClass,
        createHttpClient(
            createCache(
                createCacheFile(context)
            )
        )
    )

    private fun createCacheFile(context: Context) = File(context.cacheDir, CACHE_DIRECTORY)

    private fun createCache(file: File) = Cache(file, CACHE_SIZE)

    private fun createHttpClient(cache: Cache) = OkHttpClient.Builder().cache(cache).build()

    private fun createApiClient(url : String, interfaceClass : Class<out ApiClient>, httpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build().create(interfaceClass)

}
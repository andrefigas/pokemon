package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import android.content.Context
import android.graphics.BitmapFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result.response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


abstract class Repository(val context: Context, val map: Map<String, Class<out ApiClient>>) {

    companion object {
        const val CACHE_DIRECTORY = "responses"
        const val CACHE_SIZE = 10L * 1024 * 1024
    }

    //abstract val interfaceClass: Class<T ou>
    //protected val serviceClient: T

    abstract fun processClient(api: ApiClient)

    init {
        //serviceClient = initializeClient(url, interfaceClass)

        map.forEach {
            processClient(initializeClient(it.key, it.value))
        }
    }

    protected fun initializeClient(url : String, interfaceClass : Class<out ApiClient>) = createApiClient(
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

    private fun createHttpClient(cache: Cache) = OkHttpClient.Builder().addInterceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()
        val request = requestBuilder.build()
        val result = chain.proceed(request)
        if(request.url.toString().contains(".svg")){
            //val data = android.util.Base64.encodeToString(?.bytes(), android.util.Base64.DEFAULT)

        }
        result
    }.cache(cache).build()

    private fun createApiClient(url : String, interfaceClass : Class<out ApiClient>, httpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build().create(interfaceClass)

}
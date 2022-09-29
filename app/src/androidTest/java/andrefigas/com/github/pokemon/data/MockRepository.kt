package andrefigas.com.github.pokemon.data

import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


abstract class MockRepository(map: Map<Class<out ApiClient>, List<String>>) {

    companion object {
        const val ASSET_BASE_PATH = "../app/src/test/assets/"
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

    private fun initializeClient(interfaceClass : Class<out ApiClient>, fileNames : List<String>) = createApiClient(
        interfaceClass,
        createHttpClient(fileNames)
    )

    private fun createHttpClient(fileNames : List<String>) = OkHttpClient.Builder()
        .addInterceptor(MockInterceptor(fileNames)).build()

    private fun createApiClient(interfaceClass : Class<out ApiClient>, httpClient: OkHttpClient): ApiClient {

        return Retrofit.Builder()
            .baseUrl("http://127.0.0.1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build().create(interfaceClass)
    }

}

class MockInterceptor(private val fileNames: List<String>) : Interceptor{

    private var iterable = -1

    override fun intercept(chain: Interceptor.Chain): Response {
        iterable++
        val responseString = readJsonFile(fileNames[iterable])

        return Response.Builder()
            .code(200)
            .message("success")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()

    }

    private fun readJsonFile(filename: String) = this::class.java
        .classLoader
        .getResource(filename)
        .readText()

}

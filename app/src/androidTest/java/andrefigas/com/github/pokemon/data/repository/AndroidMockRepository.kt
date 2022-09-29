package andrefigas.com.github.pokemon.data.repository

import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException


abstract class MockRepository(apis: List<Class<out ApiClient>>, val mapping: Map<String, String>) {

    companion object{
        const val DEFAULT_URL = "http://127.0.0.1/"
    }

    abstract fun processClient(api: ApiClient)

    init {

        apis.forEach {
            processClient(initializeClient(it, mapping))
        }
    }

    private fun initializeClient(interfaceClass: Class<out ApiClient>, mapping: Map<String, String>) = createApiClient(
        interfaceClass,
        createHttpClient(mapping)
    )

    private fun createHttpClient(mapping: Map<String, String>) = OkHttpClient.Builder()
        .addInterceptor(MockInterceptor(mapping)).build()

    private fun createApiClient(interfaceClass : Class<out ApiClient>, httpClient: OkHttpClient): ApiClient {

        return Retrofit.Builder()
            .baseUrl(DEFAULT_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build().create(interfaceClass)
    }

}

class MockInterceptor(val mapping: Map<String, String>) : Interceptor{


    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url.toString()
        val filename = mapping[url] ?: throw IllegalArgumentException("$url not mapped")

        val responseString = readJsonFile(filename)

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

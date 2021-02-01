package andrefigas.com.github.pokemon

import andrefigas.com.github.pokemon.data.DataTest
import andrefigas.com.github.pokemon.injection.modules.NetworkModule
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.entities.Pokemon
import android.content.Context
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockApiClient(val bodies : List<String>) : NetworkModule() {


    val webServer by lazy {
        val server = MockWebServer()
        server.start()

        bodies.forEach{
            server.enqueue(
                MockResponse().setBody(it)
            )
        }

        server
    }


    override fun provideApiClient(
        context: Context?,
        url: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(webServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




}
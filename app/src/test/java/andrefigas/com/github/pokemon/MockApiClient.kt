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

class MockApiClient : NetworkModule() {

    companion object {

        val NULL: String = "null"

    }

    val webServer by lazy {
        val server = MockWebServer()
        server.start()

        server.enqueue(
            MockResponse().setBody(
                providePage(offset = 0, limit = 1, results = listOf(DataTest.BULBASAUR_ENTITY))
            )
        )
        server.enqueue(
            MockResponse().setBody(
                providePokemon(DataTest.BULBASAUR)
            )
        )

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

    private fun providePage(
        count: Int = Int.MAX_VALUE,
        offset: Int,
        limit: Int = 1,
        results: List<BaseEntity>
    ): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"count\": $count,\n")

        val next = offset + 1
        val nextStr =
            if (next >= limit) NULL else "https://pokeapi.co/api/v2/pokemon?offset=$next&limit=$limit"

        sb.append(" \"next\": \"$nextStr\",\n")

        val previous = offset - 1
        val previousStr =
            if (previous < 0) NULL else "https://pokeapi.co/api/v2/pokemon?offset=$previous&limit=$limit"

        sb.append("  \"previous\": \"$previousStr\",\n")
        sb.append("  \"results\": [\n")

        results.forEach {
            sb.append("    {\n")
            sb.append("      \"name\": \"${it.name}\",\n")
            sb.append("      \"url\": \"${it.url}\"\n")
            sb.append("    }\n")
        }

        sb.append("  ]\n")
        sb.append("}")
        return sb.toString()
    }

    fun providePokemon(pokemon: Pokemon): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("      \"name\": \"${pokemon.name}\",\n")
        sb.append("      \"weight\": \"${pokemon.weight}\",\n")
        sb.append("      \"height\": \"${pokemon.height}\",\n")
        sb.append("      \"id\": \"${pokemon.id}\"")
        sb.append("}\n")

        return sb.toString()
    }


}
package andrefigas.com.github.pokemon

import andrefigas.com.github.pokemon.model.entities.BaseEntity
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import io.reactivex.functions.Predicate
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiClientTest {

    companion object {

        val NULL: String = "null"

    }

    fun provideApiClient(server: MockWebServer) = Retrofit.Builder()
        .baseUrl(server.url("/"))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiClient::class.java)


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

    @Test
    fun initialLoad() {
        val server = MockWebServer()
        server.start()

        val bulbasaur: BaseEntity = BaseEntity(
            "bulbasaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )

        server.enqueue(
            MockResponse().setBody(
                providePage(offset = 0, limit = 1, results = listOf(bulbasaur))
            )
        )

        provideApiClient(server).fetchPokemons("1", "0").test().assertValue(Predicate {
            it.results.size == 1 && it.results.first() == bulbasaur
        })
    }

    @Test
    fun paginationNext() {
        val server = MockWebServer()
        server.start()

        val bulbasaur: BaseEntity = BaseEntity(
            "bulbasaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )

        server.enqueue(
            MockResponse().setBody(
                providePage(offset = 0, limit = 1, results = listOf(bulbasaur))
            )
        )

        val ivysaur: BaseEntity = BaseEntity(
            "ivysaur",
            "https://pokeapi.co/api/v2/pokemon/2/"
        )

        server.enqueue(
            MockResponse().setBody(
                providePage(offset = 0, limit = 1, results = listOf(ivysaur))
            )
        )

        provideApiClient(server).fetchPokemons("1", "0").flatMap {
            provideApiClient(server).fetchPokemons(it.next)
        }.test().assertValue(Predicate {
            it.results.size == 1 && it.results.first() == ivysaur
        })
    }

    @Test
    fun paginationEnd() {
        val server = MockWebServer()
        server.start()

        val bulbasaur: BaseEntity = BaseEntity(
            "bulbasaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )

        server.enqueue(
            MockResponse().setBody(
                providePage(count = 1, offset = 0, limit = 1, results = listOf(bulbasaur))
            )
        )

        provideApiClient(server).fetchPokemons("1", "0").test().assertValue(Predicate {
            it.next == NULL
        })
    }

}
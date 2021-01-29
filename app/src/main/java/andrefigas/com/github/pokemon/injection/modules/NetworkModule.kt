package andrefigas.com.github.pokemon.injection.modules

import andrefigas.com.github.pokemon.BuildConfig
import andrefigas.com.github.pokemon.model.repository.api.ApiClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
class NetworkModule @Inject constructor() {

    private lateinit var httpClient: OkHttpClient

    private lateinit var apiClient: ApiClient

    @Singleton
    @Provides
    fun providesHttpClient(): OkHttpClient {
        httpClient = OkHttpClient
            .Builder()
            .build()

        return httpClient
    }

    @Singleton
    @Provides
    fun provideApiClient(): ApiClient {
        apiClient = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(providesHttpClient())
            .build()
            .create(ApiClient::class.java)
        return apiClient
    }


}
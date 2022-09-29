package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.domain.entities.Page
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import com.google.gson.annotations.SerializedName

open class ResultPage<T : BaseEntity>(
    next: String,
    previous: String,
    @SerializedName("results") val results: Array<T>
) : Page(next, previous)
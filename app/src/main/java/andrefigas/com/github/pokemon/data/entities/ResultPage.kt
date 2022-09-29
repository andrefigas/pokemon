package andrefigas.com.github.pokemon.data.entities

import andrefigas.com.github.pokemon.Page
import andrefigas.com.github.pokemon.domain.entities.BaseEntity
import com.google.gson.annotations.SerializedName

open class ResultPage<T : BaseEntity>(
    count: Int,
    next: String,
    previous: String,
    @SerializedName("results") val results: Array<T>
) : Page(count, next, previous)
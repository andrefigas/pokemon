package andrefigas.com.github.pokemon.domain.entities

import com.google.gson.annotations.SerializedName

open class Page(

    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String
)
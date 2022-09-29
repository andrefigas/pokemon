package andrefigas.com.github.pokemon.data.entities

import com.google.gson.annotations.SerializedName

data class FavoritePokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("favorite") val favorite: Boolean
)
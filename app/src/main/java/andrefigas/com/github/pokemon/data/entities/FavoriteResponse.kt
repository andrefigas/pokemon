package andrefigas.com.github.pokemon.data.entities

import com.google.gson.annotations.SerializedName

class FavoriteResponse(@SerializedName("favorite") val favorite: Boolean)
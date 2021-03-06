package andrefigas.com.github.pokemon.model.entities

import com.google.gson.annotations.SerializedName

class UpdateFavoriteResponse(
    @SerializedName("message") val message: String,
    var favorite: Boolean,
    var error: Boolean = false
)
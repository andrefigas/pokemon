package andrefigas.com.github.pokemon.model.entities

import com.google.gson.annotations.SerializedName

data class OtherImage(
    @SerializedName("dream_world") val dreamWorld : Sprite?,
    @SerializedName("official-artwork") val officialArtwork : Sprite?
)
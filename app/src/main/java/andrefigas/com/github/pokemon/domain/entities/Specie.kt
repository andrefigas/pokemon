package andrefigas.com.github.pokemon.domain.entities

import com.google.gson.annotations.SerializedName

data class Specie(
    @SerializedName("flavor_text_entries") val labels: List<FlavorText>,
    @SerializedName("habitat") var habitat: BaseEntity? = null
)
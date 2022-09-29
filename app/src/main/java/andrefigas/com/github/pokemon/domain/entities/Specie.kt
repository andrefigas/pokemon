package andrefigas.com.github.pokemon.domain.entities

import com.google.gson.annotations.SerializedName

data class Specie(
    @SerializedName("flavor_text_entries") val labels: Array<FlavorText>,
    @SerializedName("habitat") var habitat: BaseEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Specie

        if (!labels.contentEquals(other.labels)) return false
        if (habitat != other.habitat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = labels.contentHashCode()
        result = 31 * result + (habitat.hashCode() ?: 0)
        return result
    }
}
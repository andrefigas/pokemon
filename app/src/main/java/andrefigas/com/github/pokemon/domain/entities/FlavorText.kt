package andrefigas.com.github.pokemon.domain.entities

import com.google.gson.annotations.SerializedName

class FlavorText {
    @SerializedName("flavor_text")
    var label: String? = null
    @SerializedName("language")
    var language: BaseEntity? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlavorText

        if (label != other.label) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = label?.hashCode() ?: 0
        result = 31 * result + (language?.hashCode() ?: 0)
        return result
    }


}
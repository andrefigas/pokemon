package andrefigas.com.github.pokemon.domain.entities

import com.google.gson.annotations.SerializedName

data class FlavorText(@SerializedName("flavor_text")
                 val label: String,
                 @SerializedName("language")
var language: BaseEntity) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlavorText

        if (label != other.label) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + language?.hashCode()
        return result
    }


}
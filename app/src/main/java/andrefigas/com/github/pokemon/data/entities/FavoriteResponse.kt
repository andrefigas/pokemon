package andrefigas.com.github.pokemon.data.entities

import com.google.gson.annotations.SerializedName

class FavoriteResponse(@SerializedName("favorite") val favorite: Boolean){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FavoriteResponse

        if (favorite != other.favorite) return false

        return true
    }

    override fun hashCode(): Int {
        return favorite.hashCode()
    }
}
package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Pokemon(name: String, url: String) : BaseEntity(name, url), Parcelable {

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("weight")
    var weight: Int = 0
    @SerializedName("height")
    var height: Int = 0
    @SerializedName("sprites")
    var spritesCollection: SpritesCollection? = null
    @SerializedName("species")
    var species: BaseEntity? = null
    @SerializedName("types")
    var types: Array<Type>? = null
    @SerializedName("moves")
    var moves: Array<Move>? = null

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
        id = parcel.readInt()
        weight = parcel.readInt()
        height = parcel.readInt()
        spritesCollection = parcel.readParcelable(SpritesCollection::class.java.classLoader)
        species = parcel.readParcelable(BaseEntity::class.java.classLoader)
        types = parcel.readParcelableArray(Type::class.java.classLoader)?.map { it as Type }
            ?.toTypedArray()
        moves = parcel.readParcelableArray(Move::class.java.classLoader)?.map { it as Move }
            ?.toTypedArray()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeInt(id)
        parcel.writeInt(weight)
        parcel.writeInt(height)
        parcel.writeParcelable(spritesCollection, flags)
        parcel.writeParcelable(species, flags)
        parcel.writeParcelableArray(types, flags)
        parcel.writeParcelableArray(moves, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Pokemon

        if (id != other.id) return false
        if (weight != other.weight) return false
        if (height != other.height) return false
        if (spritesCollection != other.spritesCollection) return false
        if (species != other.species) return false

        val finalTypes = types
        val finalOtherTypes = other.types

        if (finalTypes != null) {
            if (finalOtherTypes == null) return false
            if (!finalTypes.contentEquals(finalOtherTypes)) return false
        } else if (finalOtherTypes!= null) {
            return false
        }

        val finalMoves = moves
        val finalOtherMoves = other.moves

        if (finalMoves != null) {
            if (finalOtherMoves == null) return false
            if (!finalMoves.contentEquals(finalOtherMoves)) return false
        } else if (finalOtherMoves!= null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + weight
        result = 31 * result + height
        result = 31 * result + (spritesCollection?.hashCode() ?: 0)
        result = 31 * result + (species?.hashCode() ?: 0)
        result = 31 * result + (types?.contentHashCode() ?: 0)
        result = 31 * result + (moves?.contentHashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }



}
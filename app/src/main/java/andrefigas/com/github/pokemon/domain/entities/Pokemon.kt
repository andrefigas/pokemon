package andrefigas.com.github.pokemon.domain.entities

import andrefigas.com.github.pokemon.utils.readParcelable
import andrefigas.com.github.pokemon.utils.readParcelableArray
import andrefigas.com.github.pokemon.utils.readStringOrEmpty
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Pokemon(override val name: String,override val url: String, @SerializedName("id")
val id: Int = 0, @SerializedName("weight")
val weight: Int = 0,@SerializedName("height")
val height: Int = 0, @SerializedName("sprites")
val spritesCollection: SpritesCollection,
              @SerializedName("species")
              val species: BaseEntity,
              @SerializedName("types")
              val types: Array<Type>,
              @SerializedName("moves")
              val moves: Array<Move>) : BaseEntityContract(), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(SpritesCollection::class.java),
        parcel.readParcelable(BaseEntity::class.java),
        parcel.readParcelableArray(Type::class.java),
        parcel.readParcelableArray(Move::class.java)
    )

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

         if (!types.contentEquals(other.types)) return false

         if (!moves.contentEquals(other.moves)) return false

         return true
     }

     override fun hashCode(): Int {
         var result = id
         result = 31 * result + weight
         result = 31 * result + height
         result = 31 * result + spritesCollection.hashCode()
         result = 31 * result + (species.hashCode())
         result = 31 * result + types.contentHashCode()
         result = 31 * result + moves.contentHashCode()
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
package andrefigas.com.github.pokemon.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Pokemon(name : String, url : String) : BaseEntity(name, url), Parcelable {

    @SerializedName("weight") var weight : Int = 0
    @SerializedName("height") var height : Int = 0
    @SerializedName("sprites") var spritesCollection : SpritesCollection? = null
    @SerializedName("species") var species : BaseEntity? = null
    @SerializedName("types") var types : Array<Type>? = null
    @SerializedName("moves") var moves : Array<Move>? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
        weight = parcel.readInt()
        height = parcel.readInt()
        spritesCollection = parcel.readParcelable(SpritesCollection::class.java.classLoader)
        species = parcel.readParcelable(BaseEntity::class.java.classLoader)
        types = parcel.readParcelableArray(Type::class.java.classLoader)?.map{ it as Type }?.toTypedArray()
        moves = parcel.readParcelableArray(Move::class.java.classLoader)?.map{ it as Move }?.toTypedArray()
    }

    constructor(name : String, url : String, weight : Int, height : Int, spritesCollection : SpritesCollection?,
                species : BaseEntity?, types : Array<Type>?, moves : Array<Move>? = null) : this(name, url){
        this.weight = weight
        this.height = height
        this.spritesCollection = spritesCollection
        this.species = species
        this.types = types
        this.moves = moves
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
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

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }

}
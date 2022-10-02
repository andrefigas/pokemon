package andrefigas.com.github.pokemon.view.entities

import andrefigas.com.github.pokemon.utils.readStringOrEmpty
import android.os.Parcel
import android.os.Parcelable


class PokemonDetailsUI(name: String,
                       weight: Int,
                       height: Int,
                       types: String,
                       moves: String,
                       val description : String,
                       val habitat: String,
                       val favorite: Boolean) : PokemonUI(name, weight, height, types, moves) {
    constructor(parcel: Parcel) : this(
        parcel.readStringOrEmpty(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(description)
        parcel.writeString(habitat)
        parcel.writeByte(if (favorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonDetailsUI> {
        override fun createFromParcel(parcel: Parcel): PokemonDetailsUI {
            return PokemonDetailsUI(parcel)
        }

        override fun newArray(size: Int): Array<PokemonDetailsUI?> {
            return arrayOfNulls(size)
        }
    }
}
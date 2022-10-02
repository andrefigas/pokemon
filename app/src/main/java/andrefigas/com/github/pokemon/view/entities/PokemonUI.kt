package andrefigas.com.github.pokemon.view.entities

import andrefigas.com.github.pokemon.utils.readStringOrEmpty
import android.os.Parcel
import android.os.Parcelable

open class PokemonUI(name: String,
                     val weight: Int = 0,
                     val height: Int = 0,
                     val types : String,
                     val moves: String) : BaseEntity(name) {
    constructor(parcel: Parcel) : this(
        parcel.readStringOrEmpty(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(weight)
        parcel.writeInt(height)
        parcel.writeString(types)
        parcel.writeString(moves)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonUI> {
        override fun createFromParcel(parcel: Parcel): PokemonUI {
            return PokemonUI(parcel)
        }

        override fun newArray(size: Int): Array<PokemonUI?> {
            return arrayOfNulls(size)
        }
    }
}

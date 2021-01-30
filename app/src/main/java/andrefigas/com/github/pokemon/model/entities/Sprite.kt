package andrefigas.com.github.pokemon.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Sprite (@SerializedName("front_default")  val image : String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sprite> {
        override fun createFromParcel(parcel: Parcel): Sprite {
            return Sprite(parcel)
        }

        override fun newArray(size: Int): Array<Sprite?> {
            return arrayOfNulls(size)
        }
    }
}
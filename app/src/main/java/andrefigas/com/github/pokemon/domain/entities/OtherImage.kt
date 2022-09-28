package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class OtherImage(
    @SerializedName("dream_world") val dreamWorld: Sprite?,
    @SerializedName("official-artwork") val officialArtwork: Sprite?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Sprite::class.java.classLoader),
        parcel.readParcelable(Sprite::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(dreamWorld, flags)
        parcel.writeParcelable(officialArtwork, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OtherImage> {
        override fun createFromParcel(parcel: Parcel): OtherImage {
            return OtherImage(parcel)
        }

        override fun newArray(size: Int): Array<OtherImage?> {
            return arrayOfNulls(size)
        }
    }
}
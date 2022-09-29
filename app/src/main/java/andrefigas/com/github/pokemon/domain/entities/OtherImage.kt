package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import andrefigas.com.github.pokemon.utils.readParcelable

data class OtherImage(
    @SerializedName("dream_world") val dreamWorld: Sprite,
    @SerializedName("official-artwork") val officialArtwork: Sprite
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Sprite::class.java),
        parcel.readParcelable(Sprite::class.java)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(dreamWorld, flags)
        parcel.writeParcelable(officialArtwork, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OtherImage

        if (dreamWorld != other.dreamWorld) return false
        if (officialArtwork != other.officialArtwork) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dreamWorld?.hashCode() ?: 0
        result = 31 * result + (officialArtwork?.hashCode() ?: 0)
        return result
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
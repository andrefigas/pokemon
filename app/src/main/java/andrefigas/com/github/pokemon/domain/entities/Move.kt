package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Move(@SerializedName("move") val content: BaseEntity?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readParcelable<BaseEntity>(BaseEntity::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(content, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Move

        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        return content?.hashCode() ?: 0
    }

    companion object CREATOR : Parcelable.Creator<Move> {
        override fun createFromParcel(parcel: Parcel): Move {
            return Move(parcel)
        }

        override fun newArray(size: Int): Array<Move?> {
            return arrayOfNulls(size)
        }
    }



}
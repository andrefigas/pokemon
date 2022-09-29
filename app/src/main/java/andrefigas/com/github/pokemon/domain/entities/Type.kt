package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Type(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val content: BaseEntity?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(BaseEntity::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(slot)
        parcel.writeParcelable(content, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Type

        if (slot != other.slot) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = slot
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<Type> {
        override fun createFromParcel(parcel: Parcel): Type {
            return Type(parcel)
        }

        override fun newArray(size: Int): Array<Type?> {
            return arrayOfNulls(size)
        }
    }



}
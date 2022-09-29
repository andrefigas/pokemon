package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable

open class BaseEntity(var name: String, var url: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (name != other.name) return false
        if (url != other.url) return false

        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<BaseEntity> {
        override fun createFromParcel(parcel: Parcel): BaseEntity {
            return BaseEntity(parcel)
        }

        override fun newArray(size: Int): Array<BaseEntity?> {
            return arrayOfNulls(size)
        }
    }


}
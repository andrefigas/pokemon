package andrefigas.com.github.pokemon.domain.entities

import andrefigas.com.github.pokemon.utils.readStringOrEmpty
import android.os.Parcel
import android.os.Parcelable

data class BaseEntity(override val name: String,
                      override val url: String) : Parcelable,
BaseEntityContract(){

    constructor(parcel: Parcel) : this(
        parcel.readStringOrEmpty(),
        parcel.readStringOrEmpty()
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
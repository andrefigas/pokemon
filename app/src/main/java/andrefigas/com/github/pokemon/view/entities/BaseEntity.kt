package andrefigas.com.github.pokemon.view.entities

import android.os.Parcel
import android.os.Parcelable

open class BaseEntity(var name: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (name != other.name) return false

        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object{

        val ERROR = BaseEntity("")

        @JvmField
        val CREATOR  = object : Parcelable.Creator<BaseEntity> {
            override fun createFromParcel(parcel: Parcel): BaseEntity {
                return BaseEntity(parcel)
            }

            override fun newArray(size: Int): Array<BaseEntity?> {
                return arrayOfNulls(size)
            }
        }
    }

}
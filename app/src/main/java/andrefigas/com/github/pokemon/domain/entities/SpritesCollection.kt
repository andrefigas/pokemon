package andrefigas.com.github.pokemon.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SpritesCollection(
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("back_shiny_female") val backShinyFemale: String?,
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?,
    @SerializedName("other") val other: OtherImage?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(OtherImage::class.java.classLoader)
    )

    fun getBetterImage(): String {
        val all = listOfNotNull(
            other?.officialArtwork?.image,
            other?.dreamWorld?.image,

            frontDefault, frontFemale, frontShiny, frontShinyFemale,
            backDefault, backFemale, backShiny, backShinyFemale

        )

        val svg = ".svg"

        val svgUrl = all.firstOrNull { it.endsWith(svg, true) }

        if(svgUrl != null){
            return svgUrl
        }

        return all.first()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backDefault)
        parcel.writeString(backFemale)
        parcel.writeString(backShiny)
        parcel.writeString(backShinyFemale)
        parcel.writeString(frontDefault)
        parcel.writeString(frontFemale)
        parcel.writeString(frontShiny)
        parcel.writeString(frontShinyFemale)
        parcel.writeParcelable(other, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpritesCollection> {
        override fun createFromParcel(parcel: Parcel): SpritesCollection {
            return SpritesCollection(parcel)
        }

        override fun newArray(size: Int): Array<SpritesCollection?> {
            return arrayOfNulls(size)
        }
    }



}


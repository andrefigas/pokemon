package andrefigas.com.github.pokemon.model.entities

import com.google.gson.annotations.SerializedName

data class SpritesCollection (

	@SerializedName("back_default") val backDefault : String?,
	@SerializedName("back_female") val backFemale : String?,
	@SerializedName("back_shiny") val backShiny : String,
	@SerializedName("back_shiny_female") val backShinyFemale : String?,
	@SerializedName("front_default") val frontDefault : String?,
	@SerializedName("front_female") val frontFemale : String?,
	@SerializedName("front_shiny") val frontShiny : String?,
	@SerializedName("front_shiny_female") val frontShinyFemale : String?,
	@SerializedName("other") val other : OtherImage?){

	companion object{
		private const val SVG = ".svg"
	}

	fun getBetterImage() : String?{
		val all = listOfNotNull(
			other?.officialArtwork?.image,
			other?.dreamWorld?.image,

			frontDefault, frontFemale, frontShiny, frontShinyFemale,
			backDefault, backFemale, backShiny, backShinyFemale

		)

		return all.firstOrNull { it.endsWith(SVG, true) } ?: all.firstOrNull()
	}

}


package andrefigas.com.github.pokemon

import com.google.gson.annotations.SerializedName

open class Page (

	@SerializedName("count") val count : Int,
	@SerializedName("next") val next : String,
	@SerializedName("previous") val previous : String
)
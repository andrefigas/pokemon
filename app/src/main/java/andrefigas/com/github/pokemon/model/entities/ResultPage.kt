package repository.entities

import andrefigas.com.github.pokemon.Page
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import com.google.gson.annotations.SerializedName

class ResultPage (
	count : Int,
	next : String,
	previous : String,
	@SerializedName("results") val results : List<BaseEntity>
) : Page(count, next, previous)
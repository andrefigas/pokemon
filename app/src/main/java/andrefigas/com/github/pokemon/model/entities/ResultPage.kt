package repository.entities

import andrefigas.com.github.pokemon.Page
import andrefigas.com.github.pokemon.model.entities.BaseEntity
import com.google.gson.annotations.SerializedName

open class ResultPage<T : BaseEntity> (
	count : Int,
	next : String,
	previous : String,
	@SerializedName("results") val results : List<T>
) : Page(count, next, previous)
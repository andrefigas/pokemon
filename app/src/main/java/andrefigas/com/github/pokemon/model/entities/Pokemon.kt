package andrefigas.com.github.pokemon.model.entities

import com.google.gson.annotations.SerializedName

class Pokemon(name : String, url : String) : BaseEntity(name, url)  {

    @SerializedName("weight") var weight : Int = 0
    @SerializedName("height") var height : Int = 0

}
package andrefigas.com.github.pokemon.model.entities

import com.google.gson.annotations.SerializedName

class Pokemon(name : String, url : String) : BaseEntity(name, url)  {

    @SerializedName("weight") var weight : Int = 0
    @SerializedName("height") var height : Int = 0
    @SerializedName("sprites") var spritesCollection : SpritesCollection? = null

    constructor(name : String, url : String, weight : Int, height : Int, spritesCollection : SpritesCollection?) : this(name, url){
        this.weight = weight
        this.height = height
        this.spritesCollection = spritesCollection
    }

}
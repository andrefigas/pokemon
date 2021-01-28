package andrefigas.com.github.pokemon.model.entities

open class BaseEntity(val name : String, val url : String){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (name != other.name) return false
        if (url != other.url) return false

        return true
    }

}
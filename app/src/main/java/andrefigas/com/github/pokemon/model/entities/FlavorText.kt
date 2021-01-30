package andrefigas.com.github.pokemon.model.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class FlavorText {

    @SerializedName("flavor_text") var label : String? = null
    @SerializedName("language") var language : BaseEntity? = null

}
package andrefigas.com.github.pokemon.utils

import android.os.Parcel
import android.os.Parcelable

/**
 * build a string with data list and a string separator
 * @param separator splitter
 */
fun List<String>.toString(separator: String): String {
    if (size == 0) {
        return ""
    }

    val sb = StringBuilder()

    forEach {
        sb.append(it)
        sb.append(separator)
    }

    sb.delete(sb.length - separator.length, sb.length)
    return sb.toString()
}

fun  <T : Parcelable?> Parcel.readParcelable(loader : Class<T>): T {
    return readParcelable<T>(loader.classLoader) ?: throw IllegalArgumentException()
}

inline fun  <reified T : Parcelable?> Parcel.readParcelableArray(loader : Class<T>): Array<T> {
    return readParcelableArray(loader.classLoader)?.map { it as T }?.toTypedArray()?: throw IllegalArgumentException()
}

fun Parcel.readStringOrEmpty(): String{
    return readString()?:""
}



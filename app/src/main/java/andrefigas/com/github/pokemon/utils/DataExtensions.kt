package andrefigas.com.github.pokemon.utils

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject

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

fun <T> PublishSubject<T>.observe(lifecycleOwner: LifecycleOwner, onNext : Consumer<T> ): Disposable {
    val disposable = subscribe(onNext)
    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycleOwner.lifecycle.removeObserver(this)
            disposable.dispose()
        }
    })

    return disposable
}



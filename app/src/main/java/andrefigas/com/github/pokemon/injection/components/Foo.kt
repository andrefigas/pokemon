package andrefigas.com.github.pokemon.injection.components

import andrefigas.com.github.pokemon.injection.components.Foo
import androidx.recyclerview.widget.RecyclerView

class Foo {
    fun foo(foo: Foo?) {}
    fun bar() {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                foo(this@Foo)
            }
        }
    }
}
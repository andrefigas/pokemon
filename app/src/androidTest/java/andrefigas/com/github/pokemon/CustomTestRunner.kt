package andrefigas.com.github.pokemon

import androidx.test.runner.AndroidJUnitRunner
import android.app.Application
import android.content.Context

class CustomTestRunner : AndroidJUnitRunner() {
    @kotlin.jvm.Throws(
        InstantiationException::class,
        IllegalAccessException::class,
        ClassNotFoundException::class
    )
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, MockApplication::class.java.name, context)
    }
}
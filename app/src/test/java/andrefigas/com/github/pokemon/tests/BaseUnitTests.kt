package andrefigas.com.github.pokemon.tests

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.koin.core.context.GlobalContext


open class BaseUnitTests {

    @Before
    fun setup(){
        setupDependencies()
        setupRx()
        setupThread()
    }

    @After
    fun tearDown() {
        GlobalContext.stopKoin()
    }

    private fun setupDependencies(){
        GlobalContext.startKoin {
            modules(
                listOf(andrefigas.com.github.pokemon.di.modules)
            )
        }
    }

    private fun setupThread(){
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    private fun setupRx(){
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    protected fun <T> LiveData<T>.assert(
        waitFor : (T)->Boolean,
        assertion : (T)->Boolean,
        trigger : ()-> Unit) {
        var assertion = false

        ArchTaskExecutor.getMainThreadExecutor().execute {

            observeForever { data ->
                if (waitFor(data)){
                    assertion = assertion(data)
                }

            }

            trigger()
        }

        assert(assertion)
    }


}
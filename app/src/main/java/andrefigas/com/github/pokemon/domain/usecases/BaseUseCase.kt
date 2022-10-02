package andrefigas.com.github.pokemon.domain.usecases

import io.reactivex.disposables.Disposable

abstract class BaseUseCase : BaseUseCaseContract {

    protected var disposable: Disposable? = null

    override fun dispose() {
        disposable?.dispose()
    }

}

interface BaseUseCaseContract{

    fun dispose()
}
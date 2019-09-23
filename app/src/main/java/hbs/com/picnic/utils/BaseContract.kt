package hbs.com.picnic.utils

import io.reactivex.disposables.Disposable

abstract class BaseContract {
    abstract class Presenter {
        private val disposables = mutableListOf<Disposable>()

        fun onClear() {
            disposables.forEach { disposable ->
                disposable.dispose()
            }
            disposables.clear()
        }

        fun addDisposable(disposable: Disposable) {
            disposables.add(disposable)
        }
    }
}
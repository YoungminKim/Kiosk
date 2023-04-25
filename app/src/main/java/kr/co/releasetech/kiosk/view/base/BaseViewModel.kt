package kr.co.releasetech.kiosk.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.realm.Realm
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

open class BaseViewModel: ViewModel(), KoinComponent {
    companion object{
        private const val TAG = "BaseViewModel"
    }

    val networkError = MutableLiveData<Unit>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val mRealm: Realm by lazy { Realm.getDefaultInstance() }

    val _onDbError = MutableLiveData<Unit>()
    val onDbError: LiveData<Unit>
        get() = _onDbError


    fun addToDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    override fun onCleared() {
        DebugUtils.setLog(TAG, "onCleared")
        if(!mRealm.isClosed) mRealm.close()
        super.onCleared()
    }

}
package kr.co.releasetech.kiosk.view.activity.colorpicker

import android.graphics.Color
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class ColorPickerViewModel : BaseViewModel() {

    val colorField = ObservableField("#FFFFFFFF")

    private val _onConfirm = MutableLiveData<Unit>()
    val onConfirm: LiveData<Unit>
        get() = _onConfirm

    private val _isNotHexCode = MutableLiveData<Unit>()
    val isNotHexCode: LiveData<Unit>
        get() = _isNotHexCode

    private val _onCancel = MutableLiveData<Unit>()
    val onCancel: LiveData<Unit>
        get() = _onCancel

    fun confirm() {
        colorField.get()?.let {
            try {
                Color.parseColor(it)
                _onConfirm.postValue(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _isNotHexCode.postValue(Unit)
            }
        }

    }

    fun cancel() {
        _onCancel.postValue(Unit)
    }
}
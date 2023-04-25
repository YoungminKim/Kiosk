package kr.co.releasetech.kiosk.view.activity.splash

import android.Manifest
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.co.releasetech.kiosk.AppConst.PERMISSIONS
import kr.co.releasetech.kiosk.AppConst.REQUEST_PERMISSION
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivitySplashBinding
import kr.co.releasetech.kiosk.manager.ReceiptPrinterManager
import kr.co.releasetech.kiosk.receiver.MyDeviceAdminReceiver
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.adminlogin.AdminLoginActivity
import kr.co.releasetech.kiosk.view.activity.manager.ManagerActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start


class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    companion object{
        private const val TAG = "SplashActivity"
    }
    private val viewModel: SplashViewModel by viewModel()
    private val mDevicePolicyManager: DevicePolicyManager by lazy{
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private val mAdminComponentName: ComponentName by lazy{
        ComponentName(this, MyDeviceAdminReceiver::class.java)
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                start<ManagerActivity> { }
            }
        }

        finish()
    }

    private val deviceResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when (it.resultCode) {
            Activity.RESULT_OK -> {

            }
        }

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        DebugUtils.setLog(TAG, "onCreate called!!!")

        with(viewModel){
            findPort()
            appSettingModel.observe(this@SplashActivity){ checkAppFirstActive ->
                DebugUtils.setLog(TAG, "isFirstActive : ${ checkAppFirstActive.isFirstActive }")
                if(checkAppFirstActive.isFirstActive){
                    val printerManager = ReceiptPrinterManager(applicationContext, intent){
                        viewModel.checkAppFirstActive(checkAppFirstActive.isFirstActive)
                    }
                    printerManager.init()
                }else {
                    if(checkPermission()){
                        nextPage()
                    }


                    /*if(AppManager.checkAppInstall(this@SplashActivity, KICC_PACKAGE_NAME)) nextPage()
                    else{
                        showToast(R.string.not_kicc_app_installed)
                        finish()
                    }*/

                }
            }
        }
    }

    private fun isDeviceAdminApp() = mDevicePolicyManager.isAdminActive(mAdminComponentName)

    private fun isDeviceOwnerApp() = mDevicePolicyManager.isDeviceOwnerApp(packageName)

    private fun enableAdmin() {
        if (isDeviceAdminApp()) {
            return
        }
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponentName)
        // Start the add device admin activity
        deviceResultLauncher.launch(intent)
    }

    private fun disableAdmin(){
        if(!isDeviceAdminApp()) return
        mDevicePolicyManager.removeActiveAdmin(mAdminComponentName)
    }


    private fun nextPage(){

        resultLauncher.launch(Intent(this, AdminLoginActivity::class.java))
    }


    private fun checkPermission(): Boolean {
        var result = true

        val checkPermission: ArrayList<String> = arrayListOf()
        PERMISSIONS.filter { ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_DENIED }.map {
            DebugUtils.setLog(TAG , "permissions : $it")
            checkPermission.add(it)
        }

        if(checkPermission.isNotEmpty()){

            ActivityCompat.requestPermissions(this, checkPermission.toArray(arrayOf()), REQUEST_PERMISSION)
            result = false
        }

        DebugUtils.setLog(TAG , "result : $result")
        return result
    }

    private fun checkGrantResults(grantResult: Int, str: String): Boolean{
        val result: Boolean
        val resultMsg: String
        if (grantResult == PackageManager.PERMISSION_GRANTED){
            resultMsg = "$str permission authorized"
            result = true
        }else{
            resultMsg = "$str permission denied"
            result = false
        }
        DebugUtils.setLog(TAG, resultMsg)
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                var isAllChecked = true
                for (i in permissions.indices) {
                    var isChecked = false
                    val permission = permissions[i]
                    val grantResult = grantResults[i]
                    when(permission){
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ->{
                            isChecked = checkGrantResults(grantResult, "write external storage")
                        }

                        Manifest.permission.READ_EXTERNAL_STORAGE ->{
                            isChecked = checkGrantResults(grantResult, "read external storage")
                        }
                    }
                    DebugUtils.setLog(TAG, "permission : $permission")
                    DebugUtils.setLog(TAG, "isChecked : $isChecked")
                    if(!isChecked) isAllChecked = false
                }


                if(!isAllChecked) {
                    showToast(R.string.pemission_denied)
                    finish()
                }else{
                    nextPage()
                }

            }

        }
    }


}
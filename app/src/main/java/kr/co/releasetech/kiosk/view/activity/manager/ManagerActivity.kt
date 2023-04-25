package kr.co.releasetech.kiosk.view.activity.manager

import android.app.Activity
import android.content.Intent
import android.os.*
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.releasetech.kiosk.AppConst.DATA_TEXT_FILE_NAME
import kr.co.releasetech.kiosk.AppConst.ZIP_FILE_NAME
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityManagerBinding
import kr.co.releasetech.kiosk.manager.ReceiptPrinterManager
import kr.co.releasetech.kiosk.manager.ZipManager
import kr.co.releasetech.kiosk.model.MenuData
import kr.co.releasetech.kiosk.utils.DateUtils.getDateText
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.FileUtils.deleteDirectory
import kr.co.releasetech.kiosk.utils.FileUtils.getFileName
import kr.co.releasetech.kiosk.utils.FileUtils.writeExternalStorage
import kr.co.releasetech.kiosk.utils.FileUtils.writeInternalStorage
import kr.co.releasetech.kiosk.utils.TextUtils.getMoneyComma
import kr.co.releasetech.kiosk.utils.WindowUtils
import kr.co.releasetech.kiosk.view.activity.adminsetting.AdminSettingActivity
import kr.co.releasetech.kiosk.view.activity.editscreen.EditScreenActivity
import kr.co.releasetech.kiosk.view.activity.main.MainActivity
import kr.co.releasetech.kiosk.view.activity.menuSetting.MenuSettingActivity
import kr.co.releasetech.kiosk.view.activity.order.OrderActivity
import kr.co.releasetech.kiosk.view.activity.payment.PaymentActivity
import kr.co.releasetech.kiosk.view.activity.selectorder.SelectOrderActivity
import kr.co.releasetech.kiosk.view.activity.splash.SplashActivity
import kr.co.releasetech.kiosk.view.activity.standby.StandbyActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import kr.co.releasetech.kiosk.view.dialog.OneButtonDialog
import kr.co.releasetech.kiosk.view.dialog.TwoButtonDailog
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start
import java.io.*
import java.util.*


class ManagerActivity: BaseActivity<ActivityManagerBinding>(R.layout.activity_manager) {
    companion object{
        private const val TAG = "ManagerActivity"
        private const val UN_ZIP_COMPLETED = 7002
    }
    val viewModel:ManagerViewModel by viewModel()

    private val mHandler: Handler = object :Handler(Looper.myLooper()!!){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                UN_ZIP_COMPLETED -> {
                    showProgress(getString(R.string.read_setting_file))
                    readUploadFile()

                }
            }
        }
    }

    private var downloadResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                showProgress(getString(R.string.copy_settting_file))
                writeExternalStorage(this@ManagerActivity, it.data?.data){
                    hideProgress()
                    if(it)showToast(R.string.setting_file_download_suceess)
                }
            }
            else -> hideProgress()
        }
    }


    private var uploadResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.data?.also { uri ->
                    val fileName = getFileName(this, uri)
                    if(fileName == ZIP_FILE_NAME){
                        showProgress(getString(R.string.setting_data_reset))
                        viewModel.deleteRealm()
                        viewModel.deleteData.observe(this@ManagerActivity){
                            showProgress(getString(R.string.setting_file_reset))
                            deleteDirectory(File(getExternalFilesDir(null)?.absolutePath, getString(R.string.app_name)))
                            showProgress(getString(R.string.copy_settting_file))
                            writeInternalStorage(context = this@ManagerActivity, uri = uri, fileName = fileName){ isSuccess ->
                                if(isSuccess){
                                    showProgress(getString(R.string.decompression_setting_file))

                                    mHandler.postDelayed({
                                        ZipManager().unzip("${getExternalFilesDir(null)?.absolutePath}/$ZIP_FILE_NAME", "${getExternalFilesDir(null)?.absolutePath}/${getString(R.string.app_name)}")
                                        mHandler.sendEmptyMessage(UN_ZIP_COMPLETED)
                                    }, 1000)
                                }
                            }
                        }


                    }
                    else showToast(R.string.not_setting_file)
                }

            }
            else -> hideProgress()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowUtils.setImmersiveMode(window)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.dateTv.text = getDateText(System.currentTimeMillis())



        val yesterdayCal = Calendar.getInstance()
        yesterdayCal.set(Calendar.DAY_OF_MONTH, yesterdayCal.get(Calendar.DAY_OF_MONTH) - 1)
        binding.salesSummaryTv.text = String.format(getString(R.string.how_sales_summary), getDateText(yesterdayCal.timeInMillis))
        with(viewModel){

            getSummary(yesterdayCal)

            totalOrderCount.observe(this@ManagerActivity){

                totalOrderCountField.set("$it${getString(R.string.count)}")
            }

            totalSale.observe(this@ManagerActivity){
                totalSaleField.set("${getMoneyComma(it)}${getString(R.string.currency)}")
            }

            totalRefundCount.observe(this@ManagerActivity){
                totalRefundCountField.set("$it${getString(R.string.count)}")
            }

            totalRefund.observe(this@ManagerActivity){
                totalRefundField.set("${getMoneyComma(it)}${getString(R.string.currency)}")
            }


            onOpenStore.observe(this@ManagerActivity){
                val printerManager = ReceiptPrinterManager(applicationContext)
                printerManager.isConnect { isConnect ->
                    runOnUiThread {
                        if(isConnect){

                            if(it.useStandbyScreen){
                                start<StandbyActivity> {
                                    putExtra("setting", it)
                                }
                            }else{
                                if(it.useSelectOrderScreen){
                                    start<SelectOrderActivity> {
                                        putExtra("setting", it)
                                    }
                                }else{
                                    start<MainActivity> {
                                        putExtra("setting", it)
                                    }
                                }
                            }

                            finish()
                            startLockTask()
                        }else{
                            showToast(R.string.not_found_printer)
                        }
                    }

                }



            }

            onMenuSetting.observe(this@ManagerActivity){
                start<MenuSettingActivity> {  }
            }

            onManagerSetting.observe(this@ManagerActivity){
                start<AdminSettingActivity>{ }
            }

            onOrderList.observe(this@ManagerActivity){
                start<OrderActivity> {  }
            }

            onPaymentList.observe(this@ManagerActivity){
                start<PaymentActivity> {  }
            }

            onSettingDownload.observe(this@ManagerActivity){
                showProgress(getString(R.string.compress_settting_file))
                createZip(it)

                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_TITLE, ZIP_FILE_NAME)
                }
                downloadResultLauncher.launch(intent)
            }

            onSettingUpload.observe(this@ManagerActivity){

                TwoButtonDailog(this@ManagerActivity, getString(R.string.warning_pre_data_delete)){
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    }
                    uploadResultLauncher.launch(intent)
                }.show()
            }

            onEditScreen.observe(this@ManagerActivity){
                start<EditScreenActivity> {  }
            }

            onAppFinished.observe(this@ManagerActivity){
                kioskFinish()
            }

            uploadSuccess.observe(this@ManagerActivity){
                hideProgress()


                OneButtonDialog(this@ManagerActivity, getString(R.string.setting_file_upload_suceess)){
                    appFinish()

                }.show()



            }

        }
    }

    override fun onResume() {
        super.onResume()
        stopLockTask()
    }

    private fun kioskFinish(){

        moveTaskToBack(true)
        finishAndRemoveTask()
        Process.killProcess(Process.myPid())
        startActivity(Intent(this, SplashActivity::class.java))

    }

    fun readUploadFile(){
        var txtFileStr = ""
        val dir = File(getExternalFilesDir(null)?.absolutePath, getString(R.string.app_name))
        try {
            if(dir.exists()){
                val file = File(dir, DATA_TEXT_FILE_NAME)
                if(file.exists()){

                    val read = FileReader(file)
                    var data: Int
                    var c: Char
                    while (read.read().also { data = it } != -1) {
                        c = data.toChar()
                        txtFileStr += "$c"
                    }
                    read.close()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        DebugUtils.setLog(TAG, "txtFileStr : $txtFileStr")

        val data = Gson().fromJson(txtFileStr, MenuData::class.java)

        viewModel.setMenuData(data)

    }

    private fun createZip(data: MenuData){
        val dir = File(getExternalFilesDir(null)?.absolutePath, getString(R.string.app_name))

        try {

            if(!dir.exists()){
                dir.mkdirs()
            }

            val file = File(dir, DATA_TEXT_FILE_NAME)
            if(file.exists())  file.delete()
            file.createNewFile()
            val write = FileWriter(file, true)
            val gson = GsonBuilder().create()

            write.write(gson.toJson(data))
            write.flush()
            write.close()

        }catch (e: Exception){
            e.printStackTrace()
        }

        val zipManager = ZipManager()

        val zipPath = "${getExternalFilesDir(null)?.absolutePath}/$ZIP_FILE_NAME"
        zipManager.zipFolder(dir.absolutePath, zipPath)
    }


    private fun showProgress(value: String){
        DebugUtils.setLog(TAG, "showProgress called!!!")
        binding.progressRl.visibility = View.VISIBLE
        binding.progressTv.text = value
    }

    private fun hideProgress(){
        binding.progressRl.visibility = View.GONE
    }

}
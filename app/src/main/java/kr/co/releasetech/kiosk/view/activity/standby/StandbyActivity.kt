package kr.co.releasetech.kiosk.view.activity.standby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityStandbyBinding
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.service.ConsoleService
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.adminlogin.AdminLoginActivity
import kr.co.releasetech.kiosk.view.activity.main.MainActivity
import kr.co.releasetech.kiosk.view.activity.manager.ManagerActivity
import kr.co.releasetech.kiosk.view.activity.selectorder.SelectOrderActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start
import java.io.File

class StandbyActivity: BaseActivity<ActivityStandbyBinding>(R.layout.activity_standby) {
    companion object{
        private const val TAG = "StandbyActivity"
        private const val SETUP_BG = 10001
    }
    val viewModel: StandbyViewModel by viewModel()

    private var mBgList: ArrayList<EditScreenStandbyBg> = arrayListOf()
    private var bgPosition = 0

    var imageTimeout = 30 * 1000L

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                start<ManagerActivity> {  }
                finish()
            }
        }
    }


    private val mHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                SETUP_BG ->{
                    val data = mBgList[bgPosition]
                    if(bgPosition + 1 < mBgList.size) ++bgPosition
                    else bgPosition = 0
                    setAdView(data)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DebugUtils.setLog(TAG, "onCreate called!!!")
        binding.vm = viewModel
        binding.lifecycleOwner = this


        with(binding){
            val ani = AnimationUtils.loadAnimation(this@StandbyActivity, R.anim.store_in)
            iv.animation = ani

            iv.animation.start()
        }

        val extra = intent.getParcelableExtra<ScreenSetting>("setting")
        extra?.let { imageTimeout = it.standbyScreenImageSecond * 1000L }

        viewModel.run {

            onBgList.observe(this@StandbyActivity){
                if(it.size > 0){
                    mBgList = it
                    mHandler.sendEmptyMessage(SETUP_BG)
                }

            }
            onMain.observe(this@StandbyActivity) {

                extra?.let { extra ->
                    if(extra.useSelectOrderScreen) start<SelectOrderActivity> { putExtra("setting", extra) }
                    else start<MainActivity> { putExtra("setting", extra) }
                }

            }

            stopLockTask.observe(this@StandbyActivity) {

                //finish()
                resultLauncher.launch(Intent(this@StandbyActivity, AdminLoginActivity::class.java))
            }
        }

    }

    private fun setAdView(data: EditScreenStandbyBg){

        val file = File(getExternalFilesDir(null)?.absolutePath, "${getString(R.string.app_name)}/${data.fileName}")
        DebugUtils.setLog(TAG, "file : ${file.absolutePath}")
        if(data.isVideo){
            binding.vv.visibility = View.VISIBLE
            binding.bgIv.visibility = View.GONE
            binding.vv.stopPlayback()
            binding.vv.setVideoPath(file.path)
            binding.vv.setOnPreparedListener {
                binding.vv.start()
            }

            binding.vv.setOnCompletionListener {
                mHandler.sendEmptyMessage(SETUP_BG)
            }
        }else{
            binding.bgIv.visibility = View.VISIBLE
            binding.vv.visibility = View.GONE
            Glide.with(this@StandbyActivity)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.bgIv)

            mHandler.sendEmptyMessageDelayed(SETUP_BG, imageTimeout)
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }



    override fun onResume() {
        super.onResume()
        viewModel.getBgList()
        if(!ConsoleService.isServiceRunning){
            startService(Intent(this, ConsoleService::class.java))
        }

    }


    override fun onPause() {
        super.onPause()
        if(binding.vv.isPlaying) binding.vv.pause()
        mHandler.removeMessages(SETUP_BG)
    }
    override fun onDestroy() {
        super.onDestroy()

        binding.vv.stopPlayback()
        stopService(Intent(this, ConsoleService::class.java))
    }

}

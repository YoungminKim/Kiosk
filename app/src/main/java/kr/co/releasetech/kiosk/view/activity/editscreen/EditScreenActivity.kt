package kr.co.releasetech.kiosk.view.activity.editscreen

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kr.co.releasetech.kiosk.AppConst.IMG_EXTENSIONS
import kr.co.releasetech.kiosk.AppConst.VIDEO_EXTENSIONS
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityEditScreenBinding
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.service.ConsoleService
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.FileUtils
import kr.co.releasetech.kiosk.utils.FileUtils.writeInternalStorage
import kr.co.releasetech.kiosk.utils.ImageUtils
import kr.co.releasetech.kiosk.utils.ImageUtils.getVideoBitmap
import kr.co.releasetech.kiosk.utils.WindowUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.activity.colorpicker.ColorPickerActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start
import splitties.views.imageBitmap
import splitties.views.onClick
import java.io.File

class EditScreenActivity : BaseActivity<ActivityEditScreenBinding>(R.layout.activity_edit_screen) {
    companion object {
        private const val TAG = "EditScreenActivity"
        private const val BG_COLOR = "bg"

        private const val NONE = 1000
        private const val STANDBY = 1001
        private const val LOGO = 1002
    }

    val viewModel: EditScreenViewModel by viewModel()
    var galleryType = NONE


    private val standbyBgAdapter: EditScreenStandbyBgAdapter by lazy {
        EditScreenStandbyBgAdapter(this, viewModel)
    }

    private val themeAdapter: EditScreenThemeAdapter by lazy {
        EditScreenThemeAdapter(this, viewModel)
    }

    private val colorResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if(it.resultCode == Activity.RESULT_OK){
            it.data?.let { data ->
                DebugUtils.setLog(TAG, "type : ${data.getStringExtra("type")}")
                val color = data.getIntExtra("color", -1)
                val hexCode = data.getStringExtra("hexCode")
                when(data.getStringExtra("type")){
                    BG_COLOR -> {
                        viewModel.setBgColor(color, hexCode)
                    }
                }
            }
        }
    }

    private val galleryResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.data?.also { uri ->

                    when(galleryType){
                        STANDBY ->{
                            val fileName = FileUtils.getFileName(this, uri)
                            DebugUtils.setLog(TAG, "uri : ${uri.path}")
                            DebugUtils.setLog(TAG, "fileName : $fileName")

                            fileName?.let { fileName ->

                                writeInternalStorage(
                                    context = this@EditScreenActivity,
                                    uri = uri,
                                    dir = "${getString(R.string.app_name)}/",
                                    fileName = fileName
                                ) {
                                    viewModel.addStandbyBgItem(fileName, standbyBgAdapter.itemCount)
                                }
                            }
                        }
                        LOGO ->{
                            ImageUtils.getBitmap(this.contentResolver, uri)?.let { bitmap ->
                                binding.logoIv.imageBitmap = bitmap
                                val file = ImageUtils.saveBitmapToFile(this@EditScreenActivity, bitmap)

                                viewModel.setLogoImage(file.absolutePath)
                            }
                        }


                    }

                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowUtils.setImmersiveMode(window)

        binding.vm = viewModel
        binding.lifecycleOwner = this



        with(binding) {
            includeTitleV.title = getString(R.string.manager_menu_08)
            includeTitleV.backIv.onClick {
                finish()
            }


            val helper = ItemTouchHelper(ItemTouchHelperCallback(standbyBgAdapter))
            helper.attachToRecyclerView(standbyBgRv)
            standbyBgRv.adapter = standbyBgAdapter

            standbySecondSavedBt.onClick {
                showColorPicker()
            }

            themeRv.adapter = themeAdapter

        }

        with(viewModel) {

            getScreenSetting()
            getStandbyBgList()


            onOpenGallery.observe(this@EditScreenActivity) {
                galleryType = STANDBY
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                galleryResultLauncher.launch(intent)
            }



            onAddStandbyBgItem.observe(this@EditScreenActivity) {
                standbyBgAdapter.addItem(it)
                binding.standbyBgEmptyTv.visibility =
                    if (standbyBgAdapter.itemCount > 0) View.GONE else View.VISIBLE

                galleryType = NONE
            }

            onStandbyBgList.observe(this@EditScreenActivity) {
                standbyBgAdapter.addList(it)
                if (standbyBgAdapter.itemCount > 0) {
                    binding.standbyBgEmptyTv.visibility = View.GONE
                    it[0]?.let { firstItem ->
                        val file = getFile(firstItem.fileName)
                        if (firstItem.isVideo) setVideoView(file)
                        else setImageView(file)
                    }

                } else binding.standbyBgEmptyTv.visibility = View.VISIBLE

            }


            onShowStandbyBg.observe(this@EditScreenActivity) {
                val file = getFile(it.fileName)
                DebugUtils.setLog(TAG, "file : ${file.absolutePath}")
                if (it.isVideo) {
                    setVideoView(file)

                } else {
                    setImageView(file)
                }
            }


            onMoveItem.observe(this@EditScreenActivity) {

            }

            onEmptyStandbyBg.observe(this@EditScreenActivity) {
                binding.standbyBgEmptyTv.visibility = View.VISIBLE
            }

            onIsNotAllow.observe(this@EditScreenActivity) {
                showToast(R.string.not_allow_file)
                galleryType = NONE
            }

            isCheckedUseStandby.observe(this@EditScreenActivity) {
                binding.standbyLl.visibility = if (it) View.VISIBLE else View.GONE
            }

            onSelectBgColor.observe(this@EditScreenActivity) {
                val intent = Intent(this@EditScreenActivity, ColorPickerActivity::class.java)
                intent.putExtra("type", BG_COLOR)
                intent.putExtra("color", bgColorField.get())
                colorResultLauncher.launch(intent)
            }

            onDbError.observe(this@EditScreenActivity) {
                showToast(R.string.db_error_message)
            }

            onSelectLogo.observe(this@EditScreenActivity){
                galleryType = LOGO
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                galleryResultLauncher.launch(intent)
            }

            onLogoSaved.observe(this@EditScreenActivity){
                galleryType = NONE
            }

            onSetLogoImage.observe(this@EditScreenActivity){
                it?.let {
                    Glide.with(this@EditScreenActivity)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.logoIv)
                }

            }

            onSelectTheme.observe(this@EditScreenActivity){
                themeAdapter.selectItem(position = it)
            }
        }
    }

    private fun showColorPicker() {
        ColorPickerDialog.Builder(this)
            .setPositiveButton(getString(R.string.confirm), object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {

                }

            })
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which -> dialog.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private fun getFile(fileName: String) =
        File(getExternalFilesDir(null)?.absolutePath, "${getString(R.string.app_name)}/${fileName}")

    private fun setVideoView(file: File) {
        with(binding) {
            standbyVv.visibility = View.VISIBLE
            standbyIv.visibility = View.GONE
            standbyVv.stopPlayback()
            standbyVv.setVideoPath(file.path)
            standbyVv.setOnPreparedListener {
                standbyVv.start()
            }
        }

    }

    private fun setImageView(file: File) {
        binding.standbyIv.visibility = View.VISIBLE
        binding.standbyVv.visibility = View.GONE
        Glide.with(this@EditScreenActivity)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.standbyIv)

    }

    override fun onPause() {
        super.onPause()
        if (binding.standbyVv.isPlaying) binding.standbyVv.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.standbyVv.stopPlayback()
    }
}
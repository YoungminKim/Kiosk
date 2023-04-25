package kr.co.releasetech.kiosk.view.activity.addgoods

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityAddGoodsBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.ImageUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.activity.selectcategory.SelectCategoryActivity
import kr.co.releasetech.kiosk.view.activity.selectoption.SelectOptionActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.views.imageBitmap
import java.io.File

class AddGoodsActivity :
    BaseActivity<ActivityAddGoodsBinding>(R.layout.activity_add_goods) {
    companion object {
        private const val TAG = "AddGoodsActivity"
    }

    private var mBitmap: Bitmap? = null
    val viewModel: AddGoodsViewModel by viewModel()

    private var isUpdate = false

    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectCategoryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectOptionResultLauncher: ActivityResultLauncher<Intent>


    var mCategory:Category? = null

    private lateinit var optionAdapter: AddGoodsOptionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        mCategory = intent.getParcelableExtra<Category>("category")
        val goods = intent.getParcelableExtra<Goods>("goods")

        setResultLauncher()

        optionAdapter = AddGoodsOptionAdapter(this, viewModel)

        with(binding){
            addOptionRv.adapter = optionAdapter
            val helper = ItemTouchHelper(ItemTouchHelperCallback(optionAdapter))
            helper.attachToRecyclerView(addOptionRv)

            confirmBt.setOnClickListener {
                var file: File? = null
                mBitmap?.let { bitmap ->
                    file = ImageUtils.saveBitmapToFile(this@AddGoodsActivity, bitmap)
                }


                var optionCategoryIds = ""
                for (i in optionAdapter.mList.indices){
                    val item = optionAdapter.mList[i]
                    optionCategoryIds += item.id
                    if(i < optionAdapter.mList.size - 1) optionCategoryIds += ","
                }


                if(isUpdate){
                    goods?.let { goods ->
                        if(file != null) goods.imgUrl = file!!.absolutePath
                        file?.let { goods.imgUrl = it.absolutePath }
                        goods.optionCategoryIds = optionCategoryIds
                        mCategory?.id?.let { goods.categoryId = it }
                        viewModel.modifyGoods(goods)
                    }
                }else{
                    mCategory?.let{
                        viewModel.addGoods(it.id, optionCategoryIds, file)
                    }
                }

            }

            cancelBt.setOnClickListener {
                finish()
            }
        }



        with(viewModel){

            categoryNameField.set(mCategory?.name)

            goods?.let { goods ->
                isUpdate = true
                nameField.set(goods.name)

                Glide.with(this@AddGoodsActivity)
                    .load(File(goods.imgUrl))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.iv)
                descriptionField.set(goods.description)

                if(goods.optionCategoryIds.isNotEmpty()) getOptionList(goods.optionCategoryIds)

            }

            onOpenGallery.observe(this@AddGoodsActivity) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                galleryResultLauncher.launch(intent)
            }

            onAddGoods.observe(this@AddGoodsActivity) {
                if (it > 0) {
                    showToast(it)
                } else {
                    finish()
                }
            }

            onShowSelectCategory.observe(this@AddGoodsActivity) {
                val intent = Intent(this@AddGoodsActivity, SelectCategoryActivity::class.java)
                intent.putExtra("isUpdate", true)
                selectCategoryResultLauncher.launch(intent)
            }

            onShowSelectOption.observe(this@AddGoodsActivity){
                val intent = Intent(this@AddGoodsActivity, SelectOptionActivity::class.java)
                intent.putExtra("optionCategorys", optionAdapter.mList)
                selectOptionResultLauncher.launch(intent)
            }

            optionList.observe(this@AddGoodsActivity){
                optionAdapter.addList(it)
                binding.addOptionTv.visibility = if(optionAdapter.itemCount > 0) View.GONE else View.VISIBLE
            }

            emptyOptions.observe(this@AddGoodsActivity){
                binding.addOptionTv.visibility = View.VISIBLE
            }

        }
    }

    private fun setResultLauncher(){
        galleryResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                DebugUtils.setLog(TAG, "resultCode : ${it.resultCode}")
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        val currentImageUri = it.data?.data
                        currentImageUri?.let { uri ->
                            DebugUtils.setLog(TAG, "currentImageUri : $uri")
                            ImageUtils.getBitmap(this.contentResolver, uri)?.let { bitmap ->
                                mBitmap = bitmap
                                binding.iv.imageBitmap = bitmap

                            }
                        }

                    }
                    Activity.RESULT_CANCELED ->{ }
                    else -> showToast("failed")
                }
            }

        selectCategoryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                mCategory = it.data?.getParcelableExtra("category")
                viewModel.categoryNameField.set(mCategory?.name)
            }
        }


        selectOptionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val item = it.data?.getParcelableExtra<OptionCategory>("option")
                item?.let { item ->
                    optionAdapter.addItem(item)
                }
                binding.addOptionTv.visibility = if(optionAdapter.itemCount > 0) View.GONE else View.VISIBLE
            }
        }
    }
}
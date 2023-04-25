package kr.co.releasetech.kiosk.view.activity.goodsdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityGoodsDetailBinding
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.ImageUtils
import kr.co.releasetech.kiosk.utils.TextUtils.getMoneyComma
import kr.co.releasetech.kiosk.view.activity.addgoods.AddGoodsActivity
import kr.co.releasetech.kiosk.view.activity.main.MainActivity
import kr.co.releasetech.kiosk.view.activity.option.OptionActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.views.imageBitmap
import splitties.views.onClick

class GoodsDetailActivity : BaseActivity<ActivityGoodsDetailBinding>(R.layout.activity_goods_detail){
    companion object {
        private const val TAG = "GoodsDetailActivity"
    }

    val viewModel: GoodsDetailViewModel by viewModel()
    var mCart: Cart? = null

    private var mCountDownTimer: CountDownTimer? = null

    private val optionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        DebugUtils.setLog(TAG, "resultCode : ${it.resultCode}")
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                mCart = it.data?.getParcelableExtra<Cart>("cart")
                mCart?.let { cart ->
                    DebugUtils.setLog(TAG, "cart : ${cart.optionNames}")
                    DebugUtils.setLog(TAG, "quantity : ${cart.quantity}")
                    DebugUtils.setLog(TAG, "price : ${cart.price}")
                    viewModel.setPrice(cart.price)
                    cart.totalPrice = cart.price * cart.quantity
                    DebugUtils.setLog(TAG, "totalPrice : ${cart.totalPrice}")
                    viewModel.totalPriceField.set(getMoneyComma(cart.totalPrice) + getString(R.string.currency))

                }

            }
            Activity.RESULT_CANCELED ->{  }
            TIME_OUT ->{
                setResult(TIME_OUT)
                finish()
            }
            else -> showToast("failed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this


        val setting = intent.getParcelableExtra<ScreenSetting>("setting")

        setting?.let { setting ->
            //binding.ll.setBackgroundColor(setting.bgColor)
            setCountDownTimer(setting.orderScreenWaitSecond * 1000L)



            val themeColorResId = resources.getIdentifier("theme_${setting.themeType}", "color", packageName)
            val themeColor = ContextCompat.getColor(this@GoodsDetailActivity, themeColorResId)

            binding.topRl.setBackgroundColor(themeColor)
            binding.priceTv.setTextColor(themeColor)
            binding.addTv.setBackgroundResource(resources.getIdentifier("theme_btn_${setting.themeType}", "drawable", packageName))
        }


        //val adapter = GoodsDetailAdapter(this, viewModel)

        with(binding){
            goods = intent?.getParcelableExtra<Goods>("goods")
            priceTv.text = goods?.price.toString()
            backIv.onClick{
                finish()
            }
            //rv.adapter = adapter
        }

        with(viewModel){
            binding.goods?.apply {

                val cart = intent?.getParcelableExtra<Cart>("cart")
                if(cart == null){
                    mCart = Cart(
                        goodsId = id,
                        goodsPrice = price,
                        price = -1,
                        name = name,
                        imgUrl = imgUrl,
                        quantity = 1,
                        optionIds = "",
                        optionNames = "",
                        totalPrice = -1
                    )

                    setPrice(price)

                    getOptionList(optionCategoryIds, mCart)
                }else{
                    mCart = cart
                    setPrice(mCart?.price!!)
                    modifyCart(mCart)

                }

                binding.optionTv.visibility = if(optionCategoryIds.isNotEmpty()) View.VISIBLE else View.GONE

            }


            onSetCart.observe(this@GoodsDetailActivity){
                mCart = it
            }



            onPrice.observe(this@GoodsDetailActivity){
                mCart?.totalPrice = it
                totalPriceField.set(getMoneyComma(it) + getString(R.string.currency))
            }

            onRestartTimer.observe(this@GoodsDetailActivity){
                mCountDownTimer?.cancel()
                mCountDownTimer?.start()
            }

            onPauseTimer.observe(this@GoodsDetailActivity){
                mCountDownTimer?.cancel()
            }

            onShowOption.observe(this@GoodsDetailActivity){

                val intent = Intent(this@GoodsDetailActivity, OptionActivity::class.java)
                intent.putExtra("cart", mCart)
                intent.putExtra("setting", setting)
                intent.putExtra("goods", binding.goods)
                optionResultLauncher.launch(intent)
            }

            onSelectComplete.observe(this@GoodsDetailActivity){
                DebugUtils.setLog(TAG, "mPrice : $mPrice")
                mCart?.quantity = mCount
                mCart?.price = mPrice
                mCart?.totalPrice = mTotalPrice
                val intent = Intent()
                intent.putExtra("cart", mCart)
                intent.putExtra("position", this@GoodsDetailActivity.intent.getIntExtra("position", 0))
                setResult(RESULT_OK, intent)
                finish()
            }


            onSetCount.observe(this@GoodsDetailActivity){
                mCart?.quantity = it
            }
        }
    }

    private fun setCountDownTimer(millisInFuture: Long){
        mCountDownTimer?.cancel()
        mCountDownTimer = object : CountDownTimer(
            millisInFuture,
            AppConst.CLOSE_ORDER_SCREEN_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                setResult(TIME_OUT)
                finish()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        mCountDownTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        mCountDownTimer?.cancel()
    }
}
package kr.co.releasetech.kiosk.view.activity.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityMainBinding
import kr.co.releasetech.kiosk.kicc.setTransData
import kr.co.releasetech.kiosk.manager.ReceiptPrinterManager
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.goodsdetail.GoodsDetailActivity
import kr.co.releasetech.kiosk.view.activity.selectprint.SelectPrintActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.views.onClick
import kotlin.concurrent.thread


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private const val TAG = "MainActivity"
    }

    val viewModel: MainViewModel by viewModel()

    private var mCountDownTimer: CountDownTimer? = null

    private val cateAdapter: MainCategoryListAdapter by lazy {
        MainCategoryListAdapter(
            this,
            viewModel
        )
    }

    private val goodsAdapter: MainGoodsListAdapter by lazy {
        MainGoodsListAdapter(this, viewModel)
    }
    private val cartAdapter: MainCartListAdapter by lazy { MainCartListAdapter(this, viewModel) }


    private val selectResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val cart = it.data?.getParcelableExtra<Cart>("cart")
            DebugUtils.setLog(TAG, "cart : ${cart?.price}")
            cart?.let { cart ->
                cartAdapter.addItem(cart)
                if (cartAdapter.getList().size > 0) {
                    binding.cartLl.visibility = View.VISIBLE
                    binding.totalPriceLl.visibility = View.VISIBLE
                } else {
                    binding.cartLl.visibility = View.GONE
                    binding.totalPriceLl.visibility = View.GONE
                }

            }
        } else if (it.resultCode == TIME_OUT) {
            finish()
        }
    }


    private val modifyCartResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val cart = it.data?.getParcelableExtra<Cart>("cart")
                val position = it.data?.getIntExtra("position", 0)
                DebugUtils.setLog(TAG, "cart : $cart")
                cart?.let { cart ->
                    cartAdapter.modifyCart(cart, position!!)
                }
            } else if (it.resultCode == TIME_OUT) {
                finish()
            }
        }

    private val payResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                intent?.extras?.let { extras ->
                    val resultCode = extras.get("RESULT_CODE")

                    DebugUtils.setLog(TAG, "resultCode : $resultCode")
                    if (resultCode == "0000") {
                        viewModel.saveResult(extras)
                    } else {
                        binding.rl.visibility = View.GONE
                        val resultMessage = extras.get("RESULT_MSG").toString()
                        showToast(resultMessage)
                        viewModel.orderFailed(resultMessage)
                    }
                }
            }
        }

    private val printResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.extras?.let { extras ->
                    val payment = extras.getParcelable<Payment>("payment")
                    payment?.let {
                        thread(start = true) {
                            ReceiptPrinterManager(applicationContext).receiptPrint(
                                cartAdapter.getList(),
                                payment
                            )
                        }
                    }
                }
            }
            finish()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        val setting = intent.getParcelableExtra<ScreenSetting>("setting")
        setting?.let { extra ->
            setCountDownTimer(extra.orderScreenWaitSecond * 1000L)

            DebugUtils.setLog(TAG, "themeType : ${setting.themeType}")
            val themeColorResId =
                resources.getIdentifier("theme_${setting.themeType}", "color", packageName)
            val themeColor = ContextCompat.getColor(this@MainActivity, themeColorResId)
            DebugUtils.setLog(TAG, "themeColorResId : $themeColorResId")
            binding.goodsLl.setBackgroundColor(themeColor)
            Glide.with(this@MainActivity)
                .load(setting.logoImage)
                .into(binding.logoIv)

            binding.priceTv.setTextColor(themeColor)

            binding.paymentBt.setBackgroundResource(
                resources.getIdentifier(
                    "theme_btn_${setting.themeType}",
                    "drawable",
                    packageName
                )
            )

        }


        with(binding) {

            homeIv?.onClick {
                finish()
            }

            homeTv?.onClick {
                finish()
            }

            cateRv.adapter = cateAdapter
            goodsRv.adapter = goodsAdapter
            cartRv.adapter = cartAdapter


            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    val gridLayoutManager = GridLayoutManager(this@MainActivity, 4)
                    goodsRv.layoutManager = gridLayoutManager
                }
                Configuration.ORIENTATION_PORTRAIT -> {
                    val gridLayoutManager = GridLayoutManager(this@MainActivity, 3)
                    goodsRv.layoutManager = gridLayoutManager
                }
            }



            paymentBt.setOnClickListener {
                if (cartAdapter.getBasicItemCount() > 0) {
                    rl.visibility = View.VISIBLE
                    rl.setOnClickListener { }

                    viewModel.order(
                        cartAdapter.getList(),
                        intent.getBooleanExtra("isTakeout", false)
                    )
                } else {
                    showToast(R.string.empty_cart_item)
                }
            }
        }


        with(viewModel) {

            getCateList()
            BgColorField.set(setting?.bgColor)
            onRestartTimer.observe(this@MainActivity) {
                mCountDownTimer?.cancel()
                mCountDownTimer?.start()
            }

            onPauseTimer.observe(this@MainActivity) {
                mCountDownTimer?.cancel()
            }

            cateList.observe(this@MainActivity) { list ->
                DebugUtils.setLog(TAG, "list : ${list.size}")

                if (list.size > 0) {
                    cateAdapter.addList(list)
                    getGoodsList(list[0].id)
                } else {
                    binding.emptyRl.visibility = View.VISIBLE
                }

            }

            selectCate.observe(this@MainActivity) { cateId ->
                cateAdapter.onSelect(cateId)
            }

            goodsList.observe(this@MainActivity) {
                goodsAdapter.addList(it)
            }


            onOrder.observe(this@MainActivity) {
                DebugUtils.setLog(TAG, " order : $it")
                val intent = setTransData(it)
                payResultLauncher.launch(intent)
            }


            onShowSelect.observe(this@MainActivity) {
                val intent = Intent(this@MainActivity, GoodsDetailActivity::class.java)
                intent.putExtra("setting", setting)
                intent.putExtra("goods", it)
                selectResultLauncher.launch(intent)

            }

            onRemoveCart.observe(this@MainActivity) {
                cartAdapter.removeCart(it)
                if (cartAdapter.getBasicItemCount() > 0) {
                    binding.cartLl.visibility = View.VISIBLE
                    binding.totalPriceLl.visibility = View.VISIBLE
                } else {
                    binding.cartLl.visibility = View.GONE
                    binding.totalPriceLl.visibility = View.GONE
                }
            }

            onDbError.observe(this@MainActivity) {
                showToast(R.string.db_error_message)

            }

            onPayment.observe(this@MainActivity) {
                binding.rl.visibility = View.GONE

                thread(true) {
                    ReceiptPrinterManager(applicationContext).waitingNumberPrint(
                        cartAdapter.getList(),
                        it.orderId
                    )
                    //ReceiptPrinterManager(this@MainActivity).kitchenPrint(intent.getBooleanExtra("isTakeout", false), cartAdapter.getList(), it.orderId)
                }

                val intent = Intent(this@MainActivity, SelectPrintActivity::class.java)
                intent.putExtra("setting", setting)
                intent.putExtra("payment", it)
                printResult.launch(intent)
            }

            freeOrder.observe(this@MainActivity) {
                thread {
                    //ReceiptPrinterManager(this@MainActivity).kitchenPrint(intent.getBooleanExtra("isTakeout", false), cartAdapter.getList(), it.orderId)
                }
            }

            onAllRemoveCart.observe(this@MainActivity) {
                cartAdapter.allRemoveCart()
                binding.cartLl.visibility = View.GONE
                binding.totalPriceLl.visibility = View.GONE
            }

            onModifyCart.observe(this@MainActivity) {
                val goods = it["goods"] as Goods
                val cart = it["cart"] as Cart
                val position = it["position"] as Int

                val intent = Intent(this@MainActivity, GoodsDetailActivity::class.java)
                intent.putExtra("setting", setting)
                intent.putExtra("goods", goods)
                intent.putExtra("cart", cart)
                intent.putExtra("position", position)


                modifyCartResultLauncher.launch(intent)

            }

        }

    }

    private fun setCountDownTimer(millisInFuture: Long) {
        mCountDownTimer?.cancel()
        mCountDownTimer = object : CountDownTimer(
            millisInFuture,
            AppConst.CLOSE_ORDER_SCREEN_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {
                DebugUtils.setLog(TAG, "onTick")
            }

            override fun onFinish() {
                finish()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        mCountDownTimer?.cancel()
        mCountDownTimer?.start()
    }


    override fun onPause() {
        super.onPause()
        mCountDownTimer?.cancel()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
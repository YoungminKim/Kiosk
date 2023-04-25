package kr.co.releasetech.kiosk.view.activity.orderdetail

import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityOrderDetailBinding
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderDetailActivity: BaseActivity<ActivityOrderDetailBinding>(R.layout.activity_order_detail) {
    companion object{
        private const val TAG = "OrderDetailActivity"
    }
    val viewModel: OrderDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter =  OrderDetailAdapter(this)
        val orderId = intent.getIntExtra("orderId", 0)

        with(binding){
            lifecycleOwner = this@OrderDetailActivity
            vm = viewModel

            rv.adapter = adapter
        }

        with(viewModel){
            getCartList(orderId)
            onCartList.observe(this@OrderDetailActivity){
                DebugUtils.setLog(TAG, "list: ${it.size}")
                adapter.addList(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
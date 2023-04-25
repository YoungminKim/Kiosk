package kr.co.releasetech.kiosk.view.activity.order

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityOrderBinding
import kr.co.releasetech.kiosk.utils.DateUtils.getDateText
import kr.co.releasetech.kiosk.utils.DateUtils.getDateTime
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.orderdetail.OrderDetailActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start
import splitties.views.onClick
import java.util.*

class OrderActivity: BaseActivity<ActivityOrderBinding>(R.layout.activity_order) {
    companion object{
        const val TAG = "OrderFragment"
    }
    val viewModel: OrderViewModel by viewModel()
    private var startCal = Calendar.getInstance()
    private var endCal = Calendar.getInstance()

    private val adapter: OrderAdapter by lazy {
        OrderAdapter(this, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        with(binding){
            includeTitleV.title = getString(R.string.manager_menu_06)
            includeTitleV.backIv.onClick{
                finish()
            }

            val nowDateStr = getDateText(System.currentTimeMillis())
            startTv.text = nowDateStr
            endTv.text = nowDateStr

            startTv.onClick{
                setDatePickerDialog(startTv, startCal)
            }

            startIv.onClick {
                setDatePickerDialog(startTv, startCal)
            }

            endTv.onClick{
                setDatePickerDialog(endTv, endCal)
            }
            endIv.onClick {
                setDatePickerDialog(endTv, endCal)
            }

            resetIv.onClick{
                startCal = Calendar.getInstance()
                endCal = Calendar.getInstance()
                viewModel.searchField.set("")
                startTv.text = nowDateStr
                endTv.text = nowDateStr

            }
            searchIv.onClick{
                val start =  "${startCal.get(Calendar.YEAR)}${startCal.get(Calendar.MONTH)}${startCal.get(Calendar.DAY_OF_MONTH)}"
                val end =  "${endCal.get(Calendar.YEAR)}${endCal.get(Calendar.MONTH)}${endCal.get(Calendar.DAY_OF_MONTH)}"
                if(start > end){
                    showToast(R.string.can_not_large_endate)
                }else{
                    viewModel.search(getDateTime(startTv.text.toString() + " 00:00:00"), getDateTime(endTv.text.toString() + " 23:59:59"))
                }
            }

            rv.adapter = adapter
        }

        with(viewModel){

            search(getDateTime(binding.startTv.text.toString() + " 00:00:00"), getDateTime(binding.endTv.text.toString() + " 23:59:59"))
            orderList.observe(this@OrderActivity) {
                DebugUtils.setLog(TAG, "size : ${it.size}")
                adapter.addList(it)
            }

            onOderDetail.observe(this@OrderActivity){ orderId ->
                start<OrderDetailActivity> {
                    putExtra("orderId", orderId)
                }
            }
        }
    }


    private fun setDatePickerDialog(tv: TextView, cal: Calendar){
        val dialog = DatePickerDialog(
            this@OrderActivity,
            { view, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth)
                tv.text = "$year-${String.format("%02d", month + 1)}-${
                    String.format(
                        "%02d",
                        dayOfMonth
                    )
                }"

            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis() + 1000
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getList()
    }
}
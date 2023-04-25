package kr.co.releasetech.kiosk.view.activity.payment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityPaymentBinding
import kr.co.releasetech.kiosk.utils.DateUtils
import kr.co.releasetech.kiosk.utils.DateUtils.getApprovalDate
import kr.co.releasetech.kiosk.utils.DateUtils.getDateText
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.paymentdetail.PaymentDetailActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start
import splitties.views.onClick
import java.util.*


class PaymentActivity : BaseActivity<ActivityPaymentBinding>(R.layout.activity_payment) {
    companion object {
        private const val TAG = "PaymentFragment"
    }

    val viewModel: PaymentViewModel by viewModel()

    private var startCal = Calendar.getInstance()
    private var endCal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        val adapter = PaymentAdapter(this, viewModel)

        with(binding) {
            includeTitleV.title = getString(R.string.manager_menu_07)
            includeTitleV.backIv.onClick {
                finish()
            }

            approvalBt.isSelected = true




            val nowDateSTr = getDateText(System.currentTimeMillis())
            startTv.text = nowDateSTr
            endTv.text = nowDateSTr

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

                startTv.text = nowDateSTr
                endTv.text = nowDateSTr
            }

            searchIv.onClick {
                val start =  startTv.text.toString().replace("-", "").toInt()
                val end =  endTv.text.toString().replace("-", "").toInt()
                if(start > end){
                    showToast(R.string.can_not_large_endate)
                }else{
                    DebugUtils.setLog(TAG, "timeInMillis: ${startCal.timeInMillis} week : ${startCal.get(Calendar.DAY_OF_WEEK) - 1}")
                    DebugUtils.setLog(TAG, getDateText(startCal.timeInMillis))

                    val startApprovalDate = "${getApprovalDate(startCal.timeInMillis)}000000${startCal.get(Calendar.DAY_OF_WEEK) - 1}"
                    val endApprovalDate = "${getApprovalDate(endCal.timeInMillis)}235959${endCal.get(Calendar.DAY_OF_WEEK) - 1}"
                    viewModel.search(startApprovalDate.toLong(), endApprovalDate.toLong())
                }

            }


            rv.adapter = adapter
        }


        with(viewModel) {
            paymentList.observe(this@PaymentActivity) {
                DebugUtils.setLog(TAG, "size: ${it.size}")
                adapter.addList(it)
            }

            onPaymentDetail.observe(this@PaymentActivity){
                start<PaymentDetailActivity> {
                    putExtra("item", it)
                }
            }

            onIsApproval.observe(this@PaymentActivity){
                binding.approvalBt.isSelected = it
                binding.refundBt.isSelected = !it
            }
        }
    }

    private fun setDatePickerDialog(tv: TextView, cal: Calendar){
        val dialog = DatePickerDialog(
            this@PaymentActivity,
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
        dialog.datePicker.minDate
        dialog.datePicker.maxDate = System.currentTimeMillis() + 1000
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        val startApprovalDate = "${getApprovalDate(startCal.timeInMillis)}000000${startCal.get(Calendar.DAY_OF_WEEK) - 1}"
        val endApprovalDate = "${getApprovalDate(endCal.timeInMillis)}235959${endCal.get(Calendar.DAY_OF_WEEK) - 1}"
        viewModel.search(startApprovalDate.toLong(), endApprovalDate.toLong())
        //viewModel.getList()
    }

}
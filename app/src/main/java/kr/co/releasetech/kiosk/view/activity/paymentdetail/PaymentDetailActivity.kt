package kr.co.releasetech.kiosk.view.activity.paymentdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityPaymentDetailBinding
import kr.co.releasetech.kiosk.kicc.MakePrintMessage.receiptPrint
import kr.co.releasetech.kiosk.kicc.setTransData
import kr.co.releasetech.kiosk.manager.ReceiptPrinterManager
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.TextUtils
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentDetailActivity: BaseActivity<ActivityPaymentDetailBinding>(R.layout.activity_payment_detail) {
    companion object{
        private const val TAG = "PaymentDetailActivity"
    }

    val viewModel: PaymentDetailViewModel by viewModel()


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
                        viewModel.refundFailed(resultMessage)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val payment = intent.getParcelableExtra<Payment>("item")

        with(binding){
            lifecycleOwner = this@PaymentDetailActivity
            vm = viewModel
            item = payment

            payment?.let { payment ->
                if(payment.tranType == "D4") {
                    transTypeTv.text = getString(R.string.refund)
                    refundBt.visibility = View.GONE

                }else{
                    transTypeTv.text = getString(R.string.approval)
                    refundBt.visibility = if(payment.isRefund) View.GONE else View.VISIBLE
                }

                payment.totalAmount?.let { totalAmount -> totalAmountTv.text = TextUtils.getMoneyComma(totalAmount.toInt()) + getString(R.string.currency) }
                payment.tax?.let { tax -> taxTv.text =  TextUtils.getMoneyComma(tax.toInt()) + getString(R.string.currency) }

                installmentTv.text =  if(payment.installment == "0") getString(R.string.single_payment) else String.format(payment.installment.toString(), getString(R.string.how_month))
                
            }
           

        }
        with(viewModel){
            onRefundRequest.observe(this@PaymentDetailActivity){
                val intent = setTransData(it)
                payResultLauncher.launch(intent)
            }


            onClose.observe(this@PaymentDetailActivity){
                finish()
            }

            onRefundFailed.observe(this@PaymentDetailActivity){
                showToast(it)
            }

            onRefund.observe(this@PaymentDetailActivity){
                setInRefund(payment!!.id)
            }

            onRefundSuccess.observe(this@PaymentDetailActivity){
                showToast(R.string.refund_success)
                finish()
            }

            onDbError.observe(this@PaymentDetailActivity){
                showToast(R.string.db_error_message)
            }

            onPrint.observe(this@PaymentDetailActivity){
                val cartList = it["cart"] as ArrayList<Cart>
                val payment = it["payment"] as Payment
                ReceiptPrinterManager(applicationContext).receiptPrint(cartList, payment)
            }

        }
    }
}
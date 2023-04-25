package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.realm.Order
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

class PaymentRepository : KoinComponent {
    companion object {
        private const val TAG = "PaymentRepository"
    }

    fun getPaymentList(realm: Realm): RealmResults<Payment> =
        realm.where(Payment::class.java).sort("id", Sort.DESCENDING).findAll()


    fun getPaymentList(
        realm: Realm,
        startDate: Long,
        endDate: Long,
        transType: String = "D1"
    ): RealmResults<Payment> =
        realm.where(Payment::class.java).equalTo("tranType", transType)
            .between("approvalDate", startDate, endDate)
            .sort("approvalDate", Sort.DESCENDING).findAll()

    fun getPayment(realm: Realm, id: Int): Payment? =
        realm.where(Payment::class.java).equalTo("id", id).findFirst()

    private fun getPaymentMaxId(realm: Realm): Int {
        val maxId = realm.where(Payment::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }


    fun addPayment(realm: Realm, payment: Payment, onSuccess: ((Boolean) -> Unit)) {
        payment.id = getPaymentMaxId(realm)

        realm.executeTransactionAsync(
            {
                it.copyToRealm(payment)
            },
            {
                DebugUtils.setLog(TAG, "addPayment success")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "addPayment failed")
                onSuccess.invoke(false)
            }
        )
    }

    fun updateIsRefund(realm: Realm, id: Int, onSuccess: ((Boolean) -> Unit)) {
        realm.executeTransactionAsync(
            {
                it.where(Payment::class.java).equalTo("id", id).findFirst()?.let { payment ->
                    payment.isRefund = true
                }
            },
            {
                DebugUtils.setLog(TAG, "updateIsRefund success")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "updateIsRefund failed")
                onSuccess.invoke(false)
            }
        )
    }

}
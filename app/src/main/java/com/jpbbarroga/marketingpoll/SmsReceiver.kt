package com.jpbbarroga.marketingpoll

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

/**
 * Created by jeppie on 13/05/2018.
 */


class SmsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

//        val smsBundle = intent.extras
//
//        if (smsBundle != null) {
//            val pdus = smsBundle.get("pdus") as Array<Any>
//            for (onePdus : Any in pdus) {
//                val oneSMS = SmsMessage.createFromPdu(onePdus as ByteArray)
//                str += "SMS from " +oneSMS.originatingAddress
//                str += " :"
//                str += oneSMS.messageBody.toString()
//                str += "\n"
//            }
//          //  Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
//        }
    }
}
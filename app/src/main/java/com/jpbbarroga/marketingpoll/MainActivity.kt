package com.jpbbarroga.marketingpoll

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsMessage
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import java.util.jar.Manifest

//data class Person(val name: String, val color: String, var numVotes: Int)

enum class Person(val firstName: String, val color: String, var numVotes: Int) {
    HANS("hans", "blue", 0),
    LEAN("lean", "blue", 0),
    MIKKO("mikko", "blue", 0),
    UNDETERMINED("none", "none", 0)
}
class MainActivity : AppCompatActivity() {

    private lateinit var smsReceiver: BroadcastReceiver

    private lateinit var listView: ListView

    private var totalVotes: Int = 0

    private val MAX_VOTES = 20

    private val PERMISSIONS_REQUEST_READ_SMS = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureListView()
        configureReceiver()

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                requestSmsPermission()
            }
        } else {
            readSms()
        }
    }

    private fun configureListView() {

        listView = findViewById<ListView>(R.id.poll_list_view)
        val adapter = PollAdapter(this, generateData())
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun generateData(): ArrayList<Person> {
        var result = ArrayList<Person>()

        result.add(Person.HANS)
        result.add(Person.LEAN)
        result.add(Person.MIKKO)

        return result
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), PERMISSIONS_REQUEST_READ_SMS)

    }
    fun readSms() {
        val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)

        if (cursor!!.moveToFirst()) { // must check the result to prevent exception
            do {
                var msgData = ""
                for (idx in 0 until cursor.columnCount) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx)

                }
                // use msgData
            } while (cursor.moveToNext())
        } else {
            // empty box, no SMS
        }
    }

    private fun configureReceiver() {

        smsReceiver = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val cont = context?.let { it } ?: return
                val i = intent?.let { it } ?: return

                val smsBundle = i.extras
                if (smsBundle != null) {
                    val pdus = smsBundle.get("pdus") as Array<Any>
                    for (onePdus : Any in pdus) {
//                        val oneSMS = SmsMessage.createFromPdu(onePdus as ByteArray)
//                        str += "SMS from " +oneSMS.originatingAddress
//                        str += " :"
//                        str += oneSMS.messageBody.toString()
//                        str += "\n"
                        Flashbar.Builder(this@MainActivity)
                                .gravity(Flashbar.Gravity.BOTTOM)
                                .title("HELLO WORLD")
                                .message("YOU RECEIVED A NEW TEXT MESSAGE")
                                .backgroundColorRes(R.color.colorAccent)
                                .showOverlay().enterAnimation(FlashAnim.with(cont)
                                .animateBar()
                                .duration(750)
                                .alpha()
                                .overshoot())
                                .exitAnimation(FlashAnim.with(cont)
                                        .animateBar()
                                        .duration(400)
                                        .accelerateDecelerate())
                                .build()
                    }
               }
            }
        }

        registerReceiver(smsReceiver,  IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))


    }

    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }
}

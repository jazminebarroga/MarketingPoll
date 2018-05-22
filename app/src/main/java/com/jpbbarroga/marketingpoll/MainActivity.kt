package com.jpbbarroga.marketingpoll

import android.content.*
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsMessage
import android.view.LayoutInflater
import android.widget.ListView
import android.widget.SeekBar
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim

//data class Person(val name: String, val color: String, var numVotes: Int)

enum class Person(val firstName: String, val thumb: Int, val color: Int, var numVotes: Int) {
    HANS("Hans", R.drawable.liam, R.color.blueMaterial, 0),
    LEAN("Lean", R.drawable.robert, R.color.violetMaterial, 0),
    MIKKO("Mikko", R.drawable.chris, R.color.orangeMaterial, 0),
    UNDETERMINED("none", -1, -1, 0)
}

class MainActivity : AppCompatActivity() {

    private var seekbarView: SeekBar? = null

    private lateinit var smsReceiver: BroadcastReceiver

    private lateinit var listView: ListView

    private lateinit var adapter: PollAdapter

    private var totalVotes: Int = 0

    private val MAX_VOTES = 30

    private val PERMISSIONS_REQUEST_READ_SMS = 999

    private val data = arrayListOf<Person>(Person.HANS, Person.LEAN, Person.MIKKO)

    private val PREFS_FILENAME = "com.jpbbarroga.marketingpoll.prefs"

    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readSharedPref()
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
        }

    }

    private fun readSharedPref() {
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        for (person in data) {
            person.numVotes = prefs!!.getInt(person.firstName, 0)
        }
    }


    private fun configureListView() {

        listView = findViewById<ListView>(R.id.poll_list_view)
        addFooter()
        adapter = PollAdapter(this, data)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), PERMISSIONS_REQUEST_READ_SMS)
    }

    private fun parseSms(sms: String) {
        val dataPair = SmsParser.parseSms(sms)
        when (dataPair.first) {
            Person.HANS ->  {
                data[0].numVotes += 1
            }
            Person.LEAN -> {
                data[1].numVotes += 1
            }
            Person.MIKKO -> {
                data[2].numVotes += 1
            }
            Person.UNDETERMINED -> {
                return
            }

        }

        runOnUiThread {
            adapter.notifyDataSetChanged()
            val flash = notif(dataPair)
            flash.show()
        }
    }

    private fun addFooter() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.footer_view, null)

        listView.addFooterView(view)
    }

    private fun notif(dataPair: Pair<Person, String>): Flashbar {
       return  Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.TOP)
                .title("+1 for " + dataPair.first.firstName)
                .message("\"" + dataPair.second + "\"")
                .duration(3000)
                .backgroundColorRes(R.color.flashbarColor)
                .showIcon(0.8f)
                .icon(R.drawable.like)
                .iconAnimation(FlashAnim.with(this)
                        .animateIcon()
                        .pulse()
                        .alpha()
                        .duration(750)
                        .accelerate())
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build()
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
                        val sms = SmsMessage.createFromPdu(onePdus as ByteArray)

                        parseSms(sms.messageBody)

                    }
               }

            }
        }

        registerReceiver(smsReceiver,  IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    private fun writeSharedPref() {
        val editor = prefs!!.edit()

        for (person in data) {
            editor.putInt(person.firstName, person.numVotes)
        }
        editor.apply()
    }

    override fun onStop() {
        writeSharedPref()
        super.onStop()
    }

    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }


}

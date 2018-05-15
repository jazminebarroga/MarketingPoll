package com.jpbbarroga.marketingpoll

import android.content.IntentFilter
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.widget.ArrayAdapter
import android.widget.ListView

data class Person(val name: String, val color: String, var numVotes: Int)

class MainActivity : AppCompatActivity() {

    private var smsReceiver: SmsReceiver = SmsReceiver()

    private lateinit var listView: ListView

    private var totalVotes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureListView()
        configureReceiver()
        readSms()
    }

    private fun configureListView() {

        listView = findViewById<ListView>(R.id.poll_list_view)
        listView // 1
    //    val recipeList = Recipe.getRecipesFromFile("recipes.json", this)
        val teamList = arrayOf(
                Person("Sandy", "Blue", 0),
                Person("Chelo", "Blue", 0),
                Person("Hans", "Blue", 0),
                Person("Kristine", "Blue", 0),
                Person("Mikko", "Blue", 0),
                Person("Jeppie", "Blue", 0),
                Person("Jane", "Blue", 0),
                Person("Lean", "Blue", 0))

        val listItems = arrayOfNulls<String>(teamList.size)

        for (i in 0 until teamList.size) {
            val member = teamList[i]
            listItems[i] = member.name
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
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
        registerReceiver(smsReceiver,  IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))

    }

    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }
}

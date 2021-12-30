@file:Suppress("DEPRECATION")
package com.glass.cienciageek.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.glass.cienciageek.R
import com.glass.cienciageek.data.FirebaseApi
import com.glass.cienciageek.data.network.RetrofitClientInstance
import com.glass.cienciageek.ui.BaseActivity
import com.glass.cienciageek.utils.General.TOPIC_ENGLISH
import com.glass.cienciageek.utils.General.TOPIC_SPANISH
import com.glass.cienciageek.utils.extensions.getAsText
import kotlinx.android.synthetic.main.activity_notifications.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class NotificationsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notifications)
        setupSpinners()

        btnSend.setOnClickListener {
            sendNotification()
        }
    }

    /**
     * Set up the spinners for notification type and notification language.
     */
    private fun setupSpinners() {
        val listTypes = listOf("Youtube")
        val typesAdapter = ArrayAdapter(this, R.layout.spinner_item_simple, listTypes)

        val listLanguages = listOf(resources.getString(R.string.popup_language_spanish), resources.getString(R.string.popup_language_english))
        val languagesAdapter = ArrayAdapter(this, R.layout.spinner_item_simple, listLanguages)

        spinnerType.adapter = typesAdapter
        spinnerLanguage.adapter = languagesAdapter

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                when(pos) {
                    0 -> { // spanish
                        etNotificationTitle.setText(resources.getString(R.string.notification_title_desc_esp))
                        etNotificationMessage.setText(resources.getString(R.string.notification_message_desc_esp))
                    }
                    1 -> { // english
                        etNotificationTitle.setText(resources.getString(R.string.notification_title_desc_eng))
                        etNotificationMessage.setText(resources.getString(R.string.notification_message_desc_eng))
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /**
     * Sample snippet to send the current notification with values from edittext.
     */
    private fun sendNotification() {
        progress.visibility = View.VISIBLE

        val url = etNotificationUrl.getAsText()
        val topic = when(spinnerLanguage.selectedItemPosition){
            0 -> TOPIC_SPANISH
            else -> TOPIC_ENGLISH
        }

        val data = JSONObject().apply {
            put("title", etNotificationTitle.getAsText())
            put("body", etNotificationMessage.getAsText())

            if(url.isNotEmpty()) {
                put("link", url)
            }
        }

        val json = JSONObject().apply {
            put("to", "/topics/$topic")
            put("collapse_key", "type_a")
            put("data", data)
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(json.toString()).toString()
        )

        val service = RetrofitClientInstance.retrofitInstance?.create(FirebaseApi::class.java)
        val call = service?.sendPushNotification(body)

        call?.enqueue(object : retrofit2.Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Toast.makeText(this@NotificationsActivity, resources.getString(R.string.notification_sent_success, topic), Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("--", "Error = ${t.message}")
                Toast.makeText(this@NotificationsActivity, resources.getString(R.string.notification_sent_fail), Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }
        })
    }
}
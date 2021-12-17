package com.glass.cienciageek.ui.main

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.glass.cienciageek.R
import com.glass.cienciageek.entities.Language
import com.glass.cienciageek.entities.UrlEspEng
import com.glass.cienciageek.ui.BaseActivity
import com.glass.cienciageek.ui.content.ContentFragment
import com.glass.cienciageek.ui.notifications.NotificationsActivity
import com.glass.cienciageek.utils.General.TOPIC_ENGLISH
import com.glass.cienciageek.utils.General.TOPIC_SPANISH
import com.glass.cienciageek.utils.General.getLanguageApp
import com.glass.cienciageek.utils.General.saveLanguageApp
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splash.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setUpDrawerLayout()
        setUpNotificationTopics()
    }

    private fun setUpNotificationTopics() {
        val currentLanguage = getLanguageApp(this)
        val topicToSubscribe = if(currentLanguage == "Spanish") TOPIC_SPANISH else TOPIC_ENGLISH

        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_SPANISH)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_ENGLISH)

        FirebaseMessaging.getInstance().subscribeToTopic(topicToSubscribe)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.e("--", "Device subscribed to topic successfully")
                } else {
                    Log.e("--", "Could not subscribe device to topic")
                }
            }
    }

    private fun setUpDrawerLayout() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null

        val menuItem = navigationView.menu.getItem(0)
        onNavigationItemSelected(menuItem)
        menuItem.isChecked = true

        val header = navigationView.getHeaderView(0)
        val v = header.findViewById<TextView>(R.id.txtVersionMain)
        packageManager?.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES)?.apply {
            v.text = resources.getString(R.string.app_version, versionName)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val links = when(item.itemId) {
            R.id.nav_home -> UrlEspEng(title = resources.getString(R.string.menu_home), url = resources.getString(R.string.url_home))
            R.id.nav_nests -> UrlEspEng(title = resources.getString(R.string.menu_nests), url = resources.getString(R.string.url_nests))
            R.id.nav_farming -> UrlEspEng(title = resources.getString(R.string.menu_farming), url = resources.getString(R.string.url_farming))
            R.id.nav_community -> UrlEspEng(title = resources.getString(R.string.menu_community), url = resources.getString(R.string.url_community))
            R.id.nav_hour -> UrlEspEng(title = resources.getString(R.string.menu_hours), url = resources.getString(R.string.url_hours))
            else -> UrlEspEng(title = resources.getString(R.string.menu_news), url = resources.getString(R.string.url_news))
        }

        val args = Bundle().apply { putSerializable("links", links) }
        val fragment = ContentFragment().apply { arguments = args }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_content, fragment); commit()
        }

        title = "" // disable title on toolbar for all screens
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    @Suppress("DEPRECATION")
    private fun showDialogAdmin() {
        Dialog(this, R.style.FullDialogTheme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.popup_credentials)

            val username = findViewById<EditText>(R.id.etUser)
            val password = findViewById<EditText>(R.id.etPass)
            val button = findViewById<Button>(R.id.btnAccept)
            val error = findViewById<TextView>(R.id.txtError)

            // TODO: Validate admin credentials
            button.setOnClickListener {
                if(username.text.toString() == "" && password.text.toString() == "") {
                    error.visibility = View.GONE
                    startActivity(Intent(this@MainActivity, NotificationsActivity::class.java))
                    this.dismiss()
                } else {
                    error.visibility = View.VISIBLE
                    Handler().postDelayed({
                        error.visibility = View.GONE
                    }, 2500)
                }
            }
        }.show()
    }

    private fun showDialogLanguage() {
        Dialog(this, R.style.FullDialogTheme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.popup_language)

            val spinner = findViewById<Spinner>(R.id.spinner)
            val accept = findViewById<Button>(R.id.btnAccept)
            val adapter = LanguageAdapter(context)
            var language = resources.getString(R.string.popup_language_spanish)

            spinner.adapter = adapter
            spinner.setSelection(1)

            spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, p3: Long) {
                    language = (parent?.getItemAtPosition(pos) as Language).language
                }
            }

            accept.setOnClickListener {
                saveLanguageApp(context, language)
                dismiss(); finish(); startActivity(intent)
            }
        }.show()
    }

    /**
     * comment/uncomment the second line to show/hide the administrator menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_admin_menu, menu)
        //menu?.findItem(R.id.nav_admin)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_admin -> { showDialogAdmin(); true }
            R.id.nav_language -> { showDialogLanguage(); true }
            else -> true
        }
    }
}
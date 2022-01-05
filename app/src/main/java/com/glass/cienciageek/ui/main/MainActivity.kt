@file:Suppress("DEPRECATION")
package com.glass.cienciageek.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.glass.cienciageek.utils.extensions.getAsText
import com.glass.cienciageek.utils.extensions.getDialog
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

    /**
     * Here we define in which language the notification reaches each device
     * depending on the language it has defined.
     */
    private fun setUpNotificationTopics() {
        val currentLanguage = getLanguageApp(this)
        val topicToSubscribe = if(currentLanguage == "Spanish") TOPIC_SPANISH else TOPIC_ENGLISH

        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_SPANISH)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_ENGLISH)

        FirebaseMessaging.getInstance().subscribeToTopic(topicToSubscribe)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.e("--", "Device subscribed to topic $topicToSubscribe successfully")
                } else {
                    Log.e("--", "Could not subscribe device to topic $topicToSubscribe")
                }
            }
    }

    /**
     * Setup the side menu with custom animations, header, layouts, etc.
     */
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

    /**
     * What to do when back button (hardware) is pressed? -> Close side menu .
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Define what to do when each item from side menu is selected.
     * In this case, pass data to open correct url (english or spanish).
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val links = when(item.itemId) {
            R.id.nav_home -> UrlEspEng(title = resources.getString(R.string.menu_home), url = resources.getString(R.string.url_home))
            R.id.nav_nests -> UrlEspEng(title = resources.getString(R.string.menu_nests), url = resources.getString(R.string.url_nests))
            R.id.nav_farming -> UrlEspEng(title = resources.getString(R.string.menu_farming), url = resources.getString(R.string.url_farming))
            R.id.nav_community -> UrlEspEng(title = resources.getString(R.string.menu_community), url = resources.getString(R.string.url_community))
            R.id.nav_hour -> UrlEspEng(title = resources.getString(R.string.menu_hours), url = resources.getString(R.string.url_hours))
            R.id.nav_shiny -> UrlEspEng(title = resources.getString(R.string.menu_shiny_rates), url = resources.getString(R.string.url_shiny_rates))
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

    /**
     * Show popup for admin login and validate credentials to open new notifications screen.
     */
    private fun showDialogAdmin() {
        with(getDialog(this, R.layout.popup_credentials)) {
            val username = findViewById<EditText>(R.id.etUser)
            val password = findViewById<EditText>(R.id.etPass)
            val button = findViewById<Button>(R.id.btnAccept)
            val error = findViewById<TextView>(R.id.txtError)

            button.setOnClickListener {
                //if(username.getAsText() == "" && password.getAsText() == "") {
                if(username.getAsText() == "pokec00rd" && password.getAsText() == "M1gUelit080") {
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
        }
    }

    /**
     * Show popup for language and refresh App so changes can take effect.
     */
    private fun showDialogLanguage() {
        with(getDialog(this, R.layout.popup_language)) {
            val spinner = findViewById<Spinner>(R.id.spinner)
            val accept = findViewById<Button>(R.id.btnAccept)
            val adapter = LanguageAdapter(context)
            val txtWait = findViewById<TextView>(R.id.txtWait)
            var language = resources.getString(R.string.popup_language_spanish)

            spinner.adapter = adapter
            spinner.setSelection(1)

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, p3: Long) {
                    language = (parent?.getItemAtPosition(pos) as Language).language
                }
            }

            accept.setOnClickListener {
                txtWait.visibility = View.VISIBLE
                saveLanguageApp(context, language)

                Handler().postDelayed({
                    dismiss()
                    finish()
                    startActivity(intent)
                }, 1500)
            }
        }
    }

    /**
     * comment/uncomment the second line to show/hide the administrator menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_admin_menu, menu)
        //menu?.findItem(R.id.nav_admin)?.isVisible = false
        return true
    }

    /**
     * What to do when item is selected at top|right menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_admin -> { showDialogAdmin(); true }
            R.id.nav_language -> { showDialogLanguage(); true }
            else -> true
        }
    }
}
package com.glass.cienciageek.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.glass.cienciageek.R
import com.glass.cienciageek.entities.UrlEspEng
import com.glass.cienciageek.ui.content.ContentFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        val menuItem = navigationView.menu.getItem(0)
        onNavigationItemSelected(menuItem)
        menuItem.isChecked = true

        val header = navigationView.getHeaderView(0)
        header.setOnClickListener {
            // TODO
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
            R.id.nav_one -> UrlEspEng(title = "One", url = resources.getString(R.string.url_one))
            R.id.nav_two -> UrlEspEng(title = "Two", url = resources.getString(R.string.url_two))
            R.id.nav_three -> UrlEspEng(title = "Three", url = resources.getString(R.string.url_three))
            else -> UrlEspEng(title = "Four", url = resources.getString(R.string.url_four))
        }

        val fragment = ContentFragment()
        val args = Bundle().apply {
            putSerializable("links", links)
        }
        fragment.arguments = args

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_content, fragment)
            //addToBackStack(from?.name)
            commit()
        }

        title = ""
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_admin_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_admin -> {
                //TODO
                true
            }
            R.id.nav_language -> {
                //TODO
                true
            }
            else -> {
                true
            }
        }
    }
}
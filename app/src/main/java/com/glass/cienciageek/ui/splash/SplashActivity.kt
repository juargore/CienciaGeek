package com.glass.cienciageek.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.glass.cienciageek.R
import com.glass.cienciageek.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen", "SetTextI18n")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setCurrentVersionCode()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500)
    }

    private fun setCurrentVersionCode() {
        packageManager?.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES)?.apply {
            txtVersion.text = "Version $versionName"
        }
    }
}
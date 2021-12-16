package com.glass.cienciageek.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.glass.cienciageek.R
import com.glass.cienciageek.ui.BaseActivity
import com.glass.cienciageek.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val SPLASH_DURATION = 2700L

    /**
     * Set a theme to 'remove' the status bar and make it full screen.
     * Show the image and finish the screen after x seconds (defined on top variable).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        setAnimation()
        setCurrentVersionCode()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, SPLASH_DURATION)
    }

    /**
     * Set a zoom in animation to central image.
     * Set a zoom out animation to bottom layout (contains powered by and version code).
     */
    private fun setAnimation() {
        imgPokeCoord.animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        layBottom.animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
    }

    /**
     * Simple code to show the current App version at bottom screen.
     */
    private fun setCurrentVersionCode() {
        packageManager?.getPackageInfo(packageName,
            PackageManager.GET_ACTIVITIES)?.apply {
            txtVersion.text = resources.getString(R.string.app_version, versionName)
        }
    }
}
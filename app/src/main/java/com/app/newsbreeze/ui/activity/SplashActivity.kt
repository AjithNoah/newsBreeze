package com.app.newsbreeze.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.app.newsbreeze.R
import com.app.newsbreeze.util.Utils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_splash)

        Thread(Runnable {
            Thread.sleep(2000)
            val intent = Intent(this,NewsActivity::class.java)
            startActivity(intent)
            finish()
        }).start()
    }
}
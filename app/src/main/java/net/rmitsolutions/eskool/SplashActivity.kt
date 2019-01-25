package net.rmitsolutions.eskool

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Madhu on 09-Jun-2017.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }
}
package net.rmitsolutions.eskool

import android.content.Intent
import android.support.design.widget.TextInputEditText
import android.widget.Button
import net.rmitsolutions.eskool.dashboard.DashboardActivity
import org.jetbrains.anko.find
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows


/**
 * Created by Madhu on 23-Aug-17.
 */
@RunWith(RobolectricTestRunner::class)
//@Config(constants = BuildConfig::class)
class LoginUnitTest {

    @Test
    fun login() {
        val activity = Robolectric.setupActivity(WelcomeActivity::class.java)
        val userName = activity.find<TextInputEditText>(R.id.txtUserName)
        val pass = activity.find<TextInputEditText>(R.id.txtPass)
        val btn = activity.find<Button>(R.id.buttonLogin)

        userName.setText("1300165")
        pass.setText("test123")

        btn.performClick()
        val expectedIntent = Intent(activity, DashboardActivity::class.java)
        val shadowActivity = Shadows.shadowOf(activity)
        val actualIntent = shadowActivity.nextStartedActivity
        Assert.assertTrue(actualIntent.filterEquals(expectedIntent))
    }
}
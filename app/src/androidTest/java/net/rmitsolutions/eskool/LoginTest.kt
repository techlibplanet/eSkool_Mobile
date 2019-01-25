package net.rmitsolutions.eskool

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


/**
 * Created by Madhu on 13-Jun-2017.
 */
@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Test
    fun login() {
        onView(withId(R.id.txtUserName)).perform(typeText("1200006"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.txtPass)).perform(typeText("bushan123"), ViewActions.closeSoftKeyboard())
        onView(withText(R.string.login)).perform(click())
    }
}
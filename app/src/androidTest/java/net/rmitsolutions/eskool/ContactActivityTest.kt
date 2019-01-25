package net.rmitsolutions.eskool

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Madhu on 23-Aug-17.
 */

@RunWith(AndroidJUnit4::class)
class ContactActivityTest {
    private var context: Context? = null

    @Before
    fun setConfig() {
        context = InstrumentationRegistry.getTargetContext()
    }

    @After
    fun releaseConfig() {
        context = null
    }

    @Test
    fun deleteSchoolAddress() {
    }
}
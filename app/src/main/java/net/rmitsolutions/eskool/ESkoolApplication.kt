package net.rmitsolutions.eskool

import android.app.Application
import net.rmitsolutions.eskool.dependency.components.ApplicationComponent
import net.rmitsolutions.eskool.dependency.components.DaggerApplicationComponent
import net.rmitsolutions.eskool.dependency.modules.AppContextModule
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by Madhu on 09-Jun-2017.
 */
class ESkoolApplication : Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        applicationComponent = DaggerApplicationComponent.builder()
                .appContextModule(AppContextModule(applicationContext))
                .build()

        //set default font
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Muli_Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }
}
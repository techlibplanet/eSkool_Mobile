package net.rmitsolutions.eskool

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics
import net.rmitsolutions.eskool.assessments.AssessmentsFragment
import net.rmitsolutions.eskool.circulars.CircularsFragment
import net.rmitsolutions.eskool.fee.FeeFragment
import net.rmitsolutions.eskool.library.LibraryFragment
import net.rmitsolutions.eskool.timetable.TimeTableFragment
import net.rmitsolutions.eskool.transport.TransportFragment
import org.jetbrains.anko.find


/**
 * Created by Madhu on 04-Jul-2017.
 */
class ModuleActivity : AppCompatActivity() {
    lateinit private var fireBaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)
        fireBaseAnalytics = FirebaseAnalytics.getInstance(this)
        val toolbar = find<Toolbar>(R.id.toolbar_actionbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        window.reenterTransition = null
        val module = intent.getIntExtra("module", 0)

        Handler().post { toolbar.title = getString(module) }
        setModuleFragment(module)
    }

    private fun setModuleFragment(module: Int) {
        val fragment: Fragment? = when (module) {
            R.string.dashboard_module_circulars -> CircularsFragment.newInstance()
            R.string.dashboard_module_fee -> FeeFragment.newInstance()
            R.string.dashboard_module_library -> LibraryFragment.newInstance()
            R.string.dashboard_module_transport -> TransportFragment.newInstance()
            R.string.dashboard_module_assessments -> AssessmentsFragment.newInstance()
            R.string.dashboard_module_timetable -> TimeTableFragment.newInstance()
            else -> null
        }
        supportFragmentManager.beginTransaction().replace(R.id.module_fragment_container, fragment!!).commit()
    }
}
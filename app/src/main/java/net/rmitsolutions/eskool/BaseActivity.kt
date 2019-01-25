package net.rmitsolutions.eskool

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.eskool.Changepassword.ChangePassword
import net.rmitsolutions.eskool.about.AboutActivity
import net.rmitsolutions.eskool.contact.ContactActivity
import net.rmitsolutions.eskool.dashboard.DashboardActivity
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.downloads.DownloadsActivity
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.Student
import net.rmitsolutions.eskool.network.IStudent
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * Created by Madhu on 09-Jun-2017.
 */
abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    @Inject
    lateinit var studentService: IStudent

    companion object {
        private var backPressedTime: Long = 0
        private const val TAG = "BaseActivity"

        fun getStudentProfileImageGlideOptions(): RequestOptions {
            val glOpts = RequestOptions()
            glOpts.error(R.drawable.ic_student_large)
            glOpts.fallback(R.drawable.ic_student_large)
            glOpts.circleCrop()
            return glOpts
        }

        fun getStudentProfileImageGlideUri(token: String): GlideUrl {
            val headers = LazyHeaders.Builder().addHeader("Authorization", token).build()
            return GlideUrl(Constants.STUDENT_PROFILE_IMAGE_URL, headers)
        }
    }

    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private val MAIN_CONTENT_FADEOUT_DURATION = 100
    private val MAIN_CONTENT_FADEIN_DURATION = 200
    private val NAVDRAWER_LAUNCH_DELAY = 250

    // Primary toolbar
    private var actionBarToolbar: Toolbar? = null
    private var mainContent: View? = null

    lateinit private var handler: Handler
    lateinit internal var compositeDisposable: CompositeDisposable
    lateinit internal var fireBaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        handler = Handler()
        compositeDisposable = CompositeDisposable()
        fireBaseAnalytics = FirebaseAnalytics.getInstance(this)

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()

        depComponent.injectBaseActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        setupNavDrawer()
        setupAccount()

        mainContent = findViewById(R.id.main_content)
        if (mainContent != null) {
            mainContent!!.alpha = 0f
            mainContent!!.animate().alpha(1f).duration = MAIN_CONTENT_FADEIN_DURATION.toLong()
        } else {
            Log.d(TAG, "No view with ID main_content to fade in.")
        }
    }

    override fun onResume() {
        super.onResume()
        navigationView?.setCheckedItem(getSelfNavDrawerItem())
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        getActionBarToolbar()
    }

    override fun onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer()
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Snackbar.make(mainContent!!, "Tap back again to exit", Snackbar.LENGTH_SHORT).show()
            }

            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = super.onOptionsItemSelected(item)

    open fun getSelfNavDrawerItem(): Int = -1

    protected fun getActionBarToolbar(): Toolbar {
        if (actionBarToolbar == null) {
            actionBarToolbar = find<Toolbar>(R.id.toolbar_actionbar)
            if (actionBarToolbar != null) {
                actionBarToolbar!!.navigationContentDescription = resources.getString(R.string.nav_drawer_description)
                setSupportActionBar(actionBarToolbar)
            }
        }
        return actionBarToolbar!!
    }

    private fun setupNavDrawer() {
        drawerLayout = findOptional(R.id.drawer_layout)
        if (drawerLayout == null) {
            return
        }
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, actionBarToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = find<NavigationView>(R.id.nav_view)
        navigationView!!.setNavigationItemSelectedListener(this)
    }

    protected fun isNavDrawerOpen(): Boolean = drawerLayout != null && drawerLayout?.isDrawerOpen(GravityCompat.START) ?: false

    protected fun closeNavDrawer() = drawerLayout?.closeDrawer(GravityCompat.START)

    private fun setupAccount() {
        if (Constants.studentModel == null) {
            val student = getPref(SharedPrefKeys.STUDENT_KEY, "")
            if (student.isNullOrEmpty()) {
                getStudentInfo()
            } else {
                Constants.studentModel = JsonHelper.jsonToKt<Student>(student)
                setAccountViews(Constants.studentModel!!)
            }
        } else {
            setAccountViews(Constants.studentModel!!)
        }
    }

    private fun getStudentInfo() {
        compositeDisposable.add(studentService.getStudent(apiAccessToken)
                .processRequest(
                        { student ->
                            Constants.studentModel = student
                            putPref(SharedPrefKeys.STUDENT_KEY, JsonHelper.KtToJson(student))
                            setAccountViews(student)
                        },
                        { errMsg ->
                            toast("$errMsg")
                            logE(errMsg)
                        }
                ))
    }

    private fun setAccountViews(sm: Student) {
        if (navigationView == null) return

        val header = navigationView!!.getHeaderView(0)
        val image = header.find<ImageView>(R.id.profile_image)
        val name = header.find<TextView>(R.id.profile_name_text)
        val grade = header.find<TextView>(R.id.profile_grade_text)

        val uri = getStudentProfileImageGlideUri(apiAccessToken)
        val glOpts = getStudentProfileImageGlideOptions()
        Glide.with(this).load(uri).apply(glOpts).into(image)

        name.text = sm.name
        grade.text = getString(R.string.student_class_text, sm.code, sm.syllabus, sm.grade, sm.section)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == getSelfNavDrawerItem()) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            return true
        }

        handler.postDelayed({ goToNavViewItem(item.itemId) }, NAVDRAWER_LAUNCH_DELAY.toLong())

        // fade out the main content
        mainContent?.animate()?.alpha(0f)?.duration = MAIN_CONTENT_FADEOUT_DURATION.toLong()

        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    private fun goToNavViewItem(itemId: Int) {
        when (itemId) {
            R.id.nav_reqel_board -> startActivity<DashboardActivity>()
            R.id.nav_downloads -> startActivity<DownloadsActivity>()
            R.id.nav_contact -> startActivity<ContactActivity>()
        //R.id.nav_settings -> Log.d(TAG, "Settings clicked")
            R.id.nav_about -> startActivity<AboutActivity>()
            R.id.change_password -> startActivity<ChangePassword>()
            R.id.nav_logout -> logout()
        }
        finishNoAnim()
    }

    private fun logout() {
        removePref(SharedPrefKeys.TOKEN_KEY,
                SharedPrefKeys.TOKEN_OBJECT_KEY,
                SharedPrefKeys.STUDENT_KEY,SharedPrefKeys.FIRST_LAUNCH)
        Constants.studentModel = null
        Constants.studentProfile = null
        startActivity<WelcomeActivity>()
    }
}
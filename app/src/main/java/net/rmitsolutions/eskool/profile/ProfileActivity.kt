package net.rmitsolutions.eskool.profile

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.helpers.apiAccessToken
import net.rmitsolutions.eskool.helpers.finishNoAnim
import org.jetbrains.anko.find

/**
 * Created by Madhu on 03-Jul-2017.
 */
class ProfileActivity() : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = find<Toolbar>(R.id.toolbar_actionbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            finishNoAnim()
        }

        window.reenterTransition = null
        setStudentProfileImage()
        setTabs()
    }

    override fun onBackPressed() {
        finishNoAnim()
    }

    private fun setTabs() {
        val pagerAdapter = ProfileFragmentPagerAdapter(supportFragmentManager)
        val viewPager = find<ViewPager>(R.id.profile_tab_pager)
        viewPager.adapter = pagerAdapter

        val tabLayout = find<TabLayout>(R.id.profile_tabs)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.icon = getDrawable(R.drawable.ic_user_account)
        tabLayout.getTabAt(1)?.icon = getDrawable(R.drawable.ic_grade_cap)
        tabLayout.getTabAt(2)?.icon = getDrawable(R.drawable.ic_human_male_female)
        tabLayout.getTabAt(3)?.icon = getDrawable(R.drawable.ic_home_map_marker)
    }

    private fun setStudentProfileImage() {
        val uri = getStudentProfileImageGlideUri(apiAccessToken)
        val glOpts = getStudentProfileImageGlideOptions()
        Glide.with(this).load(uri).apply(glOpts).into(student_profile_image)
    }

}
package net.rmitsolutions.eskool.info

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.WelcomeActivity
import net.rmitsolutions.eskool.helpers.SharedPrefKeys
import net.rmitsolutions.eskool.helpers.getPref
import net.rmitsolutions.eskool.helpers.removePref
import org.jetbrains.anko.find

class InformationSliders : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private lateinit var dotsLayout: LinearLayout
    private  var dots: Array<TextView?>? = null
    private lateinit var layouts: IntArray
    private lateinit var btnSkip: Button
    private  lateinit var btnNext:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.main_slider)

        var isFirstTimeLaunched:Boolean=getPref(SharedPrefKeys.FIRST_LAUNCH,true);

        if (!isFirstTimeLaunched) {
            launchHomeScreen()
            finish()
        }
        else{

          //  putPref("isFirstTimeAppLunched",false)
        }

        viewPager = find<ViewPager>(R.id.view_pager)
        dotsLayout = find<LinearLayout>(R.id.layoutDots)
        btnSkip = find<Button>(R.id.btn_skip)
        btnNext = find<Button>(R.id.btn_next)




        layouts = intArrayOf(R.layout.overview_slide, R.layout.play_slide, R.layout.classroom_slide, R.layout.daycare_slide)

        addBottomDots(0)
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()

        viewPager!!.setAdapter(myViewPagerAdapter)
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

    }

    fun onSkipClick(view: View) {
        removePref(SharedPrefKeys.FIRST_LAUNCH)
       launchHomeScreen()
    }

    fun OnNextClick(view: View) {
        val current = getItem(+1)
        if (current < layouts.size) {
            // move to next screen

            viewPager!!.setCurrentItem(current)

        } else {
           removePref(SharedPrefKeys.FIRST_LAUNCH)
            launchHomeScreen()
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)


        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 35f
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout.addView(dots!![i])
        }

        if (dots!!.size > 0)
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.getCurrentItem() + i
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }


        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }
    private fun launchHomeScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }


    inner class MyViewPagerAdapter : PagerAdapter()
    {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}

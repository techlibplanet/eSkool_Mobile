package net.rmitsolutions.eskool.info

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.gson.Gson
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.SliderFragmnent
import net.rmitsolutions.eskool.WelcomeActivity
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.ArrList
import net.rmitsolutions.eskool.network.IInfoSliders
import org.jetbrains.anko.find
import java.util.*
import javax.inject.Inject


class InfoSlidersActivity : AppCompatActivity() {

    var viewPager: ViewPager? = null
    var nextButton: Button? = null
    var skipButton:Button?=null
    private var compositeDisposable: CompositeDisposable? = null
    var dotsIndicator: DotsIndicator? = null
    lateinit var fragment:SliderFragmnent
    @Inject lateinit var sliderService: IInfoSliders

     var list: MutableList<ArrList>?=null
    private var adapter:ViewPagerAdapter?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_sliders)
        compositeDisposable = CompositeDisposable()

        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()

        depComponent.injectInfoSlidersActivity(this)

        viewPager = find<ViewPager>(R.id.pager)
        nextButton = find<Button>(R.id.btn_next)
        skipButton=find<Button>(R.id.btn_skip)

        dotsIndicator = find<DotsIndicator>(R.id.dots_indicator)

        var isFirstTimeLaunched:Boolean=getPref(SharedPrefKeys.FIRST_LAUNCH,true);

        if (!isFirstTimeLaunched) {
            launchHomeScreen()
            finish()
        }
        else{

            //  putPref("isFirstTimeAppLunched",false)
        }

        adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        // frag1=SliderFragmnent();
        /* adapter!!.addFragment(frag1!!, "")
         adapter!!.addFragment(FragmentTwo(), "")
         adapter!!.addFragment(FragmentThree(), "")*/

        getInformationSliders()
      /*  viewPager!!.adapter = adapter
        dotsIndicator!!.setViewPager(viewPager);
*/

    }
    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
         //   addBottomDots(position)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == list!!.size - 1) {
                // last page. make button text to GOT IT
                nextButton!!.setText(getString(R.string.start));
                skipButton!!.setVisibility(View.GONE);
            } else {
                // still pages are left
                nextButton!!.setText(getString(R.string.next));
                skipButton!!.setVisibility(View.VISIBLE);
            }
        }


        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    private fun getInformationSliders() {
     //   showProgress()
        setProgress(true)
        compositeDisposable!!.add(sliderService.getArtistData().processRequest(
                {
                    info ->
              //      hideProgress()
                         //   Constants.infoModel = info

                    list= info.arrList
                            var gson =  Gson();
                            var json = gson.toJson(info);
                            //   overviewLayout.visibility=View.VISIBLE
                            Log.d("response",json);

                              for (data in this!!.list!!){
                          fragment= SliderFragmnent()
                                  fragment.setData1(data);

                                  adapter!!.addFragment(fragment, "")

                              }
                    setProgress(false)
                            viewPager!!.adapter = adapter
                            dotsIndicator!!.setViewPager(viewPager);

                        },
                        { errMsg ->
                            setProgress(false)
                            //hideProgress(true, errMsg)
                            logE(errMsg)
                        }
                ))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable!!.dispose()
    }


    fun onNextClick(view: View) {
        val current = getItem(+1)
        if (current < list!!.size) {
            // move to next screen

            viewPager!!.setCurrentItem(current)

        } else {
            removePref(SharedPrefKeys.FIRST_LAUNCH)
            launchHomeScreen()
        }

       // viewPager!!.setCurrentItem(viewPager!!.getCurrentItem() + 1, true);
    }

    fun onSkipClick(view: View) {
        removePref(SharedPrefKeys.FIRST_LAUNCH)
        launchHomeScreen()
    }
    private fun getItem(i: Int): Int {
        return viewPager!!.getCurrentItem() + i
    }
    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]

        }
    }
    private fun setProgress(show: Boolean) {
        if (show) {
            showProgress()
        } else {
            hideProgress()
        }
    }
    private fun launchHomeScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}

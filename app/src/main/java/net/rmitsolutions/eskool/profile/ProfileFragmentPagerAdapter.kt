package net.rmitsolutions.eskool.profile

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Madhu on 05-Jul-2017.
 */
class ProfileFragmentPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): ProfileFragment {
        return ProfileFragment.newInstance(position)
    }

    override fun getCount() = 4

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Personal"
        1 -> "Grade"
        2 -> "Parents"
        3 -> "Address"
        else -> ""
    }

}
package net.rmitsolutions.eskool.dependency.components

import dagger.Component
import net.rmitsolutions.eskool.SliderFragmnent
import net.rmitsolutions.eskool.assessments.AssessmentsFragment
import net.rmitsolutions.eskool.circulars.CircularsFragment
import net.rmitsolutions.eskool.dependency.scopes.ActivityScope
import net.rmitsolutions.eskool.fee.FeeFragment
import net.rmitsolutions.eskool.library.LibraryFragment
import net.rmitsolutions.eskool.profile.ProfileFragment
import net.rmitsolutions.eskool.timetable.TimeTableFragment
import net.rmitsolutions.eskool.transport.TransportFragment

/**
 * Created by Madhu on 06-Jul-2017.
 */
@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface BaseFragmentComponent {
    fun injectProfileFragment(fragment: ProfileFragment)
    fun injectCircularsFragment(fragment: CircularsFragment)
    fun injectAssesmentsFragment(fragment: AssessmentsFragment)
    fun injectLibraryFragment(fragment: LibraryFragment)
    fun injectTransportFragment(fragment: TransportFragment)
    fun injectFeeFragment(fragment: FeeFragment)
    fun injectTimeTableFragment(fragment: TimeTableFragment)
    fun injectSliderFragment(fragment:SliderFragmnent)

}
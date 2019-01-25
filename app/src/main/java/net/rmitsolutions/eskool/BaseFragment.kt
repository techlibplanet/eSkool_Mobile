package net.rmitsolutions.eskool

import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.eskool.widgets.DatePickerDialogFragment

/**
 * Created by Madhu on 03-Jul-2017.
 */
abstract class BaseFragment : Fragment() {
    lateinit internal var fireBaseAnalytics: FirebaseAnalytics
    lateinit internal var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        fireBaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun showDateDialog() {
        val fragmentManager = fragmentManager
        val datePicker = DatePickerDialogFragment.newInstance()
        datePicker.show(fragmentManager, getString(R.string.date_picker_dialog_fragment_tag))
    }
}
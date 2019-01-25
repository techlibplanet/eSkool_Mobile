package net.rmitsolutions.eskool.widgets

import android.app.DatePickerDialog
import android.app.Dialog
import android.widget.DatePicker
import android.os.Bundle
import android.support.v4.app.DialogFragment
import net.rmitsolutions.eskool.helpers.toast
import java.util.*


/**
 * Created by Madhu on 07-Jul-2017.
 */
class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    companion object {
        fun newInstance(): DatePickerDialogFragment {
            return DatePickerDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        toast("$year - $month - $dayOfMonth")
    }
}
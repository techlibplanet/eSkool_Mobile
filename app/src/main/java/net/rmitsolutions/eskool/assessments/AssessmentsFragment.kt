package net.rmitsolutions.eskool.assessments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.include_status_view.*
import net.rmitsolutions.eskool.BaseFragment
import net.rmitsolutions.eskool.R

/**
 * Created by Madhu on 01-Aug-2017.
 */
class AssessmentsFragment : BaseFragment() {
    companion object {
        fun newInstance() = AssessmentsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_assessments, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        statusView.visibility = View.VISIBLE
    }
}
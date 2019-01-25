package net.rmitsolutions.eskool.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import net.rmitsolutions.eskool.BaseFragment
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseFragmentComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.network.ITimeTable
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Madhu on 01-Aug-2017.
 */
class TimeTableFragment : BaseFragment() {
    @Inject
    lateinit var timeTableService: ITimeTable
    private lateinit var timeTableImg: PhotoView

    companion object {
        fun newInstance() = TimeTableFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectTimeTableFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_timetable, container, false)
        timeTableImg = rootView.find<PhotoView>(R.id.timeTableImage)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getTimeTable()
    }

    private fun getTimeTable() {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(timeTableService.getTimeTable(apiAccessToken)
                .processRequest(
                        { result ->
                            hideProgress()
                            val opts = RequestOptions()
                            opts.fitCenter()
                            opts.dontAnimate()
                            Glide.with(this!!.context!!).setDefaultRequestOptions(opts)
                                    .load(result.bytes()).into(timeTableImg)
                        },
                        { message ->
                            hideProgress(true, message)
                        }
                ))
    }
}
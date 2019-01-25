package net.rmitsolutions.eskool.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import net.rmitsolutions.eskool.BaseFragment
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.databinding.TransportDataBinding
import net.rmitsolutions.eskool.dependency.components.DaggerBaseFragmentComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.network.ITransport
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Madhu on 29-Jul-2017.
 */
class TransportFragment : BaseFragment() {
    @Inject
    lateinit var transportService: ITransport
    lateinit var dataBinding: TransportDataBinding
    lateinit var containerView: LinearLayout

    companion object {
        fun newInstance() = TransportFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectTransportFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = TransportDataBinding.inflate(inflater!!, container, false)
        val rootView = dataBinding.root
        containerView = rootView.find<LinearLayout>(R.id.transportContainer)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getTransportation()
    }

    private fun getTransportation() {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(transportService.getTransportation(apiAccessToken)
                .processRequest(
                        { transport ->
                            dataBinding.transVM = transport
                            hideProgress()
                            containerView.visibility = View.VISIBLE
                        },
                        { error ->
                            containerView.visibility = View.INVISIBLE
                            hideProgress(true, error)
                        }
                )
        )
    }
}
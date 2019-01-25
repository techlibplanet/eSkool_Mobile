package net.rmitsolutions.eskool.fee

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.eskool.BaseFragment
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseFragmentComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.Fee
import net.rmitsolutions.eskool.network.IFee
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Madhu on 22-Jul-2017.
 */
class FeeFragment : BaseFragment() {
    @Inject
    lateinit var feeService: IFee
    private lateinit var recyclerView: RecyclerView
    private val adapter: FeeRecyclerViewAdapter by lazy { FeeRecyclerViewAdapter() }

    companion object {
        fun newInstance() = FeeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val depComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectFeeFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_fee, container, false)
        recyclerView = rootView.find<RecyclerView>(R.id.feesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFees()
    }

    private fun getFees() {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(feeService.getFees(apiAccessToken)
                .processRequest(
                        { fees ->
                            val feesList: MutableList<Fee> = fees.fees.toMutableList()
                            val fee = Fee(getString(R.string.totals_title), feesList.sumByDouble { f -> f.due },
                                    feesList.sumByDouble { f -> f.discount }, feesList.sumByDouble { f -> f.paid })

                            feesList.add(feesList.size, fee)
                            hideProgress()
                            adapter.items = feesList
                            adapter.notifyDataSetChanged()
                        },
                        { err ->
                            adapter.items = emptyList()
                            hideProgress(true, err)
                        }
                )
        )
    }
}
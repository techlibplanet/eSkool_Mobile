package net.rmitsolutions.eskool.library

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
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
import net.rmitsolutions.eskool.network.ILibrary
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Madhu on 24-Jul-2017.
 */
class LibraryFragment : BaseFragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter: LibraryRecyclerViewAdapter by lazy { LibraryRecyclerViewAdapter() }
    @Inject
    lateinit var libraryService: ILibrary

    companion object {
        fun newInstance() = LibraryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectLibraryFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_library, container, false)
        recyclerView = rootView.find<RecyclerView>(R.id.libraryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLibraryItems()
    }

    private fun getLibraryItems() {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(libraryService.getLibraryItems(apiAccessToken)
                .processRequest(
                        { lib ->
                            hideProgress()
                            adapter.items = lib.items
                            adapter.notifyDataSetChanged()
                        },
                        { err ->
                            hideProgress(true, err)
                            adapter.items = emptyList()
                        }
                )
        )

    }
}
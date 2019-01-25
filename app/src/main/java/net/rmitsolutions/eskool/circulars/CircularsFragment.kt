package net.rmitsolutions.eskool.circulars

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
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
import net.rmitsolutions.eskool.documents.DocumentsRecyclerViewAdapter
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.Document
import net.rmitsolutions.eskool.models.FileType
import net.rmitsolutions.eskool.network.IDocuments
import org.jetbrains.anko.find
import java.text.DateFormat
import javax.inject.Inject

/**
 * Created by Madhu on 21-Jul-2017.
 */
class CircularsFragment : BaseFragment() {
    @Inject
    lateinit var documentsService: IDocuments
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentsRecyclerViewAdapter

    companion object {
        fun newInstance() = CircularsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val depComponent = DaggerBaseFragmentComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectCircularsFragment(this)

        LocalBroadcastManager.getInstance(this!!.context!!).registerReceiver(downloadStateReceiver,
                IntentFilter(Constants.DOCUMENT_DOWNLOAD_STATUS_BROADCAST))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this!!.context!!).unregisterReceiver(downloadStateReceiver)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_circulars, container, false)

        recyclerView = rootView.find<RecyclerView>(R.id.circularsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = DocumentsRecyclerViewAdapter(FileType.Circular, activity!!.supportFragmentManager)
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCirculars()
    }

    private fun getCirculars() {
        if (!isNetConnected()) return
        showProgress()
        compositeDisposable.add(documentsService.getCirculars(apiAccessToken)
                .processRequest(
                        { cirList ->
                            hideProgress()
                            adapter.items = cirList.circulars.map {
                                Document(it.id, it.circularName,
                                        "Uploaded on ${DateFormat.getDateInstance().format(it.uploadDate)}",
                                        it.description, it.circularPath,
                                        if (it.circularPath.isNullOrBlank()) R.drawable.ic_download_disabled
                                        else R.drawable.ic_download)
                            }
                            adapter.notifyDataSetChanged()
                            //logD("Circulars events size : " + cirList.circulars.size)
                        },
                        { err ->
                            hideProgress(true, err)
                            adapter.items = emptyList()
                        }
                )
        )
    }

    private val downloadStateReceiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getBooleanExtra(Constants.DOCUMENT_DOWNLOAD_STATUS, false) ?: false
            val id = intent?.getIntExtra(Constants.DOCUMENT_DOWNLOAD_ITEM_ID, 0) ?: 0
            if (id > 0) {
                val doc = adapter.items.first { d -> d.id == id }
                val pos = adapter.items.indexOf(doc)

                if (status) adapter.items[pos].imgSrc = R.drawable.ic_check
                else adapter.items[pos].imgSrc = R.drawable.ic_alert_circle_outline
                adapter.notifyItemChanged(pos)
            }
        }
    })

}
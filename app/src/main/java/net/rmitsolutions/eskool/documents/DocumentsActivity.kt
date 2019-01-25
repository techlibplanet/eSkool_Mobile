package net.rmitsolutions.eskool.documents

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_documents.*
import kotlinx.android.synthetic.main.include_calendar_appbar_layout.*
import net.rmitsolutions.eskool.CalendarBaseActivity
import net.rmitsolutions.eskool.ESkoolApplication
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.dependency.components.DaggerBaseActivityComponent
import net.rmitsolutions.eskool.helpers.*
import net.rmitsolutions.eskool.models.DocParams
import net.rmitsolutions.eskool.models.FileType
import net.rmitsolutions.eskool.network.IDocuments
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by Madhu on 07-Jul-2017.
 */
class DocumentsActivity : CalendarBaseActivity() {
    @Inject
    lateinit var docsService: IDocuments //should not be private
    private lateinit var adapter: DocumentsRecyclerViewAdapter
    private lateinit var rxPermissions: RxPermissions
    private lateinit var date: Date
    var module = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        rxPermissions = RxPermissions(this)
        val depComponent = DaggerBaseActivityComponent.builder()
                .applicationComponent(ESkoolApplication.applicationComponent)
                .build()
        depComponent.injectDocumentsActivity(this)

        module = intent.getIntExtra("module", 0)
        Handler().post { titleView.text = getString(module) }
        var docType = FileType.Worksheet
        if (module == R.string.dashboard_module_homework) {
            dayTitle.setTextColor(resources.getColor(R.color.red))
            docType = FileType.Homework
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(downloadStateReceiver,
                IntentFilter(Constants.DOCUMENT_DOWNLOAD_STATUS_BROADCAST))

        documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        documentsRecyclerView.setHasFixedSize(true)
        documentsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = DocumentsRecyclerViewAdapter(docType, supportFragmentManager)
        documentsRecyclerView.adapter = adapter
        onDateSelected(Date())
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadStateReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        finishNoAnim()
    }

    private fun getDocuments() {
        if (!isNetConnected()) return
        val docParams = DocParams(date,
                if (module == R.string.dashboard_module_worksheets) FileType.Worksheet else FileType.Homework)

        showProgress()
        compositeDisposable.add(docsService.getDocuments(apiAccessToken, docParams)
                .processRequest(
                        { docsList ->
                            hideProgress()
                            docsList.docs.map {
                                if (it.docPath.isBlank()) it.imgSrc = R.drawable.ic_download_disabled
                                else it.imgSrc = R.drawable.ic_download
                            }
                            adapter.items = docsList.docs
                        },
                        { err ->
                            hideProgress(true, err)
                            adapter.items = emptyList()
                        }
                ))
    }

    override fun onDateSelected(selectedDate: Date) {
        date = selectedDate
        val day = DateFormat.getDateInstance().format(selectedDate)
        if (dayTitle.text == day) {
            return
        }
        dayTitle.text = day
        requestPermission()
    }

    private fun requestPermission() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) getDocuments()
                    else {
                        snackBar(dayTitle, "Need storage access permission to download files."
                                , duration = Snackbar.LENGTH_INDEFINITE, actionText = "Grant") {
                            requestPermission()
                        }
                    }
                }
    }

    override fun onMonthSelected(firstDayOfNewMonth: Date) {
        //nothing to do here
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
package net.rmitsolutions.eskool.downloads

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.webkit.MimeTypeMap
import kotlinx.android.synthetic.main.activity_downloads.*
import net.rmitsolutions.eskool.BaseActivity
import net.rmitsolutions.eskool.R
import net.rmitsolutions.eskool.helpers.Constants
import net.rmitsolutions.eskool.helpers.showStatus
import net.rmitsolutions.eskool.viewmodels.DownloadsViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.io.File
import java.text.DateFormat


/**
 * Created by Madhu on 19-Jul-2017.
 */
class DownloadsActivity : BaseActivity(), OnItemClickListener {
    val adapter: DownloadsRecyclerViewAdapter by lazy { DownloadsRecyclerViewAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloads)
    }

    override fun onStart() {
        super.onStart()
        setDownloadsRecyclerView()
        getDownloadedFiles()
    }

    override fun getSelfNavDrawerItem() = R.id.nav_downloads

    private fun setDownloadsRecyclerView() {
        downloadsRecyclerView.layoutManager = LinearLayoutManager(this)
        downloadsRecyclerView.setHasFixedSize(true)
        downloadsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        downloadsRecyclerView.adapter = adapter
    }

    private fun getDownloadedFiles() {
        try {
            val dir = getRootFolder()
            if (dir == null || !dir.exists()) {
                showStatus("No downloads found")
                return
            }

            val files = dir.listFiles()
            if (files.isEmpty()) {
                showStatus("No downloads found")
                return
            }

            files.sortByDescending { it.lastModified() }
            val items = mutableListOf<DownloadsViewModel>()
            files.map {
                items.add(DownloadsViewModel(it.name, getDate(it.lastModified()) + "     " + Constants.convertBytes(it.length())))
            }
            adapter.items = items

        } catch (e: Exception) {
            toast(e.message + "")
        }
    }

    private fun getDate(mills: Long): String {
        return DateFormat.getDateTimeInstance().format(mills)
    }

    override fun onDeleteFileClick(pos: Int) {
        try {
            val outputFile = getFile(pos) ?: return
            alert("Are you sure you want to delete ${outputFile.name} file?", "Confirmation") {
                positiveButton("Delete") {
                    if (outputFile.delete()) {
                        adapter.items.removeAt(pos)
                        adapter.notifyItemRemoved(pos)
                        toast("File deleted.")
                        if (adapter.items.size == 0) {
                            showStatus("No downloads found")
                        }
                    } else {
                        toast("Couldn't delete the file.")
                    }
                }
                negativeButton("No") { }
            }.show()
        } catch (ex: Exception) {
            toast("Couldn't delete the file.")
        }
    }

    override fun onOpenFileClick(pos: Int) {
        val outputFile = getFile(pos) ?: return

        val fileExt = MimeTypeMap.getFileExtensionFromUrl(adapter.items[pos].fileName)
        val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt) ?: "*/*"
        val openFileIntent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            openFileIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            uri = FileProvider.getUriForFile(this@DownloadsActivity, getString(R.string.file_provider_name), outputFile)
        } else {
            uri = Uri.fromFile(outputFile)
        }

        try {
            openFileIntent.setDataAndType(uri, fileType)
            startActivity(openFileIntent)
        } catch (ex: ActivityNotFoundException) {
            toast("No app found to open $fileExt files.")
        }
    }

    private fun getFile(pos: Int): File? {
        val filesDir = getRootFolder() ?: return null

        val item = adapter.items[pos]
        val fileName = item.fileName
        if (!filesDir.exists()) {
            toast("File $fileName not found.")
            return null
        }

        val outputFile = File(filesDir, fileName)
        if (!outputFile.exists()) {
            toast("File $fileName not found.")
            return null
        }
        return outputFile
    }

    private fun getRootFolder(): File? {
        val studentClassId = Constants.studentModel?.studentClassId
        if (studentClassId == 0) {
            toast("Session expired.")
            return null
        }

        var dir = File(Environment.getExternalStorageDirectory(), Constants.DOWNLOAD_MAIN_DIRECTORY)
        if (!dir.exists()) return null

        dir = File(dir, Constants.DOWNLOAD_SUB_DIRECTORY)
        if (!dir.exists()) return null

        dir = File(dir, studentClassId.toString())
        return if (!dir.exists()) null else dir
    }
}
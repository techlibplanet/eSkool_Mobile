package net.rmitsolutions.eskool.helpers

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.support.design.widget.Snackbar
import android.view.View
import com.madhuteja.checknet.CheckConnection
import net.rmitsolutions.eskool.R
import kotlinx.android.synthetic.main.include_status_view.*
import timber.log.Timber


/**
 * Created by Madhu on 14-Jun-2017.
 */
val Context.apiAccessToken: String
    get() = getAccessToken(applicationContext)

val Context.isExternalStorageWritable: Boolean
    get() = isExternalStorageAvail()

fun Activity.finishNoAnim() {
    finish()
    overridePendingTransition(0, 0)
}

val Activity.apiAccessToken: String
    get() = getAccessToken(applicationContext)

fun Activity.showProgress() {
    hideStatus()
    progressBar?.visibility = View.VISIBLE
}

fun Activity.hideProgress(showStatus: Boolean = false, message: String? = null) {
    progressBar?.visibility = View.INVISIBLE
    if (showStatus) showStatus(message) else hideStatus()
}

fun Activity.showStatus(message: String? = null) {
    statusView?.visibility = View.VISIBLE
    if (!message.isNullOrBlank()) statusView?.setSubtitle(message)
}

fun Activity.hideStatus() {
    statusView?.visibility = View.INVISIBLE
}

//check network
fun Activity.isNetConnected(showStatus: Boolean = true): Boolean {
    if (CheckConnection.with(this).isConnected) {
        hideStatus()
        return true
    } else {
        if (showStatus) {
            showStatus(getString(R.string.you_are_offline))
        }
        return false
    }
}

fun Context.isNetConnected() = CheckConnection.with(this).isConnected

//snack bar
fun Activity.snackBar(view: View, message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, "$message", duration).show()
}

fun Activity.snackBar(view: View, message: String?, duration: Int = Snackbar.LENGTH_SHORT,
                      actionText: String = "Ok", action: () -> Unit) {
    Snackbar.make(view, "$message", duration)
            .setAction(actionText,
            {
                action()
            }
    ).show()
}

// logging extensions
fun Activity.logE(t: Throwable, message: String?) = Timber.e(t, message)

fun Activity.logE(message: String?) = Timber.e(message)

fun Activity.logD(t: Throwable, message: String?) = Timber.d(t, message)

fun Activity.logD(message: String?) = Timber.d(message)

//external storage check
val Activity.isExternalStorageWritable: Boolean
    get() = isExternalStorageAvail()

private fun isExternalStorageAvail() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

private fun getAccessToken(context: Context): String {
    val token = Constants.accessToken?.accessToken ?: context.getPref(SharedPrefKeys.TOKEN_KEY, "")
    return "Bearer $token"
}
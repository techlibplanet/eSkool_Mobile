package net.rmitsolutions.eskool.helpers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import net.rmitsolutions.eskool.R

/**
 * Created by Madhu on 14-Jun-2017.
 */

object SharedPrefKeys {
    const val TOKEN_OBJECT_KEY = "sp_token_object_key"
    const val TOKEN_KEY = "sp_token_key"
    const val STUDENT_KEY = "sp_student_key"
    const val FIRST_LAUNCH = "isFirstTimeAppLunched"
    const val FIRST_LOGIN = "isFirstLogin"
}

val Context.defaultSharedPreferences: SharedPreferences
    get() {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

fun Context.clearPrefs() {
    applyPref(prefEditor().clear())

}

fun <T> Context.putPref(key: String, value: T) {
    val editor = prefEditor()
    applyPref(addToPrefEditor(editor, key, value))
}

fun <T> Context.addToPrefEditor(editor: SharedPreferences.Editor, key: String, value: T): SharedPreferences.Editor {
    when (value) {
        is String -> editor.putString(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        else -> throw Throwable("Type is not valid.")
    }
    return editor
}


fun Context.removePref(key: String) {
    applyPref(prefEditor().remove(key))
}

fun Context.removePref(vararg keys: String) {
    val editor = prefEditor()
    for (k in keys) {
        editor.remove(k)
    }
    applyPref(editor)
}

fun <T> Context.getPref(key: String, defaultValue: T): T {
    val prefs = this.defaultSharedPreferences
    val value: Any = when (defaultValue) {
        is String -> prefs.getString(key, defaultValue)
        is Boolean -> prefs.getBoolean(key, defaultValue)
        is Float -> prefs.getFloat(key, defaultValue)
        is Int -> prefs.getInt(key, defaultValue)
        is Long -> prefs.getLong(key, defaultValue)
        else -> throw Throwable("Type is not valid.")
    }
    return value as T
}



fun Context.prefEditor(): SharedPreferences.Editor {
    return defaultSharedPreferences.edit()
}

inline fun Context.addBulkPrefs(func: (editor: SharedPreferences.Editor) -> Unit) {
    val editor = prefEditor()
    func(editor)
    applyPref(editor)
}





fun Context.applyPref(editor: SharedPreferences.Editor) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
        editor.commit()
    } else {
        editor.apply()
    }
}

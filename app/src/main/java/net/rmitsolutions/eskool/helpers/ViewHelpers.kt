package net.rmitsolutions.eskool.helpers

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Madhu on 14-Jul-2017.
 */

fun ViewGroup.inflateView(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

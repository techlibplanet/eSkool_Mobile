package net.rmitsolutions.eskool.cafeteria

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import net.rmitsolutions.eskool.models.CafeteriaMenu
import android.view.LayoutInflater
import android.widget.TextView
import net.rmitsolutions.eskool.R
import org.jetbrains.anko.find


/**
 * Created by Madhu on 25-Jul-2017.
 */
class CafeteriaMenuListAdapter : BaseExpandableListAdapter() {
    var items = emptyList<CafeteriaMenu>()
    override fun getGroup(groupPosition: Int) = items[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    override fun hasStableIds() = false

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = parent!!.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_cafeteria_category, parent, false)
        }
        val category = view!!.find<TextView>(R.id.cafeteriaCategory)
        category.text = getGroup(groupPosition).category
        return view
    }

    override fun getChildrenCount(groupPosition: Int) = items[groupPosition].menu.size

    override fun getChild(groupPosition: Int, childPosition: Int) = items[groupPosition].menu[childPosition]

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = parent!!.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_cafeteria_menu, parent, false)
        }

        val menuItem = view!!.find<TextView>(R.id.cafeteriaMenuItem)
        menuItem.text = getChild(groupPosition, childPosition)
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()

    override fun getGroupCount() = items.size
}
package com.example.createplanetapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class ExpandableListAdapter(
    private val context: Context,
    private val groups: List<String>,
    private val items: Map<String, List<String?>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = groups.size
    override fun getChildrenCount(groupPos: Int): Int = items[groups[groupPos]]?.size ?: 0
    override fun getGroup(groupPos: Int): Any = groups[groupPos]
    override fun getChild(groupPos: Int, childPos: Int): Any = items[groups[groupPos]]?.get(childPos) ?: ""
    override fun getGroupId(groupPos: Int): Long = groupPos.toLong()
    override fun getChildId(groupPos: Int, childPos: Int): Long = childPos.toLong()
    override fun hasStableIds(): Boolean = true

    // Разметка для группы (заголовок)
    override fun getGroupView(groupPos: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.expandable_list_group, parent, false)


        view.findViewById<TextView>(R.id.group_text).text = getGroup(groupPos) as String
        return view
    }

    // Разметка для элемента (подпункт)
    override fun getChildView(groupPos: Int, childPos: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.expandable_list_child, parent, false)

        view.findViewById<TextView>(R.id.child_text).text = getChild(groupPos, childPos) as String
        return view
    }

    override fun isChildSelectable(groupPos: Int, childPos: Int): Boolean = true
}
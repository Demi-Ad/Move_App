package com.Demi.Move.SettingActivity.Fragments.Resource

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.Database.ExtensionView
import com.Demi.Move.R

class SettingEditExtensionAdapter(val onDelete :(ExtensionData) -> Unit) :
    RecyclerView.Adapter<SettingEditExtensionAdapter.ViewHolder>() {

    private val items = ArrayList<ExtensionView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setting_edit_extension_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisPosition = items[position]
        holder.bind(thisPosition)
    }

    override fun getItemCount():Int {
        return items.size
    }

    fun submitList(data:ArrayList<ExtensionView>) {
        items.clear()
        items.addAll(data)
        Log.d("로그", "submitList: ")
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val innerAdapter = SettingEditExtensionInnerAdapter(onDelete = {
            onDelete.invoke(it)
        })
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.setting_edit_extension_inner_items)


        private val type : TextView = itemView.findViewById(R.id.setting_edit_extension_type)

        fun bind(extensionView: ExtensionView) {
            type.text = extensionView.type

            recyclerView.apply {
                this.adapter = innerAdapter
                this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            innerAdapter.submitList(extensionView.extensions as ArrayList<ExtensionData>)
            innerAdapter.notifyDataSetChanged()
        }
    }
}
package com.Demi.Move.SettingActivity.Fragments.Resource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.Database.ExtensionView
import com.Demi.Move.R

class SettingEditExtensionInnerAdapter(private val onDelete:(ExtensionData)->Unit) : RecyclerView.Adapter<SettingEditExtensionInnerAdapter.ViewHolder>() {

    private val items = ArrayList<ExtensionData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setting_edit_extension_inner_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisPosition = items[position]
        holder.bind(thisPosition)

        holder.itemView.findViewById<ImageView>(R.id.setting_edit_extension_delete_btn).setOnClickListener {
            onDelete.invoke(thisPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(data:ArrayList<ExtensionData>) {
        items.clear()
        items.addAll(data)
    }

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        private val textTitle:TextView = itemView.findViewById(R.id.setting_edit_extension_inner_item_text)

        fun bind(extensionData: ExtensionData) {
            textTitle.text = extensionData.extension
        }
    }
}
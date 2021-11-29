package com.Demi.Move.MainActivityResource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Demi.Move.R

class MainRecyclerViewInItemsAdapter(
    val color:Int
): RecyclerView.Adapter<MainRecyclerViewInItemsAdapter.ViewHolder>() {

    private val items = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_recycler_card_items, parent, false)
        return ViewHolder(view,color)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(data:ArrayList<String>) {
        items.clear()
        items.addAll(data)
    }

    inner class ViewHolder(view:View,color: Int):RecyclerView.ViewHolder(view) {
        private val recyclerViewItem = itemView.findViewById<TextView>(R.id.recycle_item)

        fun bind(data:String) {
            recyclerViewItem.setTextColor(color)
            recyclerViewItem.text = "#$data"
        }
    }
}
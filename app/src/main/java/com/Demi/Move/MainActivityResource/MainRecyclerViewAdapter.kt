/*
Author : Park Jong Seong
Created At : 2021-11-03 - 오후 11:26
Updated At :
Description : 메인 액티비티 리사이클러뷰 어댑터
*/
package com.Demi.Move.MainActivityResource

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Demi.Move.Database.PathData
import com.Demi.Move.Database.ViewPathData
import com.Demi.Move.R
import com.Demi.Move.Utils.toShotPath

/**
 * @param onDelete 삭제 버튼을 눌렀을때 작동할 콜백 함수
 * @see MainActivity.viewModel
 */


class MainRecyclerViewAdapter(
    private val context: Context,
    val onDelete: (pathData: PathData) -> Unit,
    val onStart: (viewPathData: ViewPathData) -> Unit,
    val onClick: (pathData: PathData) -> Unit
) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    private val items = ArrayList<ViewPathData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_recycler_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisPosition = items[position]

        holder.bind(thisPosition)

        holder.itemView.findViewById<ImageView>(R.id.btn_main_delete).setOnClickListener {
            onDelete.invoke(thisPosition.pathData)
        }
        holder.itemView.findViewById<ImageView>(R.id.btn_main_start).setOnClickListener {
            Log.d("로그", "MainRecyclerViewAdapter - onBindViewHolder: ")
            onStart.invoke(thisPosition)
        }
        holder.itemView.setOnClickListener {
            onClick.invoke(thisPosition.pathData)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(data: ArrayList<ViewPathData>) {
        items.clear()
        items.addAll(data)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val srcPath: TextView = itemView.findViewById(R.id.main_src_path)
        private val dstPath: TextView = itemView.findViewById(R.id.main_dst_path)
        private val actionTextView: TextView = itemView.findViewById(R.id.main_action_textview)
        private val typeTextView: TextView = itemView.findViewById(R.id.main_type_textview)
        private val innerAdapter =
            MainRecyclerViewInItemsAdapter(ContextCompat.getColor(view.context, R.color.white))
        private val innerRecyclerView = itemView.findViewById<RecyclerView>(R.id.main_recycler_view)
        private val mainRecycler = itemView.findViewById<ConstraintLayout>(R.id.main_recycler)

        fun bind(viewPathData: ViewPathData) {
            val root = Environment.getExternalStorageDirectory().absolutePath
            srcPath.text = viewPathData.pathData.srcPath.toShotPath(root)
            dstPath.text = viewPathData.pathData.dstPath.toShotPath(root)
            actionTextView.text = viewPathData.pathData.actionType
            typeTextView.text = viewPathData.pathData.type
            mainRecycler.background = when (viewPathData.pathData.type) {
                "Image" -> ContextCompat.getDrawable(context, R.drawable.recycler_image_background)
                "Docs" -> ContextCompat.getDrawable(context, R.drawable.recycler_docs_background)
                "Video" -> ContextCompat.getDrawable(context, R.drawable.recycler_video_background)
                "Music" -> ContextCompat.getDrawable(context, R.drawable.recycler_music_background)
                "Custom" -> ContextCompat.getDrawable(context, R.drawable.recycler_custom_background)
                else -> null
            }

            innerAdapter.submitList(viewPathData.extensionList as ArrayList<String>)

            innerRecyclerView.apply {
                this.adapter = innerAdapter
                this.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            innerAdapter.notifyDataSetChanged()

        }
    }
}
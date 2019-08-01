package com.kuanhsien.app.sample.android_google_map_demo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.data.DemoType
import kotlinx.android.synthetic.main.listitem_linear_vertical_viewholder.view.*

class MainSelectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(type: DemoType)
    }

    private var listener: OnItemClickListener? = null
    private var dataList = mutableListOf<DemoType>()

    // override to use recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_linear_vertical_viewholder, parent, false)
        return MainItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainItemViewHolder)
            holder.bind(dataList[position], listener)
    }

    // set Data
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(dataList: List<DemoType>) {
        this.dataList = dataList.toMutableList()
        notifyDataSetChanged()
    }

    fun insertData(position: Int, list: List<DemoType>) {
        this.dataList.addAll(position, list)
        notifyItemRangeInserted(position, list.size)
    }

    // ViewHolder
    class MainItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: DemoType, listener: OnItemClickListener?) {

            // set button text
            itemView.btn_item.text = itemView.context.getString(data.titleRes)

            // set button listener
            listener?.run {
                itemView.btn_item.setOnClickListener { onItemClick(data) }
            }
        }
    }
}
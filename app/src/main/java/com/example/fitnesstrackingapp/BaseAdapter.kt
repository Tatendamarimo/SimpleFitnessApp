package com.example.fitnesstrackingapp

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    // Example for common click listener
    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }


}

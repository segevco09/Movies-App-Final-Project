package com.example.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R

class SortAdapter(
    private val sortOptions: List<String>,
    private val listener: OnSortClickListener,
    private var selectedSort: String = "Regular"
) : RecyclerView.Adapter<SortAdapter.SortViewHolder>() {

    private var selectedPosition: Int = sortOptions.indexOf(selectedSort)

    interface OnSortClickListener {
        fun onSortClick(sortType: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_sort_option, parent, false)
        return SortViewHolder(view)
    }

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        val sortOption = sortOptions[position]
        holder.sortTextView.text = sortOption

        val context = holder.itemView.context

        // Default styling
        holder.sortTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.turquoise))
        holder.sortTextView.setTextColor(ContextCompat.getColor(context, R.color.black))

        // Highlight selected item
        if (position == selectedPosition) {
            holder.sortTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.sortTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        // Handle click event
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // Notify RecyclerView to update the item views
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            // Notify listener about the sort selection
            listener.onSortClick(sortOption)
        }
    }


    fun resetSort() {
        val previousPosition = selectedPosition
        selectedPosition = sortOptions.indexOf("Regular")

        if (previousPosition != selectedPosition) {
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        } else {
            notifyDataSetChanged()
        }
    }


    fun setSelectedSort(sortType: String) {

        val previousPosition = selectedPosition
        selectedPosition = sortOptions.indexOf(sortType)

        if (previousPosition != selectedPosition) {
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return sortOptions.size
    }

    class SortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sortTextView: TextView = itemView.findViewById(R.id.sortTextView)
    }
}

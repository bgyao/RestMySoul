package com.example.restmysoul.ui.goals

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restmysoul.R
import com.example.restmysoul.database.ReadDetailsModelClass
import kotlinx.android.synthetic.main.item_goals.view.*

class GoalsAdapter(val context: Context, val listener: ItemSelectListener? = null
): ListAdapter<ReadDetailsModelClass, GoalsAdapter.ReadingDetailsViewHolder>(ReadingDetailsDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingDetailsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_goals, parent)
        return ReadingDetailsViewHolder(view, listener)


    }

    override fun submitList(list: List<ReadDetailsModelClass>?) {
        super.submitList(list)
    }

    class ReadingDetailsViewHolder(itemView: View, listener: ItemSelectListener?): RecyclerView.ViewHolder(itemView) {

        fun bind(readDetailsModelClass: ReadDetailsModelClass, index: Int, itemSelectListener: ItemSelectListener?) {
            itemView.setOnClickListener {
                itemSelectListener?.onItemSelected(index)
            }
            itemView.tv_index.text = "id: " + readDetailsModelClass.readingdetails_id.toString()
            itemView.tv_number_of_days.text = "Number of days: " + readDetailsModelClass.numdays.toString()
            itemView.tv_chapters_per_day.text = "Chapters per day: " + readDetailsModelClass.numchapters.toString()
            if(readDetailsModelClass.is_done!=null) itemView.tv_done.text = "Status: DONE"
            else itemView.tv_done.text = "Status: ONGOING"
        }
    }

    override fun onBindViewHolder(holder: ReadingDetailsViewHolder, position: Int) {
        holder.bind(getItem(position), position, listener)
    }
}

class ReadingDetailsDiff: DiffUtil.ItemCallback<ReadDetailsModelClass>() {
    override fun areItemsTheSame(oldItem: ReadDetailsModelClass, newItem: ReadDetailsModelClass): Boolean {
        return oldItem.readingdetails_id == newItem.readingdetails_id
    }

    override fun areContentsTheSame(oldItem: ReadDetailsModelClass, newItem: ReadDetailsModelClass): Boolean {
        return oldItem.readingdetails_id == newItem.readingdetails_id
    }
}

interface ItemSelectListener {
    fun onItemSelected(position: Int)
}

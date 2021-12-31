package com.ajireapp.farmrecordstore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_record_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecordRecyclerAdapter(private val context: Context,
                            private val onDeleteClickListener: OnDeleteClickListener) : RecyclerView.Adapter<RecordRecyclerAdapter.RecordViewHolder>() {

     interface OnDeleteClickListener {
        fun onDeleteClickListener(myRecord: Record)
    }

    private var recordList: List<Record> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_record_list, parent, false)
        return RecordViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = recordList[position]
        holder.setData(record.category, record.title, record.lastUpdated, position)
        holder.setListeners()
    }

    override fun getItemCount(): Int = recordList.size

    fun setRecords(records: List<Record>) {
        recordList = records
        notifyDataSetChanged()
    }

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var pos: Int = 0

        fun setData(category: String, title : String, lastUpdated: Date?, position: Int) {
            itemView.tvRecordCategory.text = category
            itemView.tvRecordTitle.text = title
            itemView.tvLastUpdated.text = getFormattedDate(lastUpdated)

            this.pos = position
        }

        private fun getFormattedDate(lastUpdated: Date?): String {
            var time = context.getString(R.string.time_last_updated)
            time += lastUpdated?.let {
                val sdf = SimpleDateFormat("HH:mm d MMM, yyyy", Locale.getDefault())
                sdf.format(lastUpdated)
            } ?: "Not found"
            return time
        }

        fun setListeners() {
            itemView.setOnClickListener {
                val intent = Intent(context, EditRecordActivity::class.java)
                intent.putExtra("id", recordList[pos].id)
                intent.putExtra("category", recordList[pos].category)
                intent.putExtra("title", recordList[pos].title)
                intent.putExtra("details", recordList[pos].details)
                intent.putExtra("lastUpdated", getFormattedDate(recordList[pos].lastUpdated))
                (context as Activity).startActivityForResult(intent, MainActivity.UPDATE_RECORD_ACTIVITY_REQUEST_CODE)
            }

            itemView.ivDelete.setOnClickListener {
                onDeleteClickListener.onDeleteClickListener(recordList[pos])
            }
        }

    }
}

package br.com.lno.workmanagertest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import br.com.lno.workmanagertest.R

class QueueAdapter : ListAdapter<WorkInfo, QueueAdapter.QueueViewHolder>(QueueDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        return QueueViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.queue_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QueueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(workInfo: WorkInfo) {
            with(itemView) {
                setOnLongClickListener {
                    WorkManager.getInstance(context).cancelWorkById(workInfo.id)
                    true
                }
                findViewById<TextView>(R.id.tv_id).text = workInfo.id.toString()
                findViewById<TextView>(R.id.tv_state).text = workInfo.state.name
                findViewById<TextView>(R.id.tv_attempt_count).text =
                    context.getString(R.string.attempt_count, workInfo.runAttemptCount)
            }
        }
    }

    class QueueDiff : DiffUtil.ItemCallback<WorkInfo>() {
        override fun areItemsTheSame(oldItem: WorkInfo, newItem: WorkInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkInfo, newItem: WorkInfo): Boolean {
            return oldItem == newItem
        }
    }
}
package org.bitspilani.ssms.messapp.screens.notice.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_notices_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.bitspilani.ssms.messapp.screens.notice.view.model.ViewLayerNotice

class NoticesAdapter(private val listener: ClickListener) : RecyclerView.Adapter<NoticesAdapter.NoticeVHolder>() {

    interface ClickListener {

        fun onNoticeClicked(id: Id)
    }

    var notices = listOf<ViewLayerNotice>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = notices.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoticeVHolder(inflater.inflate(R.layout.row_notices_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: NoticeVHolder, position: Int) {
        val notice = notices[position]

        val resources = holder.rootPOV.resources
        val indicatorColor = when(notice.priority) {
            Priority.Normal    -> ResourcesCompat.getColorStateList(resources, R.color.pnk04, null)
            Priority.Important -> ResourcesCompat.getColorStateList(resources, R.color.pnk03, null)
            Priority.Critical  -> ResourcesCompat.getColorStateList(resources, R.color.pnk02, null)
        }
        holder.priorityIndicatorPOV.backgroundTintList = indicatorColor

        holder.headingLBL.text = notice.heading
        holder.dateLBL.text = notice.date
        holder.timeLBL.text = notice.time

        holder.rootPOV.setOnClickListener {
            listener.onNoticeClicked(notice.id)
        }
    }

    class NoticeVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val priorityIndicatorPOV: View = rootPOV.priorityIndicatorPOV
        val headingLBL: TextView = rootPOV.headingLBL
        val dateLBL: TextView = rootPOV.dateLBL
        val timeLBL: TextView = rootPOV.timeLBL
    }
}
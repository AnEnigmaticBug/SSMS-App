package org.bitspilani.ssms.messapp.screens.menu.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.col_dates_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerDate

class DatesAdapter(private val listener: PickDateListener) : RecyclerView.Adapter<DatesAdapter.DateVHolder>() {

    interface PickDateListener {

        fun onDatePicked(id: Long)
    }

    var dates = listOf<ViewLayerDate>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = dates.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DateVHolder(inflater.inflate(R.layout.col_dates_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: DateVHolder, position: Int) {
        val date = dates[position]

        holder.dateLBL.text = date.date
        holder.monthLBL.text = date.month

        val context = holder.rootPOV.context

        val bgColor = when(date.isSelected) {
            true  -> ContextCompat.getColor(context, R.color.pnk01)
            false -> ContextCompat.getColor(context, R.color.wht01)
        }

        val fgColor = when(date.isSelected) {
            true  -> ContextCompat.getColor(context, R.color.wht01)
            false -> ContextCompat.getColor(context, R.color.vio01)
        }

        holder.rootPOV.setBackgroundColor(bgColor)
        holder.dateLBL.setTextColor(fgColor)
        holder.monthLBL.setTextColor(fgColor)

        holder.rootPOV.setOnClickListener {
            listener.onDatePicked(date.id)
        }
    }

    class DateVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val dateLBL: TextView = rootPOV.dateLBL
        val monthLBL: TextView = rootPOV.monthLBL
    }
}
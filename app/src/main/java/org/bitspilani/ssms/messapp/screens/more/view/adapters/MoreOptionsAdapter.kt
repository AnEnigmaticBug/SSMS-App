package org.bitspilani.ssms.messapp.screens.more.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_more_options_rcy.view.*
import org.bitspilani.ssms.messapp.R

class MoreOptionsAdapter : RecyclerView.Adapter<MoreOptionsAdapter.MoreOptionVHolder>() {

    private val options = listOf(
        "Sick Food", "Feedback", "Help", "Contact Us", "About", "SSMS Tech Team"
    )

    override fun getItemCount() = options.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreOptionVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MoreOptionVHolder(inflater.inflate(R.layout.row_more_options_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: MoreOptionVHolder, position: Int) {
        val option = options[position]

        holder.nameLBL.text = option

        holder.rootPOV.setOnClickListener {  }
    }

    class MoreOptionVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
    }
}
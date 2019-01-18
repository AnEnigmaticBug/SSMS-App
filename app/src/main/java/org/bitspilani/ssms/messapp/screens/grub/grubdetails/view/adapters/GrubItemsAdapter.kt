package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bitspilani.ssms.messapp.R

class GrubItemsAdapter : RecyclerView.Adapter<GrubItemsAdapter.GrubItemVHolder>() {

    var items = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrubItemVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GrubItemVHolder(
            inflater.inflate(R.layout.row_grub_items_rcy, parent, false) as TextView
        )
    }

    override fun onBindViewHolder(holder: GrubItemVHolder, position: Int) {
        holder.nameLBL.text = items[position]
    }

    class GrubItemVHolder(val nameLBL: TextView) : RecyclerView.ViewHolder(nameLBL)
}
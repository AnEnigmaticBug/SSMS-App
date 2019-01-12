package org.bitspilani.ssms.messapp.screens.grub.grublist.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_grubs_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id

class GrubsAdapter(private val listener: ClickListener) : RecyclerView.Adapter<GrubsAdapter.GrubVHolder>() {

    interface ClickListener {

        fun onGrubClicked(id: Id)
    }

    var grubs = listOf<ViewLayerGrub>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = grubs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrubVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GrubVHolder(inflater.inflate(R.layout.row_grubs_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: GrubVHolder, position: Int) {
        val grub = grubs[position]

        holder.nameLBL.text = grub.name
        holder.dateLBL.text = grub.date
        holder.organizerLBL.text = grub.organizer
        holder.typeLBL.text = grub.type

        holder.rootPOV.setOnClickListener {
            listener.onGrubClicked(grub.id)
        }
    }

    class GrubVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val dateLBL: TextView = rootPOV.dateLBL
        val organizerLBL: TextView = rootPOV.organizerLBL
        val typeLBL: TextView = rootPOV.typeLBL
    }
}
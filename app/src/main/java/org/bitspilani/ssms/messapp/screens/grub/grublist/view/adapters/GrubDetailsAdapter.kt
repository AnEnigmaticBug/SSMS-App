package org.bitspilani.ssms.messapp.screens.grub.grublist.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_grub_details_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id

class GrubDetailsAdapter(private val listener: ClickListener) : RecyclerView.Adapter<GrubDetailsAdapter.GrubDetailsVHolder>() {

    interface ClickListener {

        fun onGrubClicked(id: Id)
    }

    var grubs = listOf<ViewLayerGrub>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = grubs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrubDetailsVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GrubDetailsVHolder(inflater.inflate(R.layout.row_grub_details_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: GrubDetailsVHolder, position: Int) {
        val grub = grubs[position]

        holder.nameLBL.text = grub.name
        holder.dateLBL.text = grub.date
        holder.organizerLBL.text = grub.organizer
        holder.foodOptionLBL.text = grub.foodOption

        holder.rootPOV.setOnClickListener {
            listener.onGrubClicked(grub.id)
        }
    }

    class GrubDetailsVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val dateLBL: TextView = rootPOV.dateLBL
        val organizerLBL: TextView = rootPOV.organizerLBL
        val foodOptionLBL: TextView = rootPOV.foodOptionLBL
    }
}
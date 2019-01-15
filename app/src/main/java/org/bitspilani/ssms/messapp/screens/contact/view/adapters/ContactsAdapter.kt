package org.bitspilani.ssms.messapp.screens.contact.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_contacts_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.contact.view.model.ViewLayerContact

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactVHolder>() {

    var contacts = listOf<ViewLayerContact>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactVHolder(inflater.inflate(R.layout.row_contacts_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: ContactVHolder, position: Int) {
        val contact = contacts[position]

        holder.nameLBL.text = contact.name
        holder.postLBL.text = contact.post
        holder.phoneLBL.text = contact.phone

        val context = holder.rootPOV.context

        val glideConfig = RequestOptions()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_profile_pic_placeholder_100dp_100dp))
            .circleCrop()

        Glide.with(context)
            .load(contact.picUrl)
            .apply(glideConfig)
            .into(holder.picIMG)
    }

    class ContactVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val postLBL: TextView = rootPOV.postLBL
        val phoneLBL: TextView = rootPOV.phoneLBL
        val picIMG: ImageView = rootPOV.picIMG
    }
}
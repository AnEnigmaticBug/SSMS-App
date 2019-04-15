package org.bitspilani.ssms.messapp.screens.techteam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_tech_members_rcy.view.*
import org.bitspilani.ssms.messapp.R

class TechMembersAdapter : RecyclerView.Adapter<TechMembersAdapter.TechMemberVHolder>() {

    private data class TechMember(val name: String, val post: String, val pic: Int, val icon: Int)

    private val members = listOf(
        TechMember("Nishant\nMahajan", "App\nDeveloper", R.drawable.im_nishant, R.drawable.ic_app_dev_120dp_120dp),
        TechMember("Pratik\nBachhav", "UI & UX\nDesigner", R.drawable.im_pratik, R.drawable.ic_designer_120dp_120dp),
        TechMember("Divyam\nGoel", "Backend\nDeveloper", R.drawable.im_divyam, R.drawable.ic_backend_dev_120dp_120dp),
        TechMember("Raghav\nArora", "Backend\nDeveloper", R.drawable.im_raghav, R.drawable.ic_backend_dev_120dp_120dp)
    )

    override fun getItemCount() = members.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechMemberVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TechMemberVHolder(inflater.inflate(R.layout.row_tech_members_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: TechMemberVHolder, position: Int) {
        val member = members[position]

        holder.nameLBL.text = member.name
        holder.postLBL.text = member.post

        holder.iconIMG.setImageResource(member.icon)

        val context = holder.rootPOV.context

        val glideConfig = RequestOptions()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_profile_pic_placeholder_100dp_100dp))
            .circleCrop()

        Glide.with(context)
            .load(member.pic)
            .apply(glideConfig)
            .into(holder.picIMG)
    }

    class TechMemberVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val postLBL: TextView = rootPOV.postLBL
        val picIMG: ImageView = rootPOV.picIMG
        val iconIMG: ImageView = rootPOV.iconIMG
    }
}
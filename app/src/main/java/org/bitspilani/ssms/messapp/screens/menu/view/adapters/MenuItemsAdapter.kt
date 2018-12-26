package org.bitspilani.ssms.messapp.screens.menu.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_menu_items_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMenuItem

class MenuItemsAdapter(private val listener: RateItemListener) : RecyclerView.Adapter<MenuItemsAdapter.MenuItemVHolder>() {

    interface RateItemListener {

        fun onItemRated(id: Id, rating: Rating)
    }

    var menuItems = listOf<ViewLayerMenuItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = menuItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenuItemVHolder(inflater.inflate(R.layout.row_menu_items_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: MenuItemVHolder, position: Int) {
        val menuItem = menuItems[position]

        holder.nameLBL.text = menuItem.name
        when(menuItem.rating) {
            Rating.Positive -> holder.useSolidColors(true, false)
            Rating.NotRated -> holder.useSolidColors(false, false)
            Rating.Negative -> holder.useSolidColors(false, true)
        }

        holder.ratePositivelyBTN.setOnClickListener {
            when(menuItem.rating) {
                Rating.Positive -> listener.onItemRated(menuItem.id, Rating.NotRated)
                Rating.NotRated -> listener.onItemRated(menuItem.id, Rating.Positive)
                Rating.Negative -> listener.onItemRated(menuItem.id, Rating.Positive)
            }
        }

        holder.rateNegativelyBTN.setOnClickListener {
            when(menuItem.rating) {
                Rating.Positive -> listener.onItemRated(menuItem.id, Rating.Negative)
                Rating.NotRated -> listener.onItemRated(menuItem.id, Rating.Negative)
                Rating.Negative -> listener.onItemRated(menuItem.id, Rating.NotRated)
            }
        }
    }

    private fun MenuItemVHolder.useSolidColors(positive: Boolean, negative: Boolean) {
        val positiveBTNResId = if(positive) R.drawable.ic_like_solid_28dp_28dp else R.drawable.ic_like_outline_28dp_28dp
        val negativeBTNResId = if(negative) R.drawable.ic_dislike_solid_28dp_28dp else R.drawable.ic_dislike_outline_28dp_28dp

        ratePositivelyBTN.setImageResource(positiveBTNResId)
        rateNegativelyBTN.setImageResource(negativeBTNResId)
    }

    class MenuItemVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val ratePositivelyBTN: ImageView = rootPOV.ratePositivelyBTN
        val rateNegativelyBTN: ImageView = rootPOV.rateNegativelyBTN
    }
}
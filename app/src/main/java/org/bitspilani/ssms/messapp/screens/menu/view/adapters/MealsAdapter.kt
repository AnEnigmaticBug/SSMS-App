package org.bitspilani.ssms.messapp.screens.menu.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_meals_rcy.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMeal

class MealsAdapter(private val listener: RateItemListener) : RecyclerView.Adapter<MealsAdapter.MealVHolder>(), MenuItemsAdapter.RateItemListener {

    interface RateItemListener {

        fun onItemRated(id: Id, rating: Rating)
    }

    var meals = listOf<ViewLayerMeal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = meals.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealVHolder(inflater.inflate(R.layout.row_meals_rcy, parent, false))
    }

    override fun onBindViewHolder(holder: MealVHolder, position: Int) {
        val meal = meals[position]

        holder.nameLBL.text = meal.name

        holder.menuItemsRCY.adapter = MenuItemsAdapter(this).also { it.menuItems = meal.items }
    }

    override fun onItemRated(id: Id, rating: Rating) {
        listener.onItemRated(id, rating)
    }

    class MealVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val menuItemsRCY: RecyclerView = rootPOV.menuItemsRCY
    }
}
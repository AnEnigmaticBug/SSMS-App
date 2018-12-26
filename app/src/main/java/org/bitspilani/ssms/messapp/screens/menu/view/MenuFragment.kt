package org.bitspilani.ssms.messapp.screens.menu.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fra_menu.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModel
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModelFactory
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.view.adapters.DatesAdapter
import org.bitspilani.ssms.messapp.screens.menu.view.adapters.MealsAdapter

class MenuFragment : Fragment(), DatesAdapter.PickDateListener, MealsAdapter.RateItemListener {

    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, MenuViewModelFactory())[MenuViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_menu, container, false)

        rootPOV.mealsRCY.adapter = MealsAdapter(this)
        rootPOV.datesRCY.adapter = DatesAdapter(this)

        viewModel.dates.observe(this, Observer {
            (rootPOV.datesRCY.adapter as DatesAdapter).dates = it
        })

        viewModel.meals.observe(this, Observer {
            (rootPOV.mealsRCY.adapter as MealsAdapter).meals = it
        })

        viewModel.toast.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        return rootPOV
    }

    override fun onDatePicked(id: Long) {
        viewModel.onPickDateAction(id)
    }

    override fun onItemRated(id: Id, rating: Rating) {
        viewModel.onRateItemAction(id, rating)
    }
}
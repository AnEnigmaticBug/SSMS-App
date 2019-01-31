package org.bitspilani.ssms.messapp.screens.menu.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fra_menu_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.login.view.LoginActivity
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModel
import org.bitspilani.ssms.messapp.screens.menu.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModelFactory
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.view.adapters.DatesAdapter
import org.bitspilani.ssms.messapp.screens.menu.view.adapters.MealsAdapter
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerDate
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMeal

class MenuFragment : Fragment(), DatesAdapter.PickDateListener, MealsAdapter.RateItemListener {

    private lateinit var viewModel: MenuViewModel

    // True when the dates bar has been swiped at-least once.
    private var isDatesBarSwiped = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, MenuViewModelFactory())[MenuViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_menu_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_menu)

        rootPOV.mealsRCY.adapter = MealsAdapter(this)
        rootPOV.datesRCY.adapter = DatesAdapter(this)

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.leftBTN.setOnClickListener {
            rootPOV.datesRCY.smoothScrollBy(-500, 0)
        }

        rootPOV.rightBTN.setOnClickListener {
            rootPOV.datesRCY.smoothScrollBy(+500, 0)
        }

        rootPOV.datesRCY.onFlingListener = object : RecyclerView.OnFlingListener() {

            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                isDatesBarSwiped = true
                return false
            }
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.dates, it.meals)
                is UiOrder.ShowFailure -> showFailureState(it.error)
                is UiOrder.ShowLoading -> showLoadingState()
                is UiOrder.MoveToLogin -> {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            }
        })

        viewModel.toast.observe(this, Observer {
            if(it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }

    override fun onDatePicked(id: Long) {
        viewModel.onPickDateAction(id)
    }

    override fun onItemRated(id: Id, rating: Rating) {
        viewModel.onRateItemAction(id, rating)
    }

    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showWorkingState(dates: List<ViewLayerDate>, meals: List<ViewLayerMeal>) {
        if(!isDatesBarSwiped) {
            view!!.datesRCY.scrollToPosition(dates.indexOfFirst { it.isSelected })
        }

        (view as ConstraintLayout).setState(R.id.working, 0, 0)
        (view!!.datesRCY.adapter as DatesAdapter).dates = dates
        (view!!.mealsRCY.adapter as MealsAdapter).meals = meals
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        view!!.errorLBL.text = error
    }
}
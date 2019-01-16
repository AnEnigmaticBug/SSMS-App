package org.bitspilani.ssms.messapp.screens.grub.grublist.view

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fra_grub_list_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grublist.core.GrubListViewModel
import org.bitspilani.ssms.messapp.screens.grub.grublist.core.GrubListViewModelFactory
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.adapters.GrubDetailsAdapter
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.login.view.LoginActivity

class GrubListFragment : Fragment(), GrubDetailsAdapter.ClickListener {

    private lateinit var viewModel: GrubListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, GrubListViewModelFactory())[GrubListViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_grub_list_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_grub_list)

        rootPOV.grubDetailsRCY.adapter = GrubDetailsAdapter(this)

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.viewUpcomingBTN.setOnClickListener {
            viewModel.onViewUpcomingGrubsAction()
        }

        rootPOV.viewSignedBTN.setOnClickListener {
            viewModel.onViewSignedGrubsAction()
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.grubs, it.viewOnlySigned)
                is UiOrder.ShowFailure -> showFailureState(it.error)
                is UiOrder.ShowLoading -> showLoadingState()
                is UiOrder.MoveToLogin -> {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            }
        })

        return rootPOV
    }

    override fun onGrubClicked(id: Id) {

    }

    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showWorkingState(grubs: List<ViewLayerGrub>, viewOnlySigned: Boolean) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)
        (view?.grubDetailsRCY?.adapter as GrubDetailsAdapter).grubs = grubs

        val pnkColor = ContextCompat.getColor(context!!, R.color.pnk01)
        val whtColor = ContextCompat.getColor(context!!, R.color.wht01)

        when(viewOnlySigned) {
            true  -> {
                view!!.viewUpcomingBTN.background.setColorFilter(whtColor, PorterDuff.Mode.SRC_ATOP)
                view!!.viewUpcomingBTN.setTextColor(pnkColor)
                view!!.viewSignedBTN.background.setColorFilter(pnkColor, PorterDuff.Mode.SRC_ATOP)
                view!!.viewSignedBTN.setTextColor(whtColor)
            }
            false -> {
                view!!.viewUpcomingBTN.background.setColorFilter(pnkColor, PorterDuff.Mode.SRC_ATOP)
                view!!.viewUpcomingBTN.setTextColor(whtColor)
                view!!.viewSignedBTN.background.setColorFilter(whtColor, PorterDuff.Mode.SRC_ATOP)
                view!!.viewSignedBTN.setTextColor(pnkColor)
            }
        }
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        view!!.errorLBL.text = error
    }
}
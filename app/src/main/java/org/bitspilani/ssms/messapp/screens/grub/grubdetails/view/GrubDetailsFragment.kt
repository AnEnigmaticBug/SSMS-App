package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fra_grub_details_working_state.*
import kotlinx.android.synthetic.main.fra_grub_details_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.core.GrubDetailsViewModel
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.core.GrubDetailsViewModelFactory
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs.CancelConfirmationDialog
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs.GrubDetailsDialog
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs.GrubMenuDialog
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs.GrubSignUpDialog
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.SigningStatus
import org.bitspilani.ssms.messapp.screens.login.view.LoginActivity

class GrubDetailsFragment : Fragment() {

    private lateinit var viewModel: GrubDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id = arguments?.getLong("ID")!!

        viewModel = ViewModelProviders.of(this, GrubDetailsViewModelFactory(id))[GrubDetailsViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_grub_details_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_grub_details)

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.backBTN.setOnClickListener {
            rootPOV.findNavController().popBackStack()
        }

        rootPOV.viewDetailsBTN.setOnClickListener {
            val grub = (viewModel.order.value as UiOrder.ShowWorking).grub
            val bundle = bundleOf(
                "SIGNUP_DEADLINE" to grub.signUpDeadline,
                "CANCEL_DEADLINE" to grub.cancelDeadline,
                "SLOT1_TIME" to grub.slot1Time,
                "SLOT2_TIME" to grub.slot2Time,
                "FOOD_OPTION" to grub.foodOption.toString(),
                "VEG_VENUE" to grub.vegVenue,
                "NON_VEG_VENUE" to grub.nonVegVenue
            )
            GrubDetailsDialog().also { it.arguments = bundle }
                .show(childFragmentManager, "Grub Details Dialog")
        }

        rootPOV.viewVegMenuBTN.setOnClickListener {
            val grub = (viewModel.order.value as UiOrder.ShowWorking).grub
            val bundle = bundleOf(
                "FOOD_TYPE" to FoodType.Veg.toString(),
                "MENU" to grub.vegMenu.toMutableList(),
                "PRICE" to grub.vegPrice
            )
            GrubMenuDialog().also { it.arguments = bundle }
                .show(childFragmentManager, "Grub Veg Menu Dialog")
        }

        rootPOV.viewNonVegMenuBTN.setOnClickListener {
            val grub = (viewModel.order.value as UiOrder.ShowWorking).grub
            val bundle = bundleOf(
                "FOOD_TYPE" to FoodType.NonVeg.toString(),
                "MENU" to grub.nonVegMenu.toMutableList(),
                "PRICE" to grub.nonVegPrice
            )
            GrubMenuDialog().also { it.arguments = bundle }
                .show(childFragmentManager, "Grub Non-Veg Menu Dialog")
        }

        rootPOV.signUpBTN.setOnClickListener {
            val grub = (viewModel.order.value as UiOrder.ShowWorking).grub
            val bundle = bundleOf(
                "FOOD_OPTION" to grub.foodOption.toString()
            )
            GrubSignUpDialog().also { it.arguments = bundle }
                .show(childFragmentManager, "Grub SignUp Dialog")
        }

        rootPOV.cancelBTN.setOnClickListener {
            CancelConfirmationDialog().show(childFragmentManager, "Cancel Confirmation Dialog")
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.grub)
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
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }


    fun signUpForMenu(foodType: FoodType) {
        viewModel.onSignUpAction(foodType)
    }

    fun cancelGrub() {
        viewModel.onCancelAction()
    }


    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        errorLBL.text = error
    }

    private fun showWorkingState(grub: ViewLayerGrub) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)

        view!!.nameLBL.text = grub.name
        view!!.organizerLBL.text = grub.organizer
        view!!.dateLBL.text = grub.date
        view!!.statusLBL.text = when(grub.signingStatus) {
            SigningStatus.SignedForVeg    -> "Signed up for veg"
            SigningStatus.SignedForNonVeg -> "Signed for non-veg"
            SigningStatus.Available       -> "Not signed"
            SigningStatus.NotAvailable    -> "Not available"
        }
        view!!.slotLBL.text = grub.slot

        when(grub.signingStatus) {
            SigningStatus.Available    -> {
                view!!.signUpBTN.visibility = View.VISIBLE
                view!!.cancelBTN.visibility = View.GONE
            }
            SigningStatus.NotAvailable -> {
                view!!.signUpBTN.visibility = View.GONE
                view!!.cancelBTN.visibility = View.GONE
            }
            else                       -> {
                view!!.signUpBTN.visibility = View.GONE
                view!!.cancelBTN.visibility = View.VISIBLE
            }
        }

        when(grub.foodOption) {
            FoodOption.Veg          -> {
                viewVegMenuBTN.visibility = View.VISIBLE
                viewNonVegMenuBTN.visibility = View.GONE
            }
            FoodOption.NonVeg       -> {
                viewVegMenuBTN.visibility = View.GONE
                viewNonVegMenuBTN.visibility = View.VISIBLE
            }
            FoodOption.VegAndNonVeg -> {
                viewVegMenuBTN.visibility = View.VISIBLE
                viewNonVegMenuBTN.visibility = View.VISIBLE
            }
        }
    }
}
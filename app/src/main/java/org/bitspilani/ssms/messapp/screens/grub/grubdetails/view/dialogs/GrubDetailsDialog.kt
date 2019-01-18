package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dia_grub_details.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption

class GrubDetailsDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val signUpDeadline = arguments?.getString("SIGNUP_DEADLINE")
        val cancelDeadline = arguments?.getString("CANCEL_DEADLINE")

        val slot1Time = arguments?.getString("SLOT1_TIME")
        val slot2Time = arguments?.getString("SLOT2_TIME")

        val foodOption = FoodOption.valueOf(arguments?.getString("FOOD_OPTION")!!)
        val vegVenue = arguments?.getString("VEG_VENUE")
        val nonVegVenue = arguments?.getString("NON_VEG_VENUE")

        val rootPOV = inflater.inflate(R.layout.dia_grub_details, container, false)

        rootPOV.signUpDeadlineLBL.text = signUpDeadline
        rootPOV.cancelDeadlineLBL.text = cancelDeadline

        rootPOV.slot1TimeLBL.text = slot1Time
        rootPOV.slot2TimeLBL.text = slot2Time

        when(foodOption) {
            FoodOption.Veg          -> {
                rootPOV.vegVenueLIN.visibility = View.VISIBLE
                rootPOV.nonVegVenueLIN.visibility = View.GONE

                rootPOV.vegVenueLBL.text = vegVenue
            }
            FoodOption.NonVeg       -> {
                rootPOV.vegVenueLIN.visibility = View.GONE
                rootPOV.nonVegVenueLIN.visibility = View.VISIBLE

                rootPOV.nonVegVenueLBL.text = nonVegVenue
            }
            FoodOption.VegAndNonVeg -> {
                rootPOV.vegVenueLIN.visibility = View.VISIBLE
                rootPOV.nonVegVenueLIN.visibility = View.VISIBLE

                rootPOV.vegVenueLBL.text = vegVenue
                rootPOV.nonVegVenueLBL.text = nonVegVenue
            }
        }


        rootPOV.closeBTN.setOnClickListener {
            dismiss()
        }

        return rootPOV
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //To make rounded corners visible
        return dialog
    }
}
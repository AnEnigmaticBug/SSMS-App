package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dia_grub_sign_up.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.GrubDetailsFragment
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType

class GrubSignUpDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val foodOption = FoodOption.valueOf(arguments?.getString("FOOD_OPTION")!!)

        val rootPOV = inflater.inflate(R.layout.dia_grub_sign_up, container, false)

        when(foodOption) {
            FoodOption.Veg          -> {
                val lightGray = ContextCompat.getColor(context!!, R.color.gry01)
                rootPOV.nonVegMenuRBT.setTextColor(lightGray)

                rootPOV.nonVegMenuRBT.isClickable = false

                rootPOV.vegMenuRBT.isChecked = true
            }
            FoodOption.NonVeg       -> {
                val lightGray = ContextCompat.getColor(context!!, R.color.gry01)
                rootPOV.vegMenuRBT.setTextColor(lightGray)

                rootPOV.vegMenuRBT.isClickable = false

                rootPOV.nonVegMenuRBT.isChecked = true
            }
        }

        rootPOV.closeBTN.setOnClickListener {
            dismiss()
        }

        rootPOV.acceptBTN.setOnClickListener {
            dismiss()
            when {
                rootPOV.vegMenuRBT.isChecked    -> (parentFragment as GrubDetailsFragment).signUpForMenu(FoodType.Veg)
                rootPOV.nonVegMenuRBT.isChecked -> (parentFragment as GrubDetailsFragment).signUpForMenu(FoodType.NonVeg)
                else                            -> Toast.makeText(context, "Please select one menu", Toast.LENGTH_SHORT).show()
            }
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
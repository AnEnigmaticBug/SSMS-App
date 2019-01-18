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
import kotlinx.android.synthetic.main.dia_grub_menu.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.adapters.GrubItemsAdapter
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType

class GrubMenuDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val foodType = FoodType.valueOf(arguments?.getString("FOOD_TYPE")!!)
        val menu = arguments?.getStringArrayList("MENU")?: listOf<String>()
        val price = arguments?.getString("PRICE")

        val rootPOV = inflater.inflate(R.layout.dia_grub_menu, container, false)

        rootPOV.dialogTitleLBL.text = when(foodType) {
            FoodType.Veg    -> "Veg Menu"
            FoodType.NonVeg -> "Non-Veg Menu"
        }

        rootPOV.grubItemsRCY.adapter = GrubItemsAdapter().also { it.items = menu!! }

        rootPOV.priceLBL.text = price

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
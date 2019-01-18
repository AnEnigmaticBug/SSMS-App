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
import kotlinx.android.synthetic.main.dia_cancel_confirmation.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.GrubDetailsFragment

class CancelConfirmationDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.dia_cancel_confirmation, container, false)

        rootPOV.declineBTN.setOnClickListener {
            dismiss()
        }

        rootPOV.acceptBTN.setOnClickListener {
            dismiss()
            (parentFragment as GrubDetailsFragment).cancelGrub()
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
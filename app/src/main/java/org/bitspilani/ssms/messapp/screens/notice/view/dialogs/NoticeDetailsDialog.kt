package org.bitspilani.ssms.messapp.screens.notice.view.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dia_notice_details.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id

class NoticeDetailsDialog : DialogFragment() {

    interface ClickListener {

        fun onDeleteBTNPressed(id: Id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(arguments?.getLong("ID") == null) {
            Toast.makeText(context, "An error occurred :(", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        val id = arguments!!.getLong("ID")
        val heading = arguments?.getString("HEADING")?: "Error"
        val content = arguments?.getString("CONTENT")?: "Something has gone wrong"

        val rootPOV = inflater.inflate(R.layout.dia_notice_details, container, false)

        rootPOV.headingLBL.text = heading
        rootPOV.contentLBL.text = content

        rootPOV.closeBTN.setOnClickListener {
            this.dismiss()
        }

        rootPOV.deleteBTN.setOnClickListener {
            (parentFragment as ClickListener).onDeleteBTNPressed(id)
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
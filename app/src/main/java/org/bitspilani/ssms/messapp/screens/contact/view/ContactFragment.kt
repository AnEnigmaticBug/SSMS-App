package org.bitspilani.ssms.messapp.screens.contact.view

import android.content.Context
import android.os.Bundle
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fra_contact_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.contact.core.ContactViewModel
import org.bitspilani.ssms.messapp.screens.contact.core.ContactViewModelFactory
import org.bitspilani.ssms.messapp.screens.contact.view.adapters.ContactsAdapter
import org.bitspilani.ssms.messapp.screens.contact.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.contact.view.model.ViewLayerContact

class ContactFragment : Fragment(), ContactsAdapter.ClickListener {

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, ContactViewModelFactory())[ContactViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_contact_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_contact)

        rootPOV.contactsRCY.adapter = ContactsAdapter(this)

        rootPOV.backBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.contacts)
                is UiOrder.ShowFailure -> showFailureState(it.error)
                is UiOrder.ShowLoading -> showLoadingState()
            }
        })

        return rootPOV
    }

    override fun onPhoneNumberClicked(phone: String) {
        val clipboard = context?.applicationContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        clipboard?.let {
            it.text = phone
            Toast.makeText(context, "Copied phone number :)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showWorkingState(contacts: List<ViewLayerContact>) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)
        (view?.contactsRCY?.adapter as ContactsAdapter).contacts = contacts
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        view!!.errorLBL.text = error
    }
}
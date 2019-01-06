package org.bitspilani.ssms.messapp.screens.notice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fra_notice_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.notice.core.NoticeViewModel
import org.bitspilani.ssms.messapp.screens.notice.core.NoticeViewModelFactory
import org.bitspilani.ssms.messapp.screens.notice.view.dialogs.HelpDialog
import org.bitspilani.ssms.messapp.screens.notice.view.dialogs.NoticeDetailsDialog
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.view.adapters.NoticesAdapter
import org.bitspilani.ssms.messapp.screens.notice.view.dialogs.DeleteConfirmationDialog
import org.bitspilani.ssms.messapp.screens.notice.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.notice.view.model.ViewLayerNotice

class NoticeFragment : Fragment(), NoticesAdapter.ClickListener, NoticeDetailsDialog.ClickListener, DeleteConfirmationDialog.DeleteAllNoticesListener {

    private lateinit var viewModel: NoticeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, NoticeViewModelFactory())[NoticeViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_notice_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_notice)

        rootPOV.noticesRCY.adapter = NoticesAdapter(this)

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.infoBTN.setOnClickListener {
            HelpDialog().show(childFragmentManager, "Help Dialog")
        }

        rootPOV.deleteAllBTN.setOnClickListener {
            DeleteConfirmationDialog().show(childFragmentManager, "Delete-All Notices Confirmation Dialog")
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowLoading -> showLoadingState()
                is UiOrder.ShowWorking -> showWorkingState(it.notices)
                is UiOrder.ShowFailure -> showFailureState(it.error)
                is UiOrder.MoveToLogin -> TODO("Follow the move to login order in notice screen")
            }
        })

        viewModel.toast.observe(this, Observer {
            if(it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }

    override fun onAllNoticesDeleted() {
        viewModel.onDeleteAllNoticesAction()
    }

    override fun onNoticeClicked(id: Id) {
        NoticeDetailsDialog().also {
            val bundle = Bundle().apply {
                putLong("ID", id)
                putString("HEADING", (viewModel.order.value as UiOrder.ShowWorking).notices.find { it.id == id }?.heading)
                putString("CONTENT", (viewModel.order.value as UiOrder.ShowWorking).notices.find { it.id == id }?.content)
            }
            it.arguments = bundle
        }
            .show(childFragmentManager, "Notice Details")
    }

    override fun onDeleteBTNPressed(id: Id) {
        viewModel.onDeleteNoticeByIdAction(id)
    }

    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showWorkingState(notices: List<ViewLayerNotice>) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)
        (view!!.noticesRCY.adapter as NoticesAdapter).notices = notices
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        view!!.errorLBL.text = error
    }
}
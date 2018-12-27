package org.bitspilani.ssms.messapp.screens.notice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fra_notice.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.notice.core.NoticeViewModel
import org.bitspilani.ssms.messapp.screens.notice.core.NoticeViewModelFactory
import org.bitspilani.ssms.messapp.screens.notice.view.dialogs.HelpDialog
import org.bitspilani.ssms.messapp.screens.notice.view.dialogs.NoticeDetailsDialog
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.view.adapters.NoticesAdapter

class NoticeFragment : Fragment(), NoticesAdapter.ClickListener, NoticeDetailsDialog.ClickListener {

    private lateinit var viewModel: NoticeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, NoticeViewModelFactory())[NoticeViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_notice, container, false)

        rootPOV.noticesRCY.adapter = NoticesAdapter(this)

        rootPOV.infoBTN.setOnClickListener {
            HelpDialog().show(childFragmentManager, "Help Dialog")
        }

        rootPOV.deleteAllBTN.setOnClickListener {
            viewModel.onDeleteAllNoticesAction()
        }

        viewModel.notices.observe(this, Observer {
            (rootPOV.noticesRCY.adapter as NoticesAdapter).notices = it
        })

        return rootPOV
    }

    override fun onNoticeClicked(id: Id) {
        NoticeDetailsDialog().also {
            val bundle = Bundle().apply {
                putLong("ID", id)
                putString("HEADING", viewModel.notices.value?.find { it.id == id }?.heading)
                putString("CONTENT", viewModel.notices.value?.find { it.id == id }?.content)
            }
            it.arguments = bundle
        }
            .show(childFragmentManager, "Notice Details")
    }

    override fun onDeleteBTNPressed(id: Id) {
        viewModel.onDeleteNoticeByIdAction(id)
    }
}
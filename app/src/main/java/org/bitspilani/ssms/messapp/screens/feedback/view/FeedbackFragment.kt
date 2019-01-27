package org.bitspilani.ssms.messapp.screens.feedback.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fra_feedback_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.feedback.core.FeedbackViewModel
import org.bitspilani.ssms.messapp.screens.feedback.core.FeedbackViewModelFactory
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Tag
import org.bitspilani.ssms.messapp.screens.feedback.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.feedback.view.model.ViewLayerFeedback
import org.bitspilani.ssms.messapp.screens.login.view.LoginActivity

class FeedbackFragment : Fragment() {

    private lateinit var viewModel: FeedbackViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, FeedbackViewModelFactory())[FeedbackViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_feedback_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_feedback)


        rootPOV.backBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.sendBTN.setOnClickListener {
            viewModel.onGiveFeedbackAction()
        }

        rootPOV.contentTXT.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                viewModel.onContentChangeAction(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        class TagCheckBoxListener(private val tag: Tag) : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                when(isChecked) {
                    true  -> viewModel.onAddTagAction(tag)
                    false -> viewModel.onRemoveTagAction(tag)
                }
            }
        }

        rootPOV.appTagCHK.setOnCheckedChangeListener(TagCheckBoxListener(Tag.App))

        rootPOV.foodTagCHK.setOnCheckedChangeListener(TagCheckBoxListener(Tag.Food))

        rootPOV.hygieneTagCHK.setOnCheckedChangeListener(TagCheckBoxListener(Tag.Hygiene))

        rootPOV.serviceTagCHK.setOnCheckedChangeListener(TagCheckBoxListener(Tag.Service))

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.feedback)
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


    private fun showLoadingState() {
        (view as ConstraintLayout).setState(R.id.loading, 0, 0)
    }

    private fun showWorkingState(feedback: ViewLayerFeedback) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)

        if(view!!.contentTXT.text.toString() != feedback.content) {
            view!!.contentTXT.setText(feedback.content)
        }

        view!!.appTagCHK.isChecked = Tag.App in feedback.tags
        view!!.foodTagCHK.isChecked = Tag.Food in feedback.tags
        view!!.hygieneTagCHK.isChecked = Tag.Hygiene in feedback.tags
        view!!.serviceTagCHK.isChecked = Tag.Service in feedback.tags
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)

        view!!.errorLBL.text = error
    }
}
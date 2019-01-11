package org.bitspilani.ssms.messapp.screens.feedback.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Feedback
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Tag
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepository
import org.bitspilani.ssms.messapp.screens.feedback.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.feedback.view.model.ViewLayerFeedback
import org.bitspilani.ssms.messapp.util.NoConnectionException
import org.bitspilani.ssms.messapp.util.NoLoggedUserException
import org.bitspilani.ssms.messapp.util.toMut

class FeedbackViewModel(private val fRepo: FeedbackRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = MutableLiveData()


    private val blankFeedback = ViewLayerFeedback("", setOf())


    private var feedback = blankFeedback


    init {
        order.toMut().value = UiOrder.ShowWorking(blankFeedback)
    }


    fun onGiveFeedbackAction() {
        if(feedback.content.isBlank()) {
            toast.toMut().value = "Please enter something as feedback"
            return
        }
        order.toMut().value = UiOrder.ShowLoading
        fRepo.giveFeedback(Feedback(feedback.content, feedback.tags))
            .subscribe(
                {
                    order.toMut().postValue(UiOrder.ShowWorking(blankFeedback))
                    toast.toMut().postValue("Feedback submitted successfully")
                },
                {
                    order.toMut().postValue(when(it) {
                        is NoConnectionException -> UiOrder.ShowFailure("Device isn't connected to the internet")
                        is NoLoggedUserException -> UiOrder.MoveToLogin
                        else                     -> UiOrder.ShowFailure("Something went wrong")
                    })
                }
            )
    }

    fun onContentChangeAction(content: String) {
        feedback = feedback.copy(content = content)
        order.toMut().value = UiOrder.ShowWorking(feedback)

    }

    fun onAddTagAction(tag: Tag) {
        Log.d("FeedbackViewModel", "AddTag: $tag")
        feedback = feedback.copy(tags = feedback.tags.toMutableSet().also { it.add(tag) })
        order.toMut().value = UiOrder.ShowWorking(feedback)
    }

    fun onRemoveTagAction(tag: Tag) {
        feedback = feedback.copy(tags = feedback.tags.toMutableSet().also { it.remove(tag) })
        order.toMut().value = UiOrder.ShowWorking(feedback)
    }

    fun onRetryAction() {
        order.toMut().value = UiOrder.ShowWorking(feedback)
    }
}
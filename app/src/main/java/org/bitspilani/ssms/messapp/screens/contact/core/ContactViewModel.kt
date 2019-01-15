package org.bitspilani.ssms.messapp.screens.contact.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.contact.core.model.Contact
import org.bitspilani.ssms.messapp.screens.contact.data.repo.ContactRepository
import org.bitspilani.ssms.messapp.screens.contact.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.contact.view.model.ViewLayerContact
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut

class ContactViewModel(private val cRepo: ContactRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()


    private val d1 = CompositeDisposable()


    init {
        initialize()
    }


    fun onRetryAction() {
        initialize()
    }


    private fun initialize() {
        order.toMut().value = UiOrder.ShowLoading
        d1.set(cRepo.getAllContacts()
            .map { _contacts -> _contacts.map { ViewLayerContact(it.name, it.post, it.picUrl, it.phone) } }
            .map { _contacts -> UiOrder.ShowWorking(_contacts) }
            .subscribe(
                { _order ->
                    order.toMut().postValue(_order)
                },
                {
                    val message = when(it) {
                        is NoDataSourceException -> "Couldn't connect to server"
                        else                     -> "Something went wrong :("
                    }
                    order.toMut().postValue(UiOrder.ShowFailure(message))
                }
            ))
    }


    private fun Contact.toViewLayer(): ViewLayerContact {
        return ViewLayerContact(name.replace(' ', '\n'), post, picUrl, "+91 $phone")
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}
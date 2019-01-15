package org.bitspilani.ssms.messapp.screens.contact.data.retrofit

import io.reactivex.Single
import org.bitspilani.ssms.messapp.screens.contact.data.retrofit.model.ContactResponse
import retrofit2.Response
import retrofit2.http.GET

interface ContactService {

    @GET("api/contacts")
    fun getAllContacts(): Single<Response<List<ContactResponse>>>
}
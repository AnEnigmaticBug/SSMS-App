package org.bitspilani.ssms.messapp.screens.profile.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fra_profile_working_state.*
import kotlinx.android.synthetic.main.fra_profile_working_state.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.profile.core.ProfileViewModel
import org.bitspilani.ssms.messapp.screens.profile.core.ProfileViewModelFactory
import org.bitspilani.ssms.messapp.screens.profile.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.profile.view.model.ViewLayerUser

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, ProfileViewModelFactory())[ProfileViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_profile_working_state, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.ctl_states_fra_profile)

        rootPOV.retryBTN.setOnClickListener {
            viewModel.onRetryAction()
        }

        rootPOV.logoutBTN.setOnClickListener {
            viewModel.onLogoutAction()
        }

        rootPOV.refreshBTN.setOnClickListener {
            viewModel.onRefreshQrCodeAction()
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowLoading -> showLoadingState()
                is UiOrder.ShowWorking -> showWorkingState(it.user)
                is UiOrder.ShowFailure -> showFailureState(it.error)
                is UiOrder.MoveToLogin -> TODO("Follow the move to login order in profile screen")
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

    @SuppressLint("CheckResult")
    private fun showWorkingState(user: ViewLayerUser) {
        (view as ConstraintLayout).setState(R.id.working, 0, 0)

        view!!.nameLBL.text = user.name
        view!!.bitsIdLBL.text = user.id
        view!!.roomLBL.text = user.room

        val glideConfig = RequestOptions()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_profile_pic_placeholder_100dp_100dp))
            .circleCrop()

        Glide.with(this)
            .load(user.profilePicUrl)
            .apply(glideConfig)
            .into(view!!.profilePicIMG)

        Observable.just(user.qrCode)
            .observeOn(Schedulers.computation())
            .map { it.toQrCode() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    rootPOV.qrCodeIMG.setImageBitmap(it)
                },
                {
                    Toast.makeText(context, "Couldn't display QR code", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun showFailureState(error: String) {
        (view as ConstraintLayout).setState(R.id.failure, 0, 0)
        view!!.errorLBL.text = error
    }

    private fun String.toQrCode(): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(this, BarcodeFormat.QR_CODE, 400, 400)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}
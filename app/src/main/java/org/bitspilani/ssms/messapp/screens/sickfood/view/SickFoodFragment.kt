package org.bitspilani.ssms.messapp.screens.sickfood.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fra_sick_food.view.*
import org.bitspilani.ssms.messapp.R

class SickFoodFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_sick_food, container, false)

        rootPOV.backBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        return rootPOV
    }
}
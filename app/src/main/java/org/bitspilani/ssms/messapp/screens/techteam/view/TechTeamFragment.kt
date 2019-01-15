package org.bitspilani.ssms.messapp.screens.techteam.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fra_tech_team.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.techteam.TechMembersAdapter

class TechTeamFragment() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_tech_team, container, false)

        rootPOV.techMembersRCY.adapter = TechMembersAdapter()

        rootPOV.backBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        return rootPOV
    }
}
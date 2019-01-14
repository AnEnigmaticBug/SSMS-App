package org.bitspilani.ssms.messapp.screens.about.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fra_about.view.*
import org.bitspilani.ssms.messapp.R

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_about, container, false)
        rootPOV.contentLBL.text = "The Society for Student Mess Services, SSMS (Reg. No 32/JHU/2012-13) is the only legally registered student body on campus, registered under Rajasthan Society Registration Act, 1958, responsible for managing the student mess system in BITS Pilani,  and ensuring efficient and satisfactory services in the same.\n\nThe society has an employee capacity of 153 and caters to the needs of over 4000 students in BITS-Pilani, Pilani Campus. The Society directly handles all administrative operations, financial transactions, legal issues, documents and details for all the 9 messes on campus. The Society has presently tied up with Blue Chip Hospitality and Aditya Food Solutions, who have been hired as consultants to monitor, review and suggest changes in the system.\n\n" +
                "Though all students registered in BITS Pilani are de facto members of this Society, the day-to-day operational aspects and all other major decisions are overseen by the Governing Council (SSMS). The Governing Council comprises of elected representatives from each of the messes, four members from the Advisory and Monitoring Committee (AMC), and two ex-officio members. The main post-holders in the Council include the President, Secretary and the Treasurer of the Society. Every student on campus is encouraged to voice his/her opinions and suggestions towards the improvement of the mess-system. To indulge in cliches, the Society is after all an initiative of the students, by the students, for the students of BITS Pilani."

        rootPOV.backBTN.setOnClickListener {
            findNavController().popBackStack()
        }

        return rootPOV
    }
}
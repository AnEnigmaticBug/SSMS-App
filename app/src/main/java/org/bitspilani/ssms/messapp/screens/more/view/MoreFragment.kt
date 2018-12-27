package org.bitspilani.ssms.messapp.screens.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fra_more.view.*
import org.bitspilani.ssms.messapp.R
import org.bitspilani.ssms.messapp.screens.more.view.adapters.MoreOptionsAdapter

class MoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_more, container, false)

        rootPOV.moreOptionsRCY.adapter = MoreOptionsAdapter()

        return rootPOV
    }
}
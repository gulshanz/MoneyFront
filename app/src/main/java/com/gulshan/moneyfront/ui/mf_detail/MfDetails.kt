package com.gulshan.moneyfront.ui.mf_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gulshan.moneyfront.MainActivity
import com.gulshan.moneyfront.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MfDetails : Fragment(R.layout.fragment_mf_deatails) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv: TextView = view.findViewById(R.id.text_name)
        tv.text = arguments?.getString("name") ?: ""
    }
}
package com.globe.homecontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.databinding.FragmentHomeDataControllerBinding

class HomeDataControllerFragment : Fragment() {

    companion object {
        fun newInstance() = HomeDataControllerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeDataControllerBinding.inflate(inflater, container, false).root
}

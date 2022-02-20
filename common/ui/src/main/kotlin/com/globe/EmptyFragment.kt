package com.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.ui.databinding.FragmentEmptyBinding

class EmptyFragment : Fragment() {

    companion object {
        fun newInstance() = EmptyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentEmptyBinding.inflate(inflater, container, false).root
}

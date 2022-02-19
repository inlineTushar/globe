package com.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {

    companion object {
        fun newInstance() = ErrorFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentErrorBinding.inflate(inflater, container, false).root
}

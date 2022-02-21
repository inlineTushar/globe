package com.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.ui.databinding.FragmentGenericErrorBinding

class GenericErrorFragment : Fragment() {

    companion object {
        fun newInstance() = GenericErrorFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentGenericErrorBinding.inflate(inflater, container, false).root
}

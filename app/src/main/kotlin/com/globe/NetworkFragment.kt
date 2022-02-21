package com.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.databinding.FragmentNetworkErrorBinding

class NetworkFragment : Fragment() {

    companion object {
        fun newInstance() = NetworkFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNetworkErrorBinding.inflate(inflater, container, false).root
}

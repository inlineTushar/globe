package com.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.globe.databinding.FragmentLoaderBinding

class LoaderFragment : Fragment() {

    companion object {
        fun newInstance() = LoaderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoaderBinding.inflate(inflater, container, false).root
}

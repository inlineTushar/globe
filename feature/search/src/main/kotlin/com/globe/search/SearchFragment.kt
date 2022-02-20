package com.globe.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.globe.platform.extension.viewLifecycle
import com.globe.search.databinding.FragmentSearchBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val DEBOUNCE_TIME = 150L
    }

    private var binding: FragmentSearchBinding by viewLifecycle()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchBinding.inflate(inflater, container, false)
        .also {
            binding = it
            initSearchMenu()
        }
        .root

    @OptIn(FlowPreview::class)
    private fun initSearchMenu() {
        val searchItem = binding.toolbar.menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView
            .textChanges()
            .debounce(DEBOUNCE_TIME)
            .onEach { searchViewModel.searchCounties(it) }
            .launchIn(MainScope())
    }
}

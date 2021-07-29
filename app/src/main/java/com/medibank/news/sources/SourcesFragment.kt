package com.medibank.news.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.medibank.news.common.ViewState
import com.medibank.news.common.createViewModel
import com.medibank.news.common.factories
import com.medibank.news.databinding.FragmentSourcesBinding

/**
 * Shows a list of availablesources for the user to select. Users can select multiple sources and the selections
 * will be persisted across app launches
 */
class SourcesFragment : Fragment() {

    private lateinit var binding: FragmentSourcesBinding
    private lateinit var viewModel: SourcesViewModel
    private val adapter: SourcesAdapter by lazy { SourcesAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSourcesBinding.inflate(layoutInflater)
        viewModel = createViewModel { SourcesViewModel(factories().getSourcesUseCase) }

        setupUi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindLiveData()
        loadSources()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUi() {
        binding.sourcesList.layoutManager = LinearLayoutManager(requireContext())
        binding.sourcesList.adapter = adapter

        binding.swipeLayout.setOnRefreshListener {
            loadSources()
        }
    }

    private fun bindLiveData() {
        adapter.setRowCallback { sourceId ->
            viewModel.saveSource(sourceId)
        }

        adapter.setSavedSourcesCallback {
            return@setSavedSourcesCallback factories().appPreferences.getSavedSources()
        }

        viewModel.sourcesList.observe(requireActivity(), { sources ->
            adapter.setSources(sources)
        })

        viewModel.viewState.observe(requireActivity(), { state ->
            when (state) {
                ViewState.ShowLoading -> binding.swipeLayout.isRefreshing = true

                ViewState.DismissLoading -> binding.swipeLayout.isRefreshing = false

                is ViewState.ShowError -> {
                    binding.swipeLayout.isRefreshing = false
                    Snackbar.make(binding.swipeLayout, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun loadSources() {
        viewModel.getAllSources()
    }
}
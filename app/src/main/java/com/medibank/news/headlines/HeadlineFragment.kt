package com.medibank.news.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.medibank.news.R
import com.medibank.news.common.ViewState
import com.medibank.news.common.createViewModel
import com.medibank.news.common.factories
import com.medibank.news.databinding.FragmentMainBinding
import com.medibank.news.detail.NewsDetailFragment

/**
 * Shows a list of headlines from user selected sources.
 */
open class HeadlineFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: HeadlineViewModel
    private val adapter by lazy { NewsHeadlineAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = createViewModel { HeadlineViewModel(factories().getNewsUseCase, factories().getSourcesUseCase) }

        setupUi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindLiveData()
        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadData() {
        viewModel.getHeadlines()
    }

    private fun setupUi() {
        binding.headlinesList.layoutManager = LinearLayoutManager(requireContext())
        binding.headlinesList.adapter = adapter

        binding.swipeLayout.setOnRefreshListener {
            loadData()
        }
    }

    private fun bindLiveData() {
        adapter.setCallback { headline ->
            val b = Bundle()
            b.putParcelable(NewsDetailFragment.ARG_HEADLINE, headline)
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_mainFragment_to_newsDetailFragment, b)
        }

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

        viewModel.headlineList.observe(requireActivity(), { headlines ->
            adapter.setHeadlines(headlines)
        })
    }
}
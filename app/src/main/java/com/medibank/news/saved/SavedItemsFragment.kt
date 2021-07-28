package com.medibank.news.saved

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
import com.medibank.news.databinding.FragmentSavedItemsBinding
import com.medibank.news.common.factories
import com.medibank.news.detail.NewsDetailFragment
import com.medibank.news.headlines.NewsHeadlineAdapter

class SavedItemsFragment : Fragment(){

    private lateinit var binding: FragmentSavedItemsBinding
    private lateinit var viewModel: SavedItemsViewModel
    private val adapter: NewsHeadlineAdapter by lazy { NewsHeadlineAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedItemsBinding.inflate(layoutInflater)
        viewModel = createViewModel { SavedItemsViewModel(factories().savedItemsUseCase) }

        setupUi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindData()
        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    fun setupUi(){
        binding.savedItemList.layoutManager = LinearLayoutManager(requireContext())
        binding.savedItemList.adapter = adapter

        binding.swipeLayout.setOnRefreshListener {
            loadData()
        }
    }

    private fun bindData(){
        adapter.setCallback { headline ->
            val b = Bundle()
            b.putParcelable(NewsDetailFragment.ARG_HEADLINE, headline)
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_savedItemsFragment_to_newsDetailFragment, b)
        }

        viewModel.savedHeadlines.observe(requireActivity(), { headlines ->
            adapter.setHeadlines(headlines)
        })

        viewModel.viewState.observe(requireActivity(), { state ->
            when(state){
                ViewState.DismissLoading -> binding.swipeLayout.isRefreshing = false

                ViewState.DismissLoading -> binding.swipeLayout.isRefreshing = false

                is ViewState.ShowError -> {
                    binding.swipeLayout.isRefreshing = false
                    Snackbar.make(binding.swipeLayout, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun loadData(){
        viewModel.loadSavedHeadlines()
    }
}
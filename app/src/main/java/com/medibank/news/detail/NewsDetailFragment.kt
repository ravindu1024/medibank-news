package com.medibank.news.detail

import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.news.R
import com.medibank.news.common.MainActivity
import com.medibank.news.common.createViewModel
import com.medibank.news.common.factories
import com.medibank.news.databinding.FragmentDetailBinding

/**
 * A simple webview wrapper that displays the content of a given headline. Allows the user to save or unsave
 * the current article.
 */
class NewsDetailFragment : Fragment() {

    companion object {
        const val ARG_HEADLINE = "ARG_HEADLINE"
    }

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: NewsDetailViewModel
    private var headline: NewsHeadline? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        viewModel = createViewModel { NewsDetailViewModel(factories().savedItemsUseCase) }
        setHasOptionsMenu(true)

        headline = arguments?.getParcelable(ARG_HEADLINE) as? NewsHeadline
        if (headline == null) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack()
        }

        binding.webview.webChromeClient = WebChromeClient()
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // This is to stop the webview from being annoying and throwing us into the default browser
                return false
            }
        }

        binding.webview.loadUrl(headline!!.url)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        val isSaved = viewModel.isItemSaved(headline!!)
        menu.getItem(0).title =
            if (isSaved) getString(R.string.menu_title_unsave) else getString(R.string.menu_title_save)

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                val isSaved = viewModel.isItemSaved(headline!!)
                if (isSaved) {
                    viewModel.removeSavedHeadline(headline!!)
                    Snackbar.make(binding.webview, getString(R.string.msg_article_deleted), Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addSavedHeadline(headline!!)
                    Snackbar.make(binding.webview, getString(R.string.msg_article_saved), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
package com.medibank.news.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.medibank.news.R
import com.medibank.news.databinding.ActivityMainBinding

/**
 * The main Activity that hosts all the fragments in the app and handles
 * fragment navigation
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.mainFragment, R.id.sourcesFragment, R.id.savedItemsFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    enableToolbarBackButton(false)
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    enableToolbarBackButton(true)
                }
            }
        }
    }

    private fun enableToolbarBackButton(enabled: Boolean) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(enabled)
            it.setDisplayShowHomeEnabled(enabled)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
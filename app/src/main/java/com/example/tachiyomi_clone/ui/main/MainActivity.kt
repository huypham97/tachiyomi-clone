package com.example.tachiyomi_clone.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityMainBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.ui.main.home.HomeFragment
import com.example.tachiyomi_clone.ui.main.search.SearchFragment
import com.example.tachiyomi_clone.ui.main.suggest.SuggestFragment


class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>() {

    private var activeFragment: Fragment? = null

    override val modelClass: Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        showHomeFragment()
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.bnvDashBoard.setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.nav_home -> {
                    showHomeFragment()
                    true
                }
                R.id.nav_suggest -> {
                    showSuggestFragment()
                    true
                }
                R.id.nav_search -> {
                    showSearchFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun showHomeFragment() {
        if (activeFragment is HomeFragment) return

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment?

        if (fragment == null) {
            fragment = HomeFragment.newInstance()
            fragmentTransaction.add(binding.flContainer.id, fragment, HomeFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        activeFragment?.let { fragmentTransaction.hide(it) }

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showSuggestFragment() {
        if (activeFragment is SuggestFragment) return

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(SuggestFragment.TAG) as SuggestFragment?

        if (fragment == null) {
            fragment = SuggestFragment.newInstance()
            fragmentTransaction.add(binding.flContainer.id, fragment, SuggestFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        activeFragment?.let { fragmentTransaction.hide(it) }

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showSearchFragment() {
        if (activeFragment is SearchFragment) return

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment =
            supportFragmentManager.findFragmentByTag(SearchFragment.TAG) as SearchFragment?

        if (fragment == null) {
            fragment = SearchFragment.newInstance()
            fragmentTransaction.add(binding.flContainer.id, fragment, SearchFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        activeFragment?.let { fragmentTransaction.hide(it) }

        fragmentTransaction.commit()

        activeFragment = fragment
    }
}
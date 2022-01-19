package com.sample.ui.main.activities

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.coroutineScope
import com.sample.base.BaseActivity
import com.sample.core.WeatherLogger
import com.sample.core.extensions.TAG
import com.sample.core.extensions.safeGet
import com.sample.ui.main.fragment.home.HomeWeatherFragment
import com.sample.ui.main.utils.circleRevealAnimation
import com.weather.sample.R
import com.weather.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * Activity with navigation graph
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    // region Variables
    override val viewModel = MainActivityViewModel::class.java
    override fun getBindingVariable() = BR._all
    override val layoutId = R.layout.activity_main
    private var searchView: SearchView? = null

    @ExperimentalCoroutinesApi
    private var queryFlow = MutableStateFlow("")
    // endregion Variables

    //region LIFECYCLE Methods
    override fun initUserInterface() {
        viewDataBinding.toolBar.apply {
            setNavigationOnClickListener {
                gotoFavAndSearchScreen()
            }
            setContentInsetsAbsolute(0, contentInsetRight)
            setContentInsetsRelative(0, contentInsetEnd)
            contentInsetStartWithNavigation = 0
        }
    }
    //endregion


    //region UTIL

    private fun gotoFavAndSearchScreen() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container)
        navHostFragment?.childFragmentManager?.fragments?.get(0)?.let { fragment ->
            if (fragment.isAdded && fragment is HomeWeatherFragment) {
                fragment.navigateToFavScreen()
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initSearchStateFlow() {
        lifecycle.coroutineScope.launch {
            getQueryTextChangeStateFlow()
                .debounce(1500)
                .onEach { query ->
                    injectedViewModel.getSearchResults(query)
                }.catch {
                }.launchIn(this)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun initToolbar() {
        viewDataBinding.toolBar.apply {
            inflateMenu(R.menu.fragment_search_menu)
            searchView = menu.findItem(R.id.action_search).actionView as SearchView?
            initSearchStateFlow()

            searchView?.apply {
                isIconified = false
                queryHint = getString(R.string.search)
                findViewById<View>(androidx.appcompat.R.id.search_plate)
                    ?.background = null
                setIconifiedByDefault(false)
                maxWidth = Integer.MAX_VALUE
                imeOptions = EditorInfo.IME_ACTION_SEARCH
            }

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_search -> {
                        circleRevealAnimation(
                            this, 1, true,
                            context = this@MainActivity
                        )
                    }

                    else -> WeatherLogger.d(TAG, "else case")
                }

                true
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getQueryTextChangeStateFlow(): Flow<String> {

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryFlow.value = newText.safeGet()
                return true
            }
        })

        WeatherLogger.d(TAG, "getQueryTextChangeStateFlow end queryFlow=$queryFlow")
        return queryFlow
    }

    fun controlVisibilityFavIcon(visible: Boolean) {
        if (visible) {
            viewDataBinding.toolBar.navigationIcon =
                ContextCompat.getDrawable(this, R.drawable.ic_add_to_fav)
        } else viewDataBinding.toolBar.navigationIcon = null
    }

    fun controlVisibilitySearchIcon(visible: Boolean) {
        if (visible.not())
            viewDataBinding.toolBar.collapseActionView()
    }

    //endregion
}

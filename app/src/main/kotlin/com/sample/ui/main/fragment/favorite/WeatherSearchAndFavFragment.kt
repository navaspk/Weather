package com.sample.ui.main.fragment.favorite

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.base.BaseFragment
import com.sample.base.ItemClickListener
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.ui.main.activities.MainActivity
import com.sample.ui.main.activities.MainActivityViewModel
import com.sample.ui.main.fragment.favorite.adapter.SearchCityResultAdapter
import com.sample.ui.main.utils.showToast
import com.weather.sample.BuildConfig
import com.weather.sample.R
import com.weather.sample.databinding.FragmentDetailsItemBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Fragment for searching city name adding to favorite.
 *
 * In future same fragment will used for displaying all favorite city locations
 */
class WeatherSearchAndFavFragment :
    BaseFragment<FragmentDetailsItemBinding, WeatherSearchAndFavFragmentViewModel>(),
    ItemClickListener, CityNameNavigator {
    // region VARIABLE
    private lateinit var sharedViewModel: MainActivityViewModel

    override val viewModel = WeatherSearchAndFavFragmentViewModel::class.java
    override fun getBindingVariable() = BR._all
    override val layoutId = R.layout.fragment_details_item

    private var adapter: SearchCityResultAdapter? = null
    // endregion

    // region LIFECYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SearchCityResultAdapter(this)
        activity?.run {
            sharedViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        }
        initObserver()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun initUserInterface(view: View?) {
        injectedViewModel.setNavigator(this)
        activity?.apply {
            (this as MainActivity).controlVisibilityFavIcon(false)
            this.controlVisibilitySearchIcon(true)
        }

        initToolbar()
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).controlVisibilitySearchIcon(false)
    }

    //endregion


    // region OVERRIDDEN

    override fun onItemClick(position: Int, view: View) {
        sharedViewModel.selectedCity = adapter?.getItems()?.get(position)
        findNavController().navigate(R.id.action_got_to_independent)
    }

    override fun onSearchCityNameFound(searchedResult: WeatherCityResponse) {
        viewDataBinding?.progressBar?.visibility = GONE
        adapter?.setItems(arrayListOf(searchedResult))
    }

    override fun onSearchCityNotFound() {
        viewDataBinding?.progressBar?.visibility = GONE
        activity?.let {
            showToast(it, getString(R.string.city_not_found))
        }
    }

    //endregion


    //region UTIL

    private fun initObserver() {
        sharedViewModel.queryString.observe(this) {
            if (it?.isNotEmpty() == true) {
                viewDataBinding?.progressBar?.visibility = VISIBLE
                injectedViewModel.searchCityByName(it, BuildConfig.API_KEY)
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initToolbar() {
        (activity as MainActivity).initToolbar()
    }

    private fun initRecyclerView() {
        viewDataBinding?.let {
            it.itemsRecyclerView.apply {
                activity?.apply {
                    layoutManager = LinearLayoutManager(this)
                }
                adapter = this@WeatherSearchAndFavFragment.adapter
            }
        }
    }

    //endregion


}

package com.sample.ui.main.fragment.independant


import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.base.BaseFragment
import com.sample.base.ItemClickListener
import com.sample.ui.main.activities.MainActivity
import com.sample.ui.main.activities.MainActivityViewModel
import com.sample.ui.main.fragment.home.adapter.WeatherAdapter
import com.weather.sample.R

import com.weather.sample.databinding.FragmentHomWeatherBinding

/**
 * This is independent fragment, used to display the all weather data related to selected location
 * after search
 *
 * </p>
 *
 * Data are asynchronously get updated from livedata which defined inside view model class
 */
class IndependentWeatherFragment :
    BaseFragment<FragmentHomWeatherBinding, IndependentViewModel>(), ItemClickListener {

    // region VARIABLE
    private var adapter: WeatherAdapter? = null

    override val viewModel = IndependentViewModel::class.java
    override fun getBindingVariable() = BR._all
    override val layoutId = R.layout.fragment_hom_weather

    private lateinit var sharedViewModel: MainActivityViewModel

    // endregion

    //region LIFECYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WeatherAdapter(this)
    }

    override fun initUserInterface(view: View?) {

        activity?.run {
            sharedViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
            (this as MainActivity).controlVisibilitySearchIcon(false)
        }
        viewDataBinding?.let {
            it.itemRecyclerView.apply {
                setHasFixedSize(true)
                activity?.apply {
                    layoutManager = LinearLayoutManager(this)
                }
                adapter = this@IndependentWeatherFragment.adapter
            }
        }

        adapter?.setItems(arrayListOf(sharedViewModel.selectedCity!!))
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    //end region


    // region OVERRIDDEN

    override fun onItemClick(position: Int, view: View) {
    }

    //endregion

}

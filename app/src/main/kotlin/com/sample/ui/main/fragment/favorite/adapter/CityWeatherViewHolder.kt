package com.sample.ui.main.fragment.favorite.adapter

import com.sample.base.BaseViewHolder
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.core.extensions.safeGet
import com.sample.ui.main.utils.showToast
import com.weather.sample.R
import com.weather.sample.databinding.RecyclerItemCityListBinding

/**
 * View holder class with data inflation
 */
class CityWeatherViewHolder(
    private var recyclerItem: RecyclerItemCityListBinding
) : BaseViewHolder<WeatherCityResponse>(recyclerItem.root) {

    override fun bindItem(item: WeatherCityResponse) {
        recyclerItem.apply {
            cityNameNameTextView.text = item.name
            countryNameTextView.text = item.sys?.country.safeGet()

            addToFavButton.let { view ->
                view.setOnClickListener {
                    view.context.apply {
                        showToast(this, getString(R.string.sorry_will_do))
                    }
                }
            }
        }
    }
}


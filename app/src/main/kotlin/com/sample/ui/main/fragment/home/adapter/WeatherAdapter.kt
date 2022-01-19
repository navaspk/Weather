package com.sample.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sample.base.BaseRecyclerAdapter
import com.sample.base.BaseViewHolder
import com.sample.base.ItemClickListener
import com.sample.core.domain.entity.WeatherCityResponse
import com.weather.sample.databinding.RecyclerItemHomeBinding

/**
 * Adapter with view holder instantiation
 */
class WeatherAdapter(
    itemClickListener: ItemClickListener
) : BaseRecyclerAdapter<WeatherCityResponse>(itemClickListener) {

    override fun createBaseViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<WeatherCityResponse> {
        return WeatherItemViewHolder(
            RecyclerItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

}

package com.sample.ui.main.fragment.home.adapter

import com.sample.base.BaseViewHolder
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.core.extensions.safeGet
import com.weather.sample.databinding.RecyclerItemHomeBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * View holder class with data inflation
 */
class WeatherItemViewHolder(
    private var recyclerItem: RecyclerItemHomeBinding
) : BaseViewHolder<WeatherCityResponse>(recyclerItem.root) {

    override fun bindItem(item: WeatherCityResponse) {
        recyclerItem.apply {
            locationNameTextView.text = item.name
            countryTextView.text = item.sys?.country.safeGet()
            countryTimeZonePlace.text = getCurrentTime()

            temperatureTextView.text = getCurrentTemp(item.main?.temp!!)
            if (item.weather?.isNotEmpty() == true)
                temperatureStatusTextView.text = item.weather?.get(0)?.description

            windTV.text = item.wind?.speed.toString()
            cloudTV.text = item.clouds?.all.toString()
            sunset.text = getTimeFromTimestamp(item.sys?.sunset)
            sunrise.text = getTimeFromTimestamp(item.sys?.sunrise)
            humidityTV.text = item.main?.humidity.toString()
        }

    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        return formatter.format(Date())
    }

    private fun getCurrentTemp(temp: Double): String = (temp - 273.15).toInt().toString()

    private fun getTimeFromTimestamp(time: Int?): String {
        val date = time?.toLong()?.let { Date(it) }
        return date?.let {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
                .toUpperCase(Locale.ROOT)
        } ?: ""
    }

}


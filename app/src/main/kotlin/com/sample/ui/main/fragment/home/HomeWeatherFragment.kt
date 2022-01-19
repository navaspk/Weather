package com.sample.ui.main.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.telephony.CellLocation
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.base.BaseFragment
import com.sample.base.ItemClickListener
import com.sample.ui.main.activities.MainActivityViewModel
import com.sample.ui.main.fragment.home.adapter.WeatherAdapter
import com.sample.ui.main.utils.DialogManipulator
import com.sample.ui.main.utils.hasInternetConnection
import com.sample.ui.main.utils.showToast
import com.weather.sample.BuildConfig
import com.weather.sample.R
import javax.inject.Inject
import android.content.Intent
import android.location.LocationProvider
import android.provider.Settings
import androidx.navigation.fragment.findNavController

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sample.ui.main.activities.MainActivity
import com.weather.sample.databinding.FragmentHomWeatherBinding

/**
 * This is fragment is a Home screen, used to display the all weather data from server by making api call
 * with the help of view model class
 *
 * </p>
 *
 * Data are asynchronously get updated from livedata which defined inside view model class
 */
class HomeWeatherFragment : BaseFragment<FragmentHomWeatherBinding, HomeWeatherFragmentViewModel>(),
    HomeWeatherFragmentNavigator, ItemClickListener, LocationListener {

    // region VARIABLE
    private var adapter: WeatherAdapter? = null
    private lateinit var sharedViewModel: MainActivityViewModel

    override val viewModel = HomeWeatherFragmentViewModel::class.java
    override fun getBindingVariable() = BR._all
    override val layoutId = R.layout.fragment_hom_weather

    @Inject
    lateinit var dialog: DialogManipulator
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var locationManager: LocationManager? = null

    // endregion

    //region LIFECYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectedViewModel.setNavigator(this)
        adapter = WeatherAdapter(this)
        activity?.run {
            sharedViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val list: MutableList<String> = ArrayList()
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            list.add(Manifest.permission.ACCESS_FINE_LOCATION)
            checkPermissionAndMakeAction(list)
            return
        }

        performAfterPermission()
        if (hasInternetConnection() && latitude != 0.0 && longitude != 0.0) {
            injectedViewModel.getCurrentLocationWeather(BuildConfig.API_KEY, latitude, longitude)
        }
    }

    override fun initUserInterface(view: View?) {

        activity.apply {
            (this as MainActivity).controlVisibilityFavIcon(true)
            this.controlVisibilitySearchIcon(false)
        }
        if (hasInternetConnection()) {
            viewDataBinding?.progressBar?.visibility = VISIBLE
            viewDataBinding?.let {
                it.itemRecyclerView.apply {
                    setHasFixedSize(true)
                    activity?.apply {
                        layoutManager = LinearLayoutManager(this)
                    }
                    adapter = this@HomeWeatherFragment.adapter
                }
            }

            // live data observer to show latest content to ui
            injectedViewModel.itemLiveData.observe(this) {
                viewDataBinding?.progressBar?.visibility = GONE
                if (it == null) {
                    activity?.let { context ->
                        showToast(context, getString(R.string.not_data))
                    }
                } else {
                    adapter?.setItems(arrayListOf(it))
                }
            }
        } else {
            activity?.let {
                dialog.singleButtonDialog(
                    context = it,
                    title = getString(R.string.internet_error),
                    message = getString(R.string.internet_error_message),
                    alertDialogListener = object : DialogManipulator.AlertDialogListener {
                        override fun onPositiveButtonClicked() {
                            it.finish()
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    //end region


    // region OVERRIDDEN

    override fun onItemClick(position: Int, view: View) {
    }

    override fun somethingWentWrong(responseError: Boolean) {
        viewDataBinding?.progressBar?.visibility = GONE
        activity?.let {
            if (responseError)
                showToast(it, getString(R.string.wrong_response))
            else
                showToast(it, getString(R.string.unknown_result))
        }
    }

    override fun onLocationChanged(location: Location?) {
        latitude = location?.latitude
        longitude = location?.longitude

        if (hasInternetConnection()) {
            injectedViewModel.getCurrentLocationWeather(BuildConfig.API_KEY, latitude, longitude)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        activity?.run {
            if (status ==
                LocationProvider.TEMPORARILY_UNAVAILABLE
            ) {
                showToast(this, getString(R.string.location_not_available))
            } else if (status == LocationProvider.OUT_OF_SERVICE) {
                showToast(this, getString(R.string.location_out_service))
            }
        }
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
        showToast(requireContext(), "Gps Disabled")
        val intent = Intent(
            Settings.ACTION_LOCATION_SOURCE_SETTINGS
        )
        startActivity(intent)
    }
    //endregion


    //region UTIL

    @SuppressLint("MissingPermission")
    private fun performAfterPermission() {
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L,
            100f, this
        )
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 0,
            0f, this
        )
        CellLocation.requestLocationUpdate()
    }

    private fun checkPermissionAndMakeAction(permissions: List<String>) {
        Dexter.withContext(requireContext())
            .withPermissions(
                permissions
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted())
                        performAfterPermission()
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken,
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }

    fun navigateToFavScreen() {
        findNavController().navigate(R.id.action_itemFragment_to_search)
    }

    //endregion

}

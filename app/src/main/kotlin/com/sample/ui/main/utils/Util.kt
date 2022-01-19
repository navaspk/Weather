package com.sample.ui.main.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import com.sample.ui.WeatherApp
import com.weather.sample.R

/**
 * Utility file for performing all utility generic operation, all the methods defined here
 * can access from through out the project
 */
fun showToast(context: Context, str: String) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}

fun hasInternetConnection(): Boolean {
    val cm =
        WeatherApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return try {
        val activeNetwork =
            cm?.activeNetworkInfo
        activeNetwork != null && (activeNetwork.isConnected || activeNetwork.type == ConnectivityManager.TYPE_ETHERNET)
    } catch (e: RuntimeException) {
        true
    }
}

fun circleRevealAnimation(myView: View, posFromRight: Int, isShow: Boolean, context: Context) {
    var width = myView.width
    if (posFromRight > 0) width -= posFromRight * context.resources.getDimensionPixelSize(R.dimen.dimen_48_dp)
    -context.resources.getDimensionPixelSize(R.dimen.dimen_48_dp) / 2

    width -= context.resources.getDimensionPixelSize(R.dimen.dimen_36_dp)

    val cx = width
    val cy = myView.height / 2

    val anim = if (isShow) ViewAnimationUtils.createCircularReveal(
        myView, cx, cy, 0f, width.toFloat()
    ) else ViewAnimationUtils.createCircularReveal(
        myView, cx, cy, width.toFloat(), 0f
    )

    anim.duration = 220.toLong()
    // make the view invisible when the animation is done
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (!isShow) {
                super.onAnimationEnd(animation)
                myView.visibility = View.INVISIBLE
            }
        }
    })
    // make the view visible and start the animation
    if (isShow) myView.visibility = View.VISIBLE
    // start the animation
    anim.start()
}

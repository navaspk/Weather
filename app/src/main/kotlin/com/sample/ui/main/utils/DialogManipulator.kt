package com.sample.ui.main.utils

import android.app.AlertDialog
import android.content.Context
import com.sample.core.WeatherLogger
import com.sample.core.di.scopes.PerActivity
import com.sample.core.extensions.TAG
import com.weather.sample.R
import javax.inject.Inject

/**
 * Dialog manipulator class to make all type of dialogs.
 */
@PerActivity
class DialogManipulator @Inject constructor() {

    fun singleButtonDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = context.getString(R.string.ok),
        alertDialogListener: AlertDialogListener? = null,
        cancelable: Boolean = false
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText) { dialogInterface, _ ->
                    alertDialogListener?.onPositiveButtonClicked()
                    dialogInterface?.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        } catch (e: IllegalStateException) {
            WeatherLogger.e(TAG, "singleButtonDialog " + e.localizedMessage)
        }
    }

    interface AlertDialogListener {

        fun onPositiveButtonClicked() {}

    }

}

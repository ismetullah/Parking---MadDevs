package com.ismet.parkingzonemaddevs.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import com.ismet.parkingzonemaddevs.R
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {

    val timestamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.assets
        val inS = manager.open(jsonFileName)

        val size = inS.available()
        val buffer = ByteArray(size)
        inS.read(buffer)
        inS.close()

        return String(buffer, Charsets.UTF_8)
    }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun stringToBitmap(str: String): Bitmap? {
        return try {
            val imageBytes = Base64.decode(str, 0)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    fun timeToString(time: Long): String {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(time))
    }

    fun hasBeenThreeMinutes(entryTime: Long): Boolean {
        val diff = Date().time - entryTime
        val min = diff / 1000
        return min == 20L || min > 20L
    }
}

package com.itfperu.appitf.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.work.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.itfperu.appitf.data.local.model.Month
import com.itfperu.appitf.ui.workManager.DireccionWork
import com.itfperu.appitf.ui.workManager.MedicoWork
import com.itfperu.appitf.ui.workManager.TargetWork
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.nio.channels.FileChannel
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.*

object Util {

    const val UrlFoto = "http://190.117.104.122/WebApi_Itf/Imagen/"
    private var FechaActual: String? = ""
    private const val img_height_default = 800
    private const val img_width_default = 600

    @SuppressLint("SimpleDateFormat")
    fun getFirstDay(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        return sdf.format(calendar.time)
    }

    fun getMonthCurrent(): Int {
        val c = Calendar.getInstance()
//        var month: String = String.format("%s", c.get(Calendar.MONTH) + 1)
//        if (month.length < 2) {
//            month = "0$month"
//        }
        return c.get(Calendar.MONTH) + 1
    }

    fun getMoths(): List<Month> {
        val m = ArrayList<Month>()
        val shortMonths: Array<String> = DateFormatSymbols(Locale("es", "ES")).months
        for (i in shortMonths.indices) {
            val shortMonth = shortMonths[i]
            m.add(Month(i + 1, shortMonth))
        }
//        val m1 = Month(1, "Enero")
//        val m2 = Month(2, "Febrero")
//        val m3 = Month(3, "Marzo")
//        val m4 = Month(4, "Abril")
//        val m5 = Month(5, "Mayo")
//        val m6 = Month(6, "Junio")
//        val m7 = Month(7, "Julio")
//        val m8 = Month(8, "Agosto")
//        val m9 = Month(9, "Septiembre")
//        val m10 = Month(10, "Octubre")
//        val m11 = Month(10, "Noviembre")
//        val m12 = Month(10, "Diciembre")
//        m.add(m1)
//        m.add(m2)
//        m.add(m3)
//        m.add(m4)
//        m.add(m5)
//        m.add(m6)
//        m.add(m7)
//        m.add(m8)
//        m.add(m9)
//        m.add(m10)
//        m.add(m11)
//        m.add(m12)

        return m
    }

    @SuppressLint("SimpleDateFormat")
    fun getLastaDay(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return sdf.format(calendar.time)
    }

    fun getFormatDate(date: Date): String {
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss a")
        return format.format(date)
    }

    fun getFecha(): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
//        return "05/10/2019"
    }

    fun getHora(): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("HH:mm aaa")
        return format.format(date)
    }

    fun getFechaActual(): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return format.format(date)
    }

    fun getHoraActual(): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("HH:mm:ss aaa")
        return format.format(date)
    }

    fun getFechaEditar(): String? {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("ddMMyyyy_HHmmssSSSS")
        FechaActual = format.format(date)
        return FechaActual
    }

    fun getFechaActualForPhoto(tipo: Int): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("ddMMyyyy_HHmmssSSSS")
        FechaActual = format.format(date)
        return String.format("%s_%s.jpg", tipo, FechaActual)
    }

    fun getFotoName(id: Int): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("ddMMyyyy_HHmmssSSSS")
        FechaActual = format.format(date)
        return String.format("Foto%s_%s.jpg", id, FechaActual)
    }

    fun toggleTextInputLayoutError(textInputLayout: TextInputLayout, msg: String?) {
        textInputLayout.error = msg
        textInputLayout.isErrorEnabled = msg != null
    }

    // TODO SOBRE ADJUNTAR PHOTO

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }
        val source: FileChannel? = FileInputStream(sourceFile).channel
        val destination: FileChannel = FileOutputStream(destFile).channel
        if (source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        source?.close()
        destination.close()
    }

    @Suppress("DEPRECATION")
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var result = ""
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        @SuppressLint("Recycle") val cursor =
            context.contentResolver.query(contentUri, proj, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
        }
        return result
    }

    fun getFolder(context: Context): File {
        val folder = File(context.getExternalFilesDir(null)!!.absolutePath)
        if (!folder.exists()) {
            val success = folder.mkdirs()
            if (!success) {
                folder.mkdir()
            }
        }
        return folder
    }

    // TODO SOBRE FOTO

    fun generateImageAsync(pathFile: String): Observable<Boolean> {
        return Observable.create { e ->
            e.onNext(comprimirImagen(pathFile))
            e.onComplete()
        }
    }

    private fun comprimirImagen(PathFile: String): Boolean {
        return try {
            val result = getRightAngleImage(PathFile)
            result == PathFile
        } catch (ex: Exception) {
            Log.i("exception", ex.message!!)
            false
        }
    }

    private fun getDateTimeFormatString(date: Date): String {
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy - hh:mm:ss a")
        return df.format(date)
    }

    fun getDateTimeFormatString(): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy - hh:mm:ss a")
        return df.format(date)
    }

    private fun processingBitmapSetDateTime(bm1: Bitmap?, captionString: String?): Bitmap? {
        //Bitmap bm1 = null;
        var newBitmap: Bitmap? = null
        try {

            var config: Bitmap.Config? = bm1!!.config
            if (config == null) {
                config = Bitmap.Config.ARGB_8888
            }
            newBitmap = Bitmap.createBitmap(bm1.width, bm1.height, config)

            val newCanvas = Canvas(newBitmap!!)
            newCanvas.drawBitmap(bm1, 0f, 0f, null)

            if (captionString != null) {

                val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
                paintText.color = Color.RED
                paintText.textSize = 22f
                paintText.style = Paint.Style.FILL
                paintText.setShadowLayer(0.7f, 0.7f, 0.7f, Color.YELLOW)

                val rectText = Rect()
                paintText.getTextBounds(captionString, 0, captionString.length, rectText)
                newCanvas.drawText(captionString, 0f, rectText.height().toFloat(), paintText)
            }

            //} catch (FileNotFoundException e) {
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newBitmap
    }


    private fun copyBitmatToFile(filename: String, bitmap: Bitmap): String {
        return try {
            val f = File(filename)

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            "true"

        } catch (ex: IOException) {
            ex.message.toString()
        }

    }


    private fun shrinkBitmap(file: String): Bitmap {

        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        options.inJustDecodeBounds = true

        val heightRatio = ceil((options.outHeight / img_height_default.toFloat()).toDouble()).toInt()
        val widthRatio = ceil((options.outWidth / img_width_default.toFloat()).toDouble()).toInt()

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio
            } else {
                options.inSampleSize = widthRatio
            }
        }

        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(file, options)

    }

    private fun shrinkBitmapOnlyReduce(
        file: String,
        captionString: String?
    ) {

        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        options.inJustDecodeBounds = true

        val heightRatio = ceil((options.outHeight / img_height_default.toFloat()).toDouble()).toInt()
        val widthRatio = ceil((options.outWidth / img_width_default.toFloat()).toDouble()).toInt()

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio
            } else {
                options.inSampleSize = widthRatio
            }
        }

        options.inJustDecodeBounds = false

        try {


            val b = BitmapFactory.decodeFile(file, options)

            var config: Bitmap.Config? = b.config
            if (config == null) {
                config = Bitmap.Config.ARGB_8888
            }
            val newBitmap = Bitmap.createBitmap(b.width, b.height, config)

            val newCanvas = Canvas(newBitmap)
            newCanvas.drawBitmap(b, 0f, 0f, null)

            if (captionString != null) {

                val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
                paintText.color = Color.RED
                paintText.textSize = 22f
                paintText.style = Paint.Style.FILL
                paintText.setShadowLayer(0.7f, 0.7f, 0.7f, Color.YELLOW)

                val rectText = Rect()
                paintText.getTextBounds(captionString, 0, captionString.length, rectText)
                newCanvas.drawText(captionString, 0f, rectText.height().toFloat(), paintText)
            }

            val fOut = FileOutputStream(file)
            val imageName = file.substring(file.lastIndexOf("/") + 1)
            val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)

            val out = FileOutputStream(file)
            if (imageType.equals("png", ignoreCase = true)) {
                newBitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
            } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                    "jpg",
                    ignoreCase = true
                )
            ) {
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
            }
            fOut.flush()
            fOut.close()
            newBitmap.recycle()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shrinkBitmapOnlyReduceCamera2(
        file: String
    ) {
        val b = BitmapFactory.decodeFile(file)
        val text = getDateTimeFormatString()
        var config: Bitmap.Config? = b.config
        if (config == null) {
            config = Bitmap.Config.ARGB_8888
        }
        val newBitmap = Bitmap.createBitmap(b.width, b.height, config)
        val newCanvas = Canvas(newBitmap)
        newCanvas.drawBitmap(b, 0f, 0f, null)

        val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.color = Color.RED
        paintText.textSize = 22f
        paintText.style = Paint.Style.FILL
        paintText.setShadowLayer(0.7f, 0.7f, 0.7f, Color.YELLOW)

        val rectText = Rect()
        paintText.getTextBounds(text, 0, text.length, rectText)
        newCanvas.drawText(text, 0f, rectText.height().toFloat(), paintText)

        val fOut = FileOutputStream(file)
        val imageName = file.substring(file.lastIndexOf("/") + 1)
        val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)

        val out = FileOutputStream(file)
        if (imageType.equals("png", ignoreCase = true)) {
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                "jpg",
                ignoreCase = true
            )
        ) {
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        fOut.flush()
        fOut.close()
        newBitmap.recycle()
    }

    // TODO SOBRE ROTAR LA PHOTO

    fun getRightAngleImage(photoPath: String): String {

        try {
            val ei = ExifInterface(photoPath)
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val degree: Int

            degree = when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> 0
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_UNDEFINED -> 0
                else -> 90
            }

            return rotateImage(degree, photoPath)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return photoPath
    }

    private fun rotateImage(degree: Int, imagePath: String): String {
        if (degree <= 0) {
            shrinkBitmapOnlyReduce(
                imagePath,
                getDateTimeFormatString(Date(File(imagePath).lastModified()))
            )
            return imagePath
        }
        try {

            var b: Bitmap? = shrinkBitmap(imagePath)
            val matrix = Matrix()
            if (b!!.width > b.height) {
                matrix.setRotate(degree.toFloat())
                b = Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, true)
                b = processingBitmapSetDateTime(
                    b,
                    getDateTimeFormatString(Date(File(imagePath).lastModified()))
                )
            }

            val fOut = FileOutputStream(imagePath)
            val imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
            val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)

            val out = FileOutputStream(imagePath)
            if (imageType.equals("png", ignoreCase = true)) {
                b!!.compress(Bitmap.CompressFormat.PNG, 70, out)
            } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                    "jpg",
                    ignoreCase = true
                )
            ) {
                b!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
            }
            fOut.flush()
            fOut.close()
            b!!.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imagePath
    }

    fun getVersion(context: Context): String {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionName
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    fun getImei(context: Context): String {
        val deviceUniqueIdentifier: String
        val telephonyManager: TelephonyManager? =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        deviceUniqueIdentifier = if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                @Suppress("DEPRECATION")
                telephonyManager.deviceId
            }
        } else {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
        return deviceUniqueIdentifier
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences("TOKEN", MODE_PRIVATE).getString("token", "empty")
    }

    fun getNotificacionValid(context: Context): String? {
        return context.getSharedPreferences("TOKEN", MODE_PRIVATE).getString("update", "")
    }

    fun updateNotificacionValid(context: Context) {
        context.getSharedPreferences("TOKEN", MODE_PRIVATE).edit().putString("update", "").apply()
    }

    fun snackBarMensaje(view: View, mensaje: String) {
        val mSnackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
        mSnackbar.setAction("Ok") { mSnackbar.dismiss() }
        mSnackbar.show()
    }

    fun toastMensaje(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }

    fun dialogMensaje(context: Context, title: String, mensaje: String) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(mensaje)
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
        dialog.show()
    }

    // TODO CLOSE TECLADO

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(edit: EditText, context: Context) {
        edit.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        // TODO FOR FRAGMENTS
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getDateDialogText(context: Context, text: TextView) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
            val fecha = "$day/$month/$year"
            text.text = fecha
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    fun getDateDialog(context: Context, input: TextInputEditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
            val fecha = "$day/$month/$year"
            input.setText(fecha)
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    fun getHourDialog(context: Context, input: TextInputEditText) {
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(context, { _, hourOfDay, minute ->
                val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                val minutes = if (minute < 10) "0$minute" else minute.toString()
                val day = if (hourOfDay < 12) "a.m." else "p.m."
                input.setText(String.format("%s:%s %s", hour, minutes, day))
            }, mHour, mMinute, false)
        timePickerDialog.show()
    }

    fun getCompareFecha(fechaInicial: String, fechaFinal: String): Boolean {
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd/MM/yyyy")
        var date1 = Date()
        try {
            date1 = format.parse(fechaFinal)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var date2 = Date()
        try {
            date2 = format.parse(fechaInicial)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date1.before(date2)
    }

    fun getDateDialog(context: Context, view: View, input: TextInputEditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
            val fecha = "$day/$month/$year"

            if (!getCompareFecha(getFecha(), fecha)) {
                input.setText(fecha)
            } else {
                snackBarMensaje(view, "Fecha Propuesta no debe ser menor a la fecha actual")
            }

        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun getTextHTML(html: String): Spanned {
//        textViewTotal.setText(
//            Util.getTextHTML(String.format("%s %s", "<font color='#4CAF50'>Total</font> : ", b.showTotal())),
//            TextView.BufferType.SPANNABLE
//        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_OPTION_USE_CSS_COLORS)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }

        //    Util.getTextHTML("<font color='red'>Cant. Galones</font> : " + h.cantidad),
        //                BufferType.SPANNABLE
    }

    fun getTextStyleHtml(html: String, input: TextView) {
        input.setText(
            getTextHTML(html),
            TextView.BufferType.SPANNABLE
        )
    }


    fun isNumeric(strNum: String): Boolean {
        try {
            val d = Integer.parseInt(strNum)
            Log.i("TAG", d.toString())
        } catch (nfe: NumberFormatException) {
            return false
        } catch (nfe: NullPointerException) {
            return false
        }
        return true
    }

    fun isDecimal(s: String): Boolean {
        try {
            val d = s.toDouble()
            Log.i("TAG", d.toString())
        } catch (nfe: NumberFormatException) {
            return false
        } catch (nfe: NullPointerException) {
            return false
        }
        return true
    }

    @Throws(IOException::class)
    fun deleteDirectory(file: File) {
        if (file.isDirectory) {
            for (ct: File in file.listFiles()!!) {
                ct.delete()
            }
        }
    }

    fun deletePhoto(photo: String, context: Context) {
        val f = File(getFolder(context), photo)
        if (f.exists()) {
            f.delete()
        }
    }

    fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f) shl shift
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f) shl shift
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    fun calculationByDistance(StartP: Location, EndP: LatLng): Double {
        val radius = 6371 * 1000  // radius of earth in Km * meters
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        return kmInDec.toDouble()
    }

    fun calculationByDistance(StartP: Location, EndP: Location): Double {
        val radius = 6371 * 1000  // radius of earth in Km * meters
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        return kmInDec.toDouble()
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun getAngleImage(photoPath: String): String {
        try {
            val ei = ExifInterface(photoPath)
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val degree: Int

            degree = when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> 0
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_UNDEFINED -> 0
                else -> 90
            }

            return rotateNewImage(degree, photoPath)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return photoPath
    }

    private fun rotateNewImage(degree: Int, imagePath: String): String {
        if (degree <= 0) {
            shrinkBitmapOnlyReduceCamera2(imagePath)
            return imagePath
        }
        try {

            var b: Bitmap? = BitmapFactory.decodeFile(imagePath)
            val matrix = Matrix()
            if (b!!.width > b.height) {
                matrix.setRotate(degree.toFloat())
                b = Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, true)
                b = processingBitmapSetDateTime(
                    b,
                    getDateTimeFormatString(Date(File(imagePath).lastModified()))
                )
            }

            val fOut = FileOutputStream(imagePath)
            val imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1)
            val imageType = imageName.substring(imageName.lastIndexOf(".") + 1)

            val out = FileOutputStream(imagePath)
            if (imageType.equals("png", ignoreCase = true)) {
                b!!.compress(Bitmap.CompressFormat.PNG, 100, out)
            } else if (imageType.equals("jpeg", ignoreCase = true) || imageType.equals(
                    "jpg",
                    ignoreCase = true
                )
            ) {
                b!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            fOut.flush()
            fOut.close()
            b!!.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imagePath
    }

    fun getLocationName(
        context: Context,
        input: TextInputEditText,
        location: Location,
        progressBar: ProgressBar
    ) {
        val nombre = arrayOf("")
        try {
            val addressObservable = Observable.just(
                Geocoder(context).getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )[0]
            )
            addressObservable.subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Address> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(address: Address) {
                        nombre[0] = address.getAddressLine(0)
                    }

                    override fun onError(e: Throwable) {
//                        toastMensaje(context, context.getString(R.string.try_again))
                        progressBar.visibility = View.GONE
                    }

                    override fun onComplete() {
                        input.setText(nombre[0])
                        progressBar.visibility = View.GONE
                    }
                })
        } catch (e: IOException) {
            toastMensaje(context, e.toString())
            progressBar.visibility = View.GONE
        }
    }

    fun getFolderAdjunto(
        file: String, context: Context, data: Intent
    ): Completable {
        return Completable.fromAction {
            val imagepath = getFolder(context).toString() + "/" + file
            val f = File(imagepath)
            if (!f.exists()) {
                try {
                    val success = f.createNewFile()
                    if (success) {
                        Log.i("TAG", "FILE CREATED")
                    }
                    copyFile(File(getRealPathFromURI(context, data.data!!)), f)
//                    shrinkBitmapOnlyReduceCamera2(imagepath)
                    getAngleImage(imagepath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // execute services
    fun executeMedicoWork(context: Context, tipo: Int) {
//        val downloadConstraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
        // Define the input data for work manager
        val data = Data.Builder()
        data.putInt("tipo", tipo)

        // Create an one time work request
        val downloadImageWork = OneTimeWorkRequest
            .Builder(MedicoWork::class.java)
            .setInputData(data.build())
//            .setConstraints(downloadConstraints)
            .build()
        WorkManager.getInstance(context).enqueue((downloadImageWork))
    }

    fun executeDireccionWork(context: Context, tipo: Int) {
//        val downloadConstraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
        // Define the input data for work manager
        val data = Data.Builder()
        data.putInt("tipo", tipo)

        // Create an one time work request
        val downloadImageWork = OneTimeWorkRequest
            .Builder(DireccionWork::class.java)
            .setInputData(data.build())
//            .setConstraints(downloadConstraints)
            .build()
        WorkManager.getInstance(context).enqueue((downloadImageWork))
    }

    fun executeTarget(context: Context, tipoTarget: String, tipo: Int) {
//        val downloadConstraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
        // Define the input data for work manager
        val data = Data.Builder()
        data.putString("tipoTarget", tipoTarget)
        data.putInt("tipo", tipo)

        // Create an one time work request
        val downloadImageWork = OneTimeWorkRequest
            .Builder(TargetWork::class.java)
            .setInputData(data.build())
//            .setConstraints(downloadConstraints)
            .build()
        WorkManager.getInstance(context).enqueue((downloadImageWork))
    }
}
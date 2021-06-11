package com.app.newsbreeze.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.app.newsbreeze.app.App
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {

    val STATUS_SUCCESS = 1
    val STATUS_OK = "ok"
    val APIKEY = "4c330c020d804790b7afe20bd6a86fe6"

    internal var letter = Pattern.compile("[a-zA-z]")
    internal var digit = Pattern.compile("[0-9]")

    val SHOULD_PRINT_LOG = false

    val sBuildType = BuildType.PROD

    private val SHARED_PREFERENCE_UTILS = "newsapp"
    private var sharedPreferences: SharedPreferences? = null

    private fun initializeSharedPreference() {
        sharedPreferences = App.instance.getSharedPreferences(
            SHARED_PREFERENCE_UTILS,
            Context.MODE_PRIVATE
        )
    }

    fun updateSharedPreferenceString(key: String, value: String) {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }

        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.commit()
    }


    fun updateSharedPreferenceInt(key: String, value: Int) {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }

        val editor = sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }


    fun updateSharedPreferenceBoolean(key: String, value: Boolean) {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }

        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }


    fun getSharedPreferenceString(key: String?): String? {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }

        return if (key != null) {
            sharedPreferences!!.getString(key, null)
        } else {
            null
        }
    }

    fun isAppInstalled(context: Context, packagename: String): Boolean {

        val pm = context.packageManager
        try {
            pm.getPackageInfo(packagename, 0)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun getSharedPreferenceInt(key: String?): Int {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }
        return if (key != null) {
            sharedPreferences!!.getInt(key, -1)
        } else {
            0
        }
    }

    fun getSharedPreferenceBoolean(key: String?): Boolean {
        if (sharedPreferences == null) {
            initializeSharedPreference()
        }
        return key != null && sharedPreferences!!.getBoolean(key, false)
    }

    fun isOnline(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm =
            App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val netInfo = cm.activeNetworkInfo
                if (netInfo != null) {
                    return (netInfo.isConnected() && (netInfo.getType() == ConnectivityManager.TYPE_WIFI || netInfo.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                val netInfo = cm.activeNetwork
                if (netInfo != null) {
                    val nc = cm.getNetworkCapabilities(netInfo);

                    return (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc!!.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ));
                }
            }
        }

        return haveConnectedWifi || haveConnectedMobile
    }

    enum class BuildType {
        QA, PROD
    }


    fun isEmailValid(email: String): Boolean {

        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()

    }

    fun clearSession() {

        if (sharedPreferences == null) {
            initializeSharedPreference()
        }
        sharedPreferences!!.edit().clear().apply()
    }

    fun validate(password: String): Boolean {
        return password
            .matches("(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-])".toRegex())
    }

    fun validPassword(password: String): Boolean {

        val pattern: Pattern
        val matcher: Matcher

        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
        // val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z][a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()

    }


    fun apiTime(time: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inputFormat.parse(time)
            val formattedDate: String = outputFormat.format(date)
            return formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertDate(dateInMilliseconds: Long): String? {
        val date = Date(dateInMilliseconds * 1000L)
        val format: DateFormat = SimpleDateFormat("dd MMM")
        format.timeZone = TimeZone.getTimeZone("Etc/UTC")
        val formatted = format.format(date)
        return formatted
    }


}
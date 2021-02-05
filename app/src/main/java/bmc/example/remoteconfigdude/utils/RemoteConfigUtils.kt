package bmc.example.remoteconfigdude.utils

import android.util.Log
import bmc.example.remoteconfigdude.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtils {

    private const val TAG = "RemoteConfigUtils"

    private const val WELCOME_TEXT = "welcome_text"
    private const val WELCOME_TEXT_COLOR = "welcome_text_color"
    private const val BG_COLOR = "bg_color"

    private val DEFAULTS: HashMap<String, Any> =
            hashMapOf(
                    WELCOME_TEXT to "Hello World!",
                    WELCOME_TEXT_COLOR to "#000000",
                    BG_COLOR to "#000000"
            )

    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init() {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0 // Kept 0 for quick debug
            } else {
                60 * 60 // Change this based on your requirement
            }
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        //remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setDefaultsAsync(DEFAULTS)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Config params updated: $updated")
                Log.d(TAG, "Fetch and activate succeeded")
            } else {
                Log.d(TAG, "Fetch failed")
            }
        }

        return remoteConfig
    }

    fun getWelcomeText(): String = remoteConfig.getString(WELCOME_TEXT)

    fun getWelcomeTextColor(): String = remoteConfig.getString(WELCOME_TEXT_COLOR)

    fun getBgColor(): String = remoteConfig.getString(BG_COLOR)
}
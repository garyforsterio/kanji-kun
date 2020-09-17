package io.garyforster.whatsthatkanji

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity


class SettingsPrompt : AppCompatActivity() {
    private var ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2323

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_prompt)
        checkPermission()
    }

    /**
     * Called when the button on the main screen is clicked
     */
    fun openAccessibilitySettings(view: View) {
//        val isAlreadyEnabled = isAccessibilityServiceEnabled(applicationContext, TextAnalyzerService.class )
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun isAccessibilityServiceEnabled(
        context: Context,
        service: Class<out AccessibilityService?>
    ): Boolean {
        val am =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName == context.packageName && enabledServiceInfo.name == service.name
            ) return true
        }
        return false
    }


    private fun checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }
}
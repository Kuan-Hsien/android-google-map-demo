package com.kuanhsien.app.sample.android_google_map_demo.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtil {

    fun hasPermission(context: Context, permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission)
    }

    fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        var ret = true
        for (permission in permissions) {
            ret = ret and hasPermission(context, permission)
        }
        return ret
    }

    fun requestPermissions(activity: Activity, permissions: List<String>, id: Int) {
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), id)
    }

    fun requestPermissions(fragment: Fragment, permissions: List<String>, id: Int) {
        fragment.requestPermissions(permissions.toTypedArray(), id)
    }

}
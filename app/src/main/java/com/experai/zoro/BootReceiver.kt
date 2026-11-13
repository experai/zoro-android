package com.experai.zoro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, ZoroForegroundService::class.java).apply {
                action = ZoroForegroundService.ACTION_START
                putExtra("mode", "standard")
            }
            context.startForegroundService(serviceIntent)
        }
    }
}

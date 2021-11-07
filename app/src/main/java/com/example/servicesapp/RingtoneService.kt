package com.example.servicesapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.provider.Settings
import android.widget.Toast
import java.lang.Process
import java.util.*

class RingtoneService: Service() {
    lateinit var player:MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        //Process.THREAD_PRIORITY_BACKGROUND
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        player = MediaPlayer.create(this,Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping
        player.start()
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)

        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }


    override fun onDestroy() {
        player.stop()
        super.onDestroy()
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}
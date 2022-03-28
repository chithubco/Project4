package com.echithub.project4

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.echithub.project4.utils.NotificationsHelper
import com.echithub.project4.utils.PERMISSION_WRITE_EXTENAL_STORAGE
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkDownloadPermission()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.i("Download ID ",id.toString())
            NotificationsHelper(this@MainActivity).createNotification() // Send Notification

        }
    }

    private fun checkDownloadPermission() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) { // No Permission

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) { // Previously denied Requesting Permission Rationale
                val builder = AlertDialog.Builder(this)
                    .setTitle("Storage Access Permission")
                    .setMessage("This app requires access to the local storage")
                    .setPositiveButton("Ask me") { dialog, id ->
                        requestExternalStoragePermission()
                    }.setNegativeButton("No") { dialog, id ->
                        Log.i(TAG, "Permission DENIED")
                    }

                val dialog = builder.create()
                dialog.show()

            } else { // Do not show rational and just request permission
                requestExternalStoragePermission()
            }

        } else {
            Log.i(TAG, "Permission already granted")
//                notifyMainFragmentPermissionGranted(true)
        }
    }

    private fun requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_WRITE_EXTENAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_WRITE_EXTENAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission already granted")
                } else {
                    Log.i(TAG, "Permission DENIED")
                }
            }
        }
    }

    fun download(uri: Uri, outputFileName: String) {

        val downloadRequest = DownloadManager.Request(uri)
            .setTitle("Downloading Something")
            .setDescription("Interesting Download $outputFileName")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outputFileName)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(downloadRequest)
        Toast.makeText(this, "Downloading Begins", Toast.LENGTH_LONG).show()
    }
}
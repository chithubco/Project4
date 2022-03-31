package com.echithub.project4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val extras = intent.extras?: return
        val downloadUrl = extras.getString("download_url")
        val downloadStatus = extras.getString("download_status")

        var downloadUrlText = findViewById<TextView>(R.id.tv_download_repo)
        var downloadStatusText = findViewById<TextView>(R.id.tv_download_status)
        var backButton = findViewById<Button>(R.id.btn_back_to_main)

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        if (downloadUrl != null) {
            Log.i("Intent Extra",downloadUrl)
            downloadUrlText.text = downloadUrl
            downloadStatusText.text = downloadStatus
        }
    }
}
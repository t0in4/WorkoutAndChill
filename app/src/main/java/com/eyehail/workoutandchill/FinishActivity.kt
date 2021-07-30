package com.eyehail.workoutandchill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        val toolbar_finish_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_finish_activity)
        setSupportActionBar(toolbar_finish_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_finish_activity.setNavigationOnClickListener{
            onBackPressed()
        }

        findViewById<Button>(R.id.btnFinish).setOnClickListener {
            finish()
        }
    }
}
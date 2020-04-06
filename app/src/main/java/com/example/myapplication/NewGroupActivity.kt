package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.Group
import kotlinx.android.synthetic.main.activity_group_detail.toolbar
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.serialization.json.Json
import java.util.Calendar

class NewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.floatingActionButtonAddContacts.setOnClickListener {
            val group = Group(
                editTextName.text.toString(),
                editTextDescription.text.toString(),
                editTextStartTime.text.toString(),
                editTextDate.text.toString()
            )

            this.startActivity(
                AddContactActivity::class.java,
                Json.stringify(Group.serializer(), group)
            )
        }

        this.editTextStartTime.setOnClickListener {
            showTimePickerDialog()
        }

        this.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun startActivity(activityClass: Class<*>, data: String = "") {
        val intent = Intent(this, activityClass)
        intent.putExtra("group", data)
        this.startActivity(intent)
    }

    private fun showTimePickerDialog() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                val currentTime = String.format("%02d:%02d", hour, minute)
                this.editTextStartTime.setText(currentTime)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val currentDate = String.format("%02d/%02d/$year", dayOfMonth, monthOfYear)
                this.editTextDate.setText(currentDate)
            },
            year,
            month,
            day
        ).show()
    }
}
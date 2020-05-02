package com.example.myapplication.ui.group.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.data.model.Group
import com.example.myapplication.databinding.ActivityNewGroupBinding
import com.example.myapplication.ui.contact.AddContactActivity
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import kotlinx.serialization.json.Json
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import java.util.*

class NewGroupActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val viewModel: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: ActivityNewGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_group)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.timePickerDialogData.observe(this, Observer { data ->
            if (data) {
                showTimePickerDialog()
            }
        })

        viewModel.datePickerDialogData.observe(this, Observer { data ->
            if (data) {
                showDatePickerDialog()
            }
        })

        viewModel.addGroupData.observe(this, Observer { data ->
            this.startActivity(
                AddContactActivity::class.java,
                Json.stringify(Group.serializer(), viewModel.group)
            )
        })

        binding.viewmodel = viewModel
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
                binding.editTextStartTime.setText(currentTime)
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
                binding.editTextDate.setText(currentDate)
            },
            year,
            month,
            day
        ).show()
    }
}
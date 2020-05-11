package com.example.myapplication.ui.group.add

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentGroupBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import java.util.*

class GroupFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()

    override val _viewModel: GroupsViewModel by kodeinViewModel()

    private lateinit var binding: FragmentGroupBinding
    private val LOCATION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        _viewModel.timePickerDialogData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showTimePickerDialog()
            }
        })

        _viewModel.datePickerDialogData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDatePickerDialog()
            }
        })

        _viewModel.locationData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLocationPicker()
            }
        })

        binding.viewmodel = _viewModel

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                binding.editTextLocation.setText(data.getStringExtra(LOCATION_ADDRESS))
                binding.viewmodel?.group?.location?.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                binding.viewmodel?.group?.location?.longitude = data.getDoubleExtra(LONGITUDE, 0.0)
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val currentTime = String.format("%02d:%02d", hour, minute)
                binding.editTextStartTime.setText(currentTime)
            },
            currentHour,
            currentMinute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val currentDate = String.format("%02d/%02d/$year", dayOfMonth, monthOfYear)
                binding.editTextDate.setText(currentDate)
            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun showLocationPicker() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withGeolocApiKey("AIzaSyBJXYFhwibyYCktSVPzNxfk2owfJtwCwpQ")
            .shouldReturnOkOnBackPressed()
            .withSatelliteViewHidden()
            .build(requireContext())

        startActivityForResult(locationPickerIntent, LOCATION_REQUEST_CODE)
    }
}

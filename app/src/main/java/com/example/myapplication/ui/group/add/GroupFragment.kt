package com.example.myapplication.ui.group.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentGroupBinding
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.utils.extension.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import java.util.*

class GroupFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val viewModel: GroupsViewModel by kodeinViewModel()
    private lateinit var binding: FragmentGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)

        viewModel.addGroupData.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_groupFragment_to_contactFragment)
            }
        })

        viewModel.timePickerDialogData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showTimePickerDialog()
            }
        })

        viewModel.datePickerDialogData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDatePickerDialog()
            }
        })

        binding.viewmodel = viewModel

        return binding.root
    }

    private fun showTimePickerDialog() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                val currentTime = String.format("%02d:%02d", hour, minute)
                binding.editTextStartTime.setText(currentTime)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            requireContext(),
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

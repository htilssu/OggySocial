package com.aesocial.aesocial.fragments.authentication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.aesocial.aesocial.R
import com.google.android.material.textfield.TextInputEditText


class RegisterBirthday : Fragment() {

    private lateinit var rootView: View
    private lateinit var btnNext: Button
    private lateinit var teBirthday: TextInputEditText
    private lateinit var datePickerDialog: DatePickerDialog
    private var isDatePickerDialogShow: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_register_birthday, container, false)
        initListener()
        return rootView
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        btnNext = rootView.findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.register_fragment_container, RegisterBirthday())
                .addToBackStack(null)
                .commit()
        }

        teBirthday = rootView.findViewById(R.id.teBirthday)
        teBirthday.setOnTouchListener { _, _ ->
            showDatePickerDialog()
            true
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                teBirthday.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Chọn", datePickerDialog)
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Hủy", datePickerDialog)
        datePickerDialog.setOnDismissListener {
            isDatePickerDialogShow = false
        }

        if (!isDatePickerDialogShow) {
            datePickerDialog.show()
            isDatePickerDialogShow = true
        }
    }


}
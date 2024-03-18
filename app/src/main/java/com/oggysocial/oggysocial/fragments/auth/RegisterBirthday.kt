package com.oggysocial.oggysocial.fragments.auth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.oggysocial.oggysocial.R
import java.time.LocalDate


class RegisterBirthday : Fragment() {

    private lateinit var rootView: View
    private lateinit var btnNext: Button
    private lateinit var teBirthday: TextInputEditText
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var birthday: String
    private var isDatePickerDialogShow: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_register_birthday, container, false)
        initListener()
        return rootView
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        // Next button
        btnNext = rootView.findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            if (isValid()) {
                context?.getSharedPreferences("OggySocial", Context.MODE_PRIVATE)?.edit()
                    ?.putString("birthday", birthday)?.apply()
                parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.register_fragment_container, EmailPasswordFragment())
                    .addToBackStack(null).commit()
            } else {
                val teBirthdayLayout =
                    rootView.findViewById<TextInputLayout>(R.id.teBirthdayLayout)
                teBirthdayLayout.error = getString(R.string.required_birthday)
            }
        }

        teBirthday = rootView.findViewById(R.id.teBirthday)
        teBirthday.setOnTouchListener { _, _ ->
            showDatePickerDialog()
            true
        }

        teBirthday.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(teBirthday.windowToken, 0)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val currentDate = LocalDate.now()
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)

                val age = LocalDate.from(selectedDate).until(currentDate).years

                if (age >= 18) {
                    birthday = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                    teBirthday.setText(birthday)
                } else {
                    teBirthday.error = getString(R.string.must_enough_age)
                    Toast.makeText(context, getString(R.string.must_enough_age), Toast.LENGTH_SHORT)
                        .show()
                }


            }, year, month, day
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

    private fun isValid(): Boolean {
        return teBirthday.text.toString().isNotEmpty()
    }


}
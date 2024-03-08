package com.oggysocial.oggysocial.fragments.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.oggysocial.oggysocial.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegisterName : Fragment() {

    private lateinit var rootView: View
    private lateinit var btnNext: Button
    private lateinit var teFirstName: TextInputEditText
    private lateinit var teLastName: TextInputEditText
    private lateinit var lastName: String
    private lateinit var firstName: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_register_name, container, false)
        initVariables()
        initListener()
        return rootView
    }

    private fun initVariables() {
        sharedPreferences = context?.getSharedPreferences("OggySocial", Context.MODE_PRIVATE)!!
        btnNext = rootView.findViewById(R.id.btnNext)
        teFirstName = rootView.findViewById(R.id.teFirstName)
        teLastName = rootView.findViewById(R.id.teLastName)
    }

    private fun initListener() {
        btnNext.setOnClickListener {
            firstName = teFirstName.text.toString()
            lastName = teLastName.text.toString()
            if (isValidate()) {
                val sharedPreferences = context?.getSharedPreferences("App", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putString("firstName", firstName)
                editor?.putString("lastName", lastName)
                editor?.apply()
                parentFragmentManager.beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.register_fragment_container, RegisterBirthday())
                    .addToBackStack(null).commit()
            } else {
                val teLastNameLayout = rootView.findViewById<TextInputLayout>(R.id.teLastNameLayout)
                val teFirstNameLayout =
                    rootView.findViewById<TextInputLayout>(R.id.teFirstNameLayout)
                if (lastName == "") {

                    teLastNameLayout.error = getString(R.string.required_input)
                } else {
                    teLastNameLayout.error = null
                }
                if (firstName == "") {

                    teFirstNameLayout.error = getString(R.string.required_input)
                } else {
                    teFirstNameLayout.error = null
                }
            }
        }

    }

    private fun isValidate(): Boolean {
        return firstName != "" && lastName != ""
    }

}


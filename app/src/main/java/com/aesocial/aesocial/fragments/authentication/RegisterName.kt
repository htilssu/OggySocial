package com.aesocial.aesocial.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.aesocial.aesocial.R


class RegisterName : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_register_name, container, false)
        initListener()
        return rootView
    }

    private fun initListener() {
        val btnNext = rootView.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.register_fragment_container, RegisterBirthday())
                .addToBackStack(null)
                .commit()
        }
    }

}
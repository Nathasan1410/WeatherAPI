package com.example.weatherapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login : Fragment(R.layout.fragment_login) {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = Firebase.auth

        val loginButt = view.findViewById<Button>(R.id.loginButton)
        loginButt.setOnClickListener {
            performLogin(view)
        }

        return view
    }

    private fun performLogin(view: View) {
        val email = view.findViewById<EditText>(R.id.usernameInput).text.toString()
        val password = view.findViewById<EditText>(R.id.passwordInput).text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen or perform other actions
                    findNavController().navigate(R.id.action_login_to_mainWeather)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
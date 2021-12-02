package com.example.paydayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChooseYourDomainActivity : AppCompatActivity() {

    private lateinit var naturalLogo : ImageView
    private lateinit var legalLogo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_your_domain_activity)

        naturalLogo = findViewById(R.id.naturalLogo)
        naturalLogo.bringToFront()

        legalLogo = findViewById(R.id.legalLogo)
        legalLogo.bringToFront()

        //naturalPersonButton = findViewById(R.id.buttonNormal)

        naturalLogo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, NormalPersonSignUpActivity::class.java))
        })

        legalLogo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LegalPersonSignUpActivity::class.java))
        })
    }
}
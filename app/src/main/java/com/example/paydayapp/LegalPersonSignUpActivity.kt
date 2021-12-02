package com.example.paydayapp

import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.paydayapp.classes.LegalUser
import com.example.paydayapp.classes.NaturalUser
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class LegalPersonSignUpActivity : AppCompatActivity() {

    private lateinit var usernameLogo : ImageView
    private lateinit var passwordLogo1 : ImageView
    private lateinit var passwordLogo2 : ImageView
    private lateinit var ring1 : ImageView
    private lateinit var ring2 : ImageView

    private lateinit var data1 : TextView
    private lateinit var data2 : TextView

    private lateinit var signUpButton : Button

    private lateinit var checkBox : CheckBox

    private lateinit var companyName : String
    private lateinit var CUI : String
    private lateinit var username : String
    private lateinit var password : String
    private lateinit var passwordConfirmation : String
    private lateinit var email : String

    private lateinit var companyNameLayout : TextInputLayout
    private lateinit var CUILayout : TextInputLayout
    private lateinit var usernameLayout : TextInputLayout
    private lateinit var passwordLayout : TextInputLayout
    private lateinit var passwordConfirmationLayout : TextInputLayout
    private lateinit var emailLayout : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.legal_person_sign_up_activity)

        usernameLogo = findViewById(R.id.usernameLogoLegal)
        passwordLogo1 = findViewById(R.id.passwordLogoLegal1)
        passwordLogo2 = findViewById(R.id.passwordLegalLogo2)
        data1 = findViewById(R.id.checkText1)
        data2 = findViewById(R.id.checkText2)
        ring1 = findViewById(R.id.ring1Legal)
        ring2 = findViewById(R.id.ring2Legal)

        checkBox = findViewById(R.id.checkBox)

        signUpButton = findViewById(R.id.legalSignUpButton)

        bringNodesToBack()

        handleSignUpButton()

    }

    private fun bringNodesToBack(){
        usernameLogo.bringToFront()
        passwordLogo1.bringToFront()
        passwordLogo2.bringToFront()
        data1.bringToFront()
        data2.bringToFront()
        ring1.bringToFront()
        ring2.bringToFront()
    }

    private fun handleSignUpButton(){
        signUpButton.setOnClickListener{
            companyName = findViewById<TextInputEditText>(R.id.companyNameText).text.toString()
            CUI = findViewById<TextInputEditText>(R.id.cuiText).text.toString()
            username = findViewById<TextInputEditText>(R.id.legalUsernameText).text.toString()
            password = findViewById<TextInputEditText>(R.id.legalPasswordText).text.toString()
            passwordConfirmation = findViewById<TextInputEditText>(R.id.legalPasswordConfimText).text.toString()
            email = findViewById<TextInputEditText>(R.id.emailLegalText).text.toString()

            companyNameLayout = findViewById(R.id.companyNameLayout)
            CUILayout = findViewById(R.id.cuiLayout)
            usernameLayout = findViewById(R.id.legalUsernameLayout)
            passwordLayout = findViewById(R.id.legalPasswordLayout)
            passwordConfirmationLayout = findViewById(R.id.legalPasswordConfirmLayout)
            emailLayout = findViewById(R.id.emailLegalLayout)

            val databaseLocation =
                "https://payday-5e8db-default-rtdb.europe-west1.firebasedatabase.app"
            val database = Firebase.database(databaseLocation)
            val refUsers = database.getReference("Users")

            refUsers.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var existsUsername = false
                    var existsEmail = false
                    var existsCUI = false
                    var takenCUI = false

                    if(snapshot.exists()) {
                        for (u in snapshot.children) {
                            if(u.hasChild("Company01")) {
                                for (t in u.children) {
                                    val cui = t.child("CUI").value.toString()
                                    if (cui == CUI) {
                                        Log.d("VictorNani", "AICI")
                                        existsCUI = true
                                        break
                                    }
                                }
                            } else {
                                val uname = u.child("username").value.toString()
                                val mail = u.child("email").value.toString()
                                val domain = u.child("domain").value.toString()
                                var currentCUI = "a"

                                if (domain == "L")
                                    currentCUI = u.child("cui").value.toString()

                                if (CUI == currentCUI)
                                    takenCUI = true

                                if (email == mail || uname == username) {
                                    if (email == mail)
                                        existsEmail = true
                                    if (username == uname)
                                        existsUsername = true

                                    break
                                }
                            }
                        }
                    }

                    if (CUI.isEmpty())
                        CUILayout.error = "This field must be completed"
                    else if(!existsCUI)
                        CUILayout.error = "This CUI does not exist"
                    else if (takenCUI)
                        CUILayout.error = "This company is registered"
                    else
                        CUILayout.error = ""

                    val a = filterCompanyName()
                    val b = filterUsername(existsUsername)
                    val c = filterPassword()
                    val d = filterDataCheckBox()
                    val e = filterEmail(existsEmail)
                    val f = !takenCUI
                    val isDataCorrect = a && b && c && d && e && existsCUI && f

                    Log.d("VictorNani","taken: $takenCUI")

                    if(isDataCorrect) {
                        val legalUserId = refUsers.push().key.toString()
                        val legalUser = LegalUser(companyName, CUI, username, password, "L")
                        refUsers.child(legalUserId).setValue(legalUser)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Debug", "Failed to read value.", error.toException())
                }
            })



//            val a = filterCompanyName()
//            val b = filterUsername(false)
//            val c = filterPassword()
//            val d = filterDataCheckBox()
//            val isDataCorrect = a && b && c && d && cuiExists
//
//            Log.d("VictorNani", a.toString())
//            Log.d("VictorNani", b.toString())
//            Log.d("VictorNani", c.toString())
//            Log.d("VictorNani", d.toString())
//            Log.d("VictorNani", isDataCorrect.toString())
//
//
//            Log.d("VictorNani", isDataCorrect.toString())
//
//            if(isDataCorrect) {
//                val legalUserId = ref.push().key.toString()
//                val legalUser = LegalUser(companyName, CUI, username, password, "L")
//                ref.child(legalUserId).setValue(legalUser)
//            }
        }
    }

    private fun filterCompanyName(): Boolean {
        if(companyName.isEmpty()) {
            companyNameLayout.error = "This field must be completed"
            return false
        }
        companyNameLayout.error = ""
        return true
    }

    private fun filterUsername(existsUsername: Boolean): Boolean {
        if(username.contains(" ")) {
            usernameLayout.error = "Whitespaces are not allowed"
            return false
        } else if(username.length > 15) {
            usernameLayout.error = "Length must not exceed 15 characters"
            return false
        } else if(username.isEmpty()) {
            usernameLayout.error = "This field must be completed"
            return false
        } else if(!containsAdmisibleCharacters(username)) {
            usernameLayout.error = "This field contains wrong characters"
            return false
        } else if (existsUsername) {
            usernameLayout.error = "This username is already taken"
            return false
        } else
            usernameLayout.error = ""

        return true
    }

    private fun filterEmail(existsEmail: Boolean): Boolean {
        if(email.contains(" ")) {
            emailLayout.error = "Whitespaces are not allowed"
            return false
        } else if(email.isEmpty()) {
            emailLayout.error = "This field must be completed"
            return false
        } else if(!validEmail(email)) {
            emailLayout.error = "The email is not correct"
            return false
        } else if(existsEmail) {
            emailLayout.error = "This email has been used"
            return false
        } else
            emailLayout.error = ""

        return true
    }

    private fun filterPassword(): Boolean {
        if(password.contains(" ")) {
            passwordLayout.error = "Whitespaces are not allowed"
            return false
        } else if(password.length < 6) {
            passwordLayout.error = "Password must have at least 6 characters"
            return false
        } else if(password.length > 20) {
            passwordLayout.error = "Length must not exceed 20 characters"
            return false
        } else if(password.isEmpty()) {
            passwordLayout.error = "This field must be completed"
            return false
        } else if(password != passwordConfirmation) {
            passwordLayout.error = "The passwords do not match"
            return false
        } else
            passwordLayout.error = ""

        return true
    }

    private fun filterDataCheckBox() : Boolean {
        if(!checkBox.isChecked) {
            data1.setTextColor(Color.parseColor("#B90E0A"))
            data2.setTextColor(Color.parseColor("#B90E0A"))
            return false
        } else {
            data1.setTextColor(Color.parseColor("#253A4B"))
            data2.setTextColor(Color.parseColor("#253A4B"))
        }

        return true
    }

    private fun containsAdmisibleCharacters(string: String): Boolean {
        return string.filter { it in 'A'..'Z' || it in 'a'..'z'  || it in '0'..'9' || it == '_' || it == '.' || it == '.'}.length == string.length
    }

    private fun validEmail(email: String): Boolean {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
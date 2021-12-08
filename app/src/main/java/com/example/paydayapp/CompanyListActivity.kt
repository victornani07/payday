package com.example.paydayapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class CompanyListActivity : AppCompatActivity() {
    private lateinit var username : String

    private lateinit var companyListUsername : TextView

    private lateinit var companyListLayout : ConstraintLayout

    private lateinit var companyListLinearLayout : LinearLayout

    private lateinit var companyListLogOutButton : Button

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_list_activity)

        var id = 1

        username = intent.getStringExtra("username").toString()
        Log.d("VictorNani", username)

        companyListLayout = findViewById(R.id.companyListLayout)
        companyListLayout.id = id++

        companyListUsername = TextView(this)

        companyListLinearLayout = findViewById(R.id.companyListLinearLayout)
        companyListLinearLayout.id = id

        companyListLogOutButton = findViewById(R.id.companyListLogOutButton)

        addUsernameView()

        addCompanyViews()

        handleCompanyListLogOutButton()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addUsernameView() {
        companyListLayout.addView(companyListUsername)

        companyListUsername.textSize = 20f
        companyListUsername.text = username
        companyListUsername.setTextColor(Color.parseColor("#253A4B"))
        companyListUsername.typeface = resources.getFont(R.font.segoe_ui_light)

        val params = companyListUsername.layoutParams as ConstraintLayout.LayoutParams
        params.startToStart = companyListLayout.id
        params.topToTop = companyListLayout.id
        params.bottomToBottom = companyListLayout.id
        params.endToEnd = companyListLayout.id
        params.verticalBias = 0.16F
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")

    private fun addCompanyViews() {
        val databaseLocation =
            "https://payday-5e8db-default-rtdb.europe-west1.firebasedatabase.app"
        val database = Firebase.database(databaseLocation)
        val ref = database.getReference("Users")

        ref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CommitPrefEdits")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    for (u in snapshot.children) {
                        val domain = u.child("domain").value.toString()
                        if(domain == "L") {
                            val companyName = u.child("companyName").value.toString()
                            val photoPath = "@drawable/" + companyName.lowercase()
                            val image = ImageView(this@CompanyListActivity)
                            image.setImageDrawable(ContextCompat.getDrawable(this@CompanyListActivity, resources.getIdentifier(photoPath, null,
                                packageName
                            )))
                            companyListLinearLayout.addView(image)
                            val params = image.layoutParams as LinearLayout.LayoutParams
                            params.topMargin = 25
                            params.bottomMargin = 25
                            params.leftMargin = 5
                            params.rightMargin = 5
                            image.setOnClickListener {
                                val intent = Intent(
                                    this@CompanyListActivity,
                                    ScanProductsActivity::class.java
                                )
                                intent.putExtra("companyName", companyName)
                                startActivity(intent)
                            }
                        }
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Victor", "Failed to read value.", error.toException())
            }
        })
    }

    private fun handleCompanyListLogOutButton() {
        companyListLogOutButton.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}
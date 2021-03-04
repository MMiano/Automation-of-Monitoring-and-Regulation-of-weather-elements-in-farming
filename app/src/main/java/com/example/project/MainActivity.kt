package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var TAG: String = "MainActivity"
    private lateinit var database: DatabaseReference
    var humidityReg: Int = 0
    var lightReg: Int = 0
    var tempReg: Int = 0
    var userReg: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var humidity: TextView = findViewById(R.id.humidity_value)
        var light : TextView = findViewById(R.id.light_value)
        var temp : TextView = findViewById(R.id.temperature_value)
        var ph: TextView = findViewById(R.id.soil_ph_value)
        var moisture : TextView = findViewById(R.id.soil_moisture_value)

        val button_light: Button = findViewById(R.id.button_light)
        val button_temp: Button = findViewById(R.id.button_temperature)
        val button_humidity: Button = findViewById(R.id.button_humidity)
        val button_moisture: Button = findViewById(R.id.button_moisture)
        val button_ph: Button = findViewById(R.id.button_ph)
        val user_regulate: Button = findViewById(R.id.user_regulate)

        button_humidity.setOnClickListener {
            ChangeRegulateValue()
        }

        button_temp.setOnClickListener {
            ChangeRegulateValue2 ()
        }
        button_light.setOnClickListener {
            ChangeRegulateValue3()
        }
        user_regulate.setOnClickListener {
            ChangeRegulateValue4()
        }

        database = Firebase.database.reference


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val data = dataSnapshot.getValue<ChipData>()

                humidity.text = data?.Humidity.toString()
                light.text = data?.LightIntensity.toString()
                temp.text = data?.Temperature.toString()
                ph.text = "0"
                moisture.text = "0"
                humidityReg = data?.humidityReg!!
                lightReg = data?.lightReg!!
                tempReg = data?.tempReg!!
                userReg = data?.userReg!!
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)

    }

    private fun ChangeRegulateValue(){
        if (humidityReg == 0){
            database.child("humidityReg").setValue(1)
        }else{
            database.child("humidityReg").setValue(0)
        }
    }
    private fun ChangeRegulateValue2(){
        if (tempReg == 0){
            database.child("tempReg").setValue(1)
        }else{
            database.child("tempReg").setValue(0)
        }
    }
    private fun ChangeRegulateValue3(){
        if ( lightReg == 0){
            database.child("lightReg").setValue(1)
        }else{
            database.child("lightReg").setValue(0)
        }
    }
    private fun ChangeRegulateValue4(){
        if ( userReg == 0){
            database.child("userReg").setValue(1)
        }else{
            database.child("userReg").setValue(0)
        }
    }
}


package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rayjo.model.NetflixDataModel
import com.example.rayjo.services.Services
import java.util.*

class FillFrom : AppCompatActivity(), View.OnClickListener {

    private lateinit var etName : EditText
    private lateinit var tvDate : TextView
    private lateinit var etEmail : EditText
    private lateinit var etPass : EditText
    private lateinit var etPins : EditText
    private lateinit var etProfileNumber : EditText
    private lateinit var btSubmit : Button

    companion object{
        val TYPE_EXTA = "type_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_fill_from)

        val service = Services()
        if (!service.checkCredential()){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        etName = findViewById(R.id.et_name)
        tvDate = findViewById(R.id.tv_date)
        etEmail = findViewById(R.id.et_email)
        etPass  = findViewById(R.id.et_pass)
        etPins = findViewById(R.id.et_pins)
        etProfileNumber = findViewById(R.id.et_profileNumber)
        btSubmit = findViewById(R.id.submit)

        val calendarInst = Calendar.getInstance()
        val dateNow = "${calendarInst.get(Calendar.DATE)} ${calendarInst.get(Calendar.MONTH)} ${calendarInst.get(Calendar.YEAR)}"
        tvDate.text = dateNow

        btSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit -> {
                if(validateAllInput()) {
                    val netflixData = NetflixDataModel()
                    netflixData.buyerName = etName.text.toString()
                    netflixData.date = tvDate.text.toString()
                    netflixData.email = etEmail.text.toString()
                    netflixData.pass = etPass.text.toString()
                    netflixData.pin = etPins.text.toString()
                    netflixData.profileNumber = etProfileNumber.text.toString().toInt()

                    val intent = Intent(this,PaymentMethodFill::class.java)
                    intent.putExtra(PaymentMethodFill.NetflixData_EXTRA,netflixData)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateAllInput():Boolean{
        var status = true
        if(etName.text.isEmpty()){
            etName.setError("Please Input Name!")
            status = false
        }

        if(etEmail.text.isEmpty()){
            etEmail.setError("Please Input Email!")
            status = false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()){
            etEmail.setError("Please Input Valid Email Address")
            status = false
        }

        if(etPass.text.isEmpty()){
            etPass.setError("Please Input Password!")
            status = false
        }

        if(etPins.text.isEmpty()){
            etPins.setError("Please Input Pins")
            status = false
        }

        if(etProfileNumber.text.isEmpty()){
            etProfileNumber.setError("Please Input Profile Number!")
            status = false
        }

        return status
    }
}











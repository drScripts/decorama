package com.example.rayjo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.rayjo.model.NetflixDataModel
import com.example.rayjo.model.SendEmailModel
import com.example.rayjo.model.YoutubeDataModel
import com.google.common.net.HttpHeaders.TE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.math.ceil

class PaymentMethodFill : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var etBuyerEmail : EditText
    private lateinit var etPaymentMethod : EditText
    private lateinit var btnPickImage : Button
    private lateinit var ivProof : ImageView
    private lateinit var etStock : EditText
    private lateinit var tvProfit : TextView
    private lateinit var btnSubmit : Button
    private lateinit var etBuyerName : EditText
    private lateinit var tvBuyerName : TextView
    private lateinit var tvPrice : TextView

    private val REQUEST_IMAGE_CAPTURE = 1
    var statusAddImage : Boolean = false
    lateinit var filePath : Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference
    lateinit var db : FirebaseFirestore

    lateinit var youtubeDataModel: YoutubeDataModel
    lateinit var dataId : String
    lateinit var imageId : String
    
    companion object{
        const val TYPE_EXTRA = "type_extra"
        const val NetflixData_EXTRA = "netflix_extra"
        const val TAG = "PaymentMethodFill"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_payment_method_fill)

        val netflixData : NetflixDataModel? = intent.getParcelableExtra(NetflixData_EXTRA)

        db = Firebase.firestore

        etBuyerEmail = findViewById(R.id.et_buyer_email)
        etPaymentMethod = findViewById(R.id.et_payment_method)
        btnPickImage = findViewById(R.id.btn_pick_image)
        ivProof = findViewById(R.id.iv_proof)
        etStock = findViewById(R.id.et_stock)
        tvProfit = findViewById(R.id.profit)
        btnSubmit = findViewById(R.id.btn_submit)
        etBuyerName = findViewById(R.id.et_buyer_name)
        tvBuyerName = findViewById(R.id.tv_buyer_name)
        tvPrice = findViewById(R.id.price)

        if(netflixData == null){
            etBuyerName.visibility = View.VISIBLE
            tvBuyerName.visibility = View.VISIBLE
            tvPrice.text = "10.000"
        }else{
            tvPrice.text = "37.500"
        }

        btnSubmit.setOnClickListener(this)
        btnPickImage.setOnClickListener(this)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        etStock.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_submit -> {
                if(validateInput()){
                   uploadData()
                }

            }
            R.id.btn_pick_image -> {
                 val intentGetImage = Intent(Intent.ACTION_PICK)
                intentGetImage.type = "image/*"
                startActivityForResult(intentGetImage,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!
            Glide.with(this)
                .load(filePath)
                .into(ivProof)
                statusAddImage = true
            Log.d(TAG, "onActivityResult: $filePath")
        }else{
            Toast.makeText(this,"Failed Get The Picture",Toast.LENGTH_LONG).show()
        }
    }

    private fun validateInput() : Boolean {
        var status = true

        if(etBuyerName.isVisible){
            if(etBuyerName.text.isEmpty()){
                etBuyerName.setError("Please Insert Buyer Name")
                status = false
            }
        }

        if(etBuyerEmail.text.isEmpty()){
            etBuyerEmail.setError("Please Insert Buyer Email")
            status = false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(etBuyerEmail.text).matches()){
            etBuyerEmail.setError("Please Insert Buyer Email A Valid Email")
            status = false
        }

        if(etPaymentMethod.text.isEmpty()){
            etPaymentMethod.setError("Please Insert Payment Method")
            status = false
        }

        if(filePath.toString().isEmpty() && statusAddImage){
            Toast.makeText(this,"Please Select Proof Transaction Image",Toast.LENGTH_LONG).show()
            status = false
        }

        if(etStock.text.isEmpty()){
            etStock.setError("Please Insert Stock")
            status = false
        }

        return status
    }


    private fun uploadData(){
        val netflixData : NetflixDataModel? = intent.getParcelableExtra(NetflixData_EXTRA)

        if(netflixData == null){
            youtubeDataModel = YoutubeDataModel()
        }

        // set upload Data
        val progressBar = ProgressDialog(this)
        progressBar.setTitle("Uploading...")
        progressBar.show()

        imageId = UUID.randomUUID().toString()

        val ref = storageReference.child("proofTransaction/$imageId")
        ref.putFile(filePath)
            .addOnSuccessListener {
                progressBar.dismiss()
                Toast.makeText(this,"Success Uploading Image",Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    ref.downloadUrl.addOnSuccessListener {url->
                        if(netflixData == null) {
                            val calendarInst = Calendar.getInstance()
                            val dateNow = "${calendarInst.get(Calendar.DATE)} ${calendarInst.get(Calendar.MONTH)} ${calendarInst.get(Calendar.YEAR)}"
                            youtubeDataModel.imageUri = url.toString()
                            youtubeDataModel.profit = tvProfit.text.toString()
                            youtubeDataModel.buyerEmail = etBuyerEmail.text.toString()
                            youtubeDataModel.paymentMethod = etPaymentMethod.text.toString()
                            youtubeDataModel.buyerName = etBuyerName.text.toString()
                            youtubeDataModel.date = dateNow
                            db.collection("youtube")
                                .add(youtubeDataModel)
                                .addOnSuccessListener{
                                    dataId = it.id
                                    Toast.makeText(this,"Success Upload All Data",Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,"Failed Upload Data",Toast.LENGTH_LONG).show()
                                }
                            val intent = Intent(this,ThankYou::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{
                            netflixData.imageUri = url.toString()
                            netflixData.profit = tvProfit.text.toString()
                            netflixData.buyerEmail = etBuyerEmail.text.toString()
                            netflixData.paymentMethod = etPaymentMethod.text.toString()
                            db.collection("netflix")
                                .add(netflixData)
                                .addOnSuccessListener{
                                    Toast.makeText(this,"Success Upload All Data",Toast.LENGTH_LONG).show()
                                    dataId = it.id
                                    val sendEmailModel = SendEmailModel(
                                        emailConstumer = netflixData.buyerEmail.toString(),
                                        emailData = netflixData.email.toString(),
                                        passwordData = netflixData.pass.toString(),
                                        profileData = netflixData.profileNumber.toString(),
                                        pinData = netflixData.pin.toString()
                                    )
                                    sendEmail(sendEmailModel)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,"Failed Upload Data",Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                }
            }
            .addOnFailureListener{
                progressBar.dismiss()
                Toast.makeText(this,"Failed Upload Image",Toast.LENGTH_LONG).show()
            }
            .addOnProgressListener {taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressBar.setMessage("Uploading ${ceil(progress)}%")
            }
    }

    private fun sendEmail(dataCostumer : SendEmailModel) {
        val progressBar = ProgressDialog(this)
        progressBar.setTitle("Sending Email...")
        progressBar.show()

        val body = FormBody.Builder()
            .add("email_costumer",dataCostumer.emailConstumer)
            .add("email_data",dataCostumer.emailData)
            .add("password",dataCostumer.passwordData)
            .add("profile",dataCostumer.profileData)
            .add("pin",dataCostumer.pinData)
            .build()
        Log.e(TAG, "sendEmail: ${dataCostumer.emailConstumer} , ${dataCostumer.emailData},${dataCostumer.passwordData} ${dataCostumer.profileData},",)
        val request = Request.Builder()
            .url("https://decoramastore.000webhostapp.com/api/send")
            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJuYXRoYW5hZWwudmRAZ21haWwuY29tIn0.WOnYhd0ECaX5dVW3HzCDAi-RuaBWy74NDEREsRkOgwc")
            .post(body = body)
            .build()
        Log.e(TAG, "onResponse: $imageId , $dataId")
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@PaymentMethodFill, "Failed Send API ${e.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "onResponse: ${response.code}")
                  if (response.code == 200) {
                    progressBar.dismiss()
                    val intent = Intent(this@PaymentMethodFill,ThankYou::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    progressBar.dismiss()
                      db.collection("netflix")
                              .document(dataId)
                              .delete()
                              .addOnCompleteListener {
                                  storageReference = storage.reference
                                  storageReference.child("proofTransaction/$imageId").delete()
                                          .addOnFailureListener {
                                              Toast.makeText(this@PaymentMethodFill,"Failed Send ${it.message}",Toast.LENGTH_LONG).show()
                                          }
                                          .addOnSuccessListener {
                                              Toast.makeText(this@PaymentMethodFill,"Failed Send Email",Toast.LENGTH_LONG).show()
                                          }
                              }
                }
            }
        })
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val netflixData : NetflixDataModel? = intent.getParcelableExtra(NetflixData_EXTRA)
        if(netflixData != null){
            val profit = 37500 - etStock.text.toString().toInt()
            tvProfit.text = profit.toString()
        }else{
            val profit = 10000 - etStock.text.toString().toInt()
            tvProfit.text = profit.toString()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}











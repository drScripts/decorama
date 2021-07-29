package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayjo.model.DetailDataPlaceHolder
import com.example.rayjo.model.ProfitModelData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import java.util.*
import kotlin.collections.ArrayList

class ProfitActivity : AppCompatActivity() {

    private lateinit var rvProfithMonth : RecyclerView
    private lateinit var tvTotalThisMonth : TextView
    private lateinit var tvTotalPreviousMonth : TextView
    private lateinit var buttonBack : Button
    private lateinit var db : FirebaseFirestore

    private lateinit var profitAdapter : ProfitAdapter
    private lateinit var profitList : ArrayList<ProfitModelData>

    private var totalThisMonth = 0
    private var totalPrevious = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_profit)

        rvProfithMonth = findViewById(R.id.rv_profit_month)
        tvTotalThisMonth = findViewById(R.id.total_this_month)
        tvTotalPreviousMonth = findViewById(R.id.total_previous_month)
        buttonBack = findViewById(R.id.profit_back)

        rvProfithMonth.layoutManager = LinearLayoutManager(this)
        rvProfithMonth.setHasFixedSize(true)

        profitList = arrayListOf()

        profitAdapter = ProfitAdapter(profitList)
        rvProfithMonth.adapter = profitAdapter

        buttonBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        getDataProfit()
    }

    private fun getDataProfit() {
        val calendarInst = Calendar.getInstance()
        db = FirebaseFirestore.getInstance()
        db.collection("youtube")
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this,"Failed Load Data Youtube From Database", Toast.LENGTH_LONG).show()
                }

                for(dc in value!!.documentChanges){
                    val profitModelData = ProfitModelData()
                    if(dc.type == DocumentChange.Type.ADDED){
                        val monthFromDate = dc.document.getField<String>("date").toString().split(" ").toTypedArray().get(1)
                        if(monthFromDate == calendarInst.get(Calendar.MONTH).toString()){
                            totalThisMonth += dc.document.getField<String>("profit").toString()
                                .toInt()
                            profitModelData.date = dc.document.getField<String>("date").toString()
                            profitModelData.profit = dc.document.getField<String>("profit").toString()
                            profitList.add(profitModelData)
                        }else{
                            totalPrevious += dc.document.getField<String>("profit").toString()
                                .toInt()
                        }
                    }
                }
                profitAdapter.notifyDataSetChanged()
                tvTotalPreviousMonth.text = totalPrevious.toString()
                tvTotalThisMonth.text = totalThisMonth.toString()
            }

        db.collection("netflix")
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this,"Failed Load Data Netflix From Database", Toast.LENGTH_LONG).show()
                }

                for(dc in value!!.documentChanges){
                    val profitModelData = ProfitModelData()
                    if(dc.type == DocumentChange.Type.ADDED){
                        val monthFromDate = dc.document.getField<String>("date").toString().split(" ").toTypedArray().get(1)
                        if(monthFromDate == calendarInst.get(Calendar.MONTH).toString()){
                            totalThisMonth += dc.document.getField<String>("profit").toString()
                                .toInt()
                            profitModelData.date = dc.document.getField<String>("date").toString()
                            profitModelData.profit = dc.document.getField<String>("profit").toString()
                            profitList.add(profitModelData)
                        }else{
                            totalPrevious += dc.document.getField<String>("profit").toString()
                                .toInt()
                        }
                    }
                }
                profitAdapter.notifyDataSetChanged()
                tvTotalPreviousMonth.text = totalPrevious.toString()
                tvTotalThisMonth.text = totalThisMonth.toString()
            }
    }
}
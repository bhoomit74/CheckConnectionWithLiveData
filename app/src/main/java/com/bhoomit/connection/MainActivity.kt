package com.bhoomit.connection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bhoomit.connection.util.CheckConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CheckConnection(this).observe(this, Observer{
            if(it){
                Toast.makeText(this,"Internet Connection Available",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"No Internet Connection!",Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.kotchingloginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_resistration.*

class Resistration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resistration)
        VerifyUser()
        gotofb.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,FbloginActivity::class.java))
        }

    }
    fun VerifyUser(){
        val uid= FirebaseAuth.getInstance().uid
        if(uid==null)
        {
            val intent= Intent(this,
                MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

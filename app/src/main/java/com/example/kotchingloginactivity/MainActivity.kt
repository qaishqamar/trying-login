package com.example.kotchingloginactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    val TAG="signin"
    var phonNo=""
    var verificationId=""
    var num=""
    lateinit var mCallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
      // num=phone_no_et.text.toString()
        phone_login_btn.setOnClickListener {
            if(phone_no_et.length() ==10){
                verify()
            }
            else
            {
               Toast.makeText(this,"enter no. properly" +
                       "you have enterd onl:${phone_no_et.length()} numbers ",Toast.LENGTH_SHORT).show()
            }

        }
        login_otp_btn.setOnClickListener {
            authenticate()
        }
        goto_fb.setOnClickListener {
            startActivity(Intent(this,FbloginActivity::class.java))
        }


    }
    private fun verificationCallback(){
        mCallbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
               Toast.makeText(this@MainActivity,"verification succesfull",Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@MainActivity,"verification failed",Toast.LENGTH_SHORT).show()
               // return verify()
            }

            override fun onCodeSent(verification: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verification, p1)
                verificationId=verification.toString()
                get_mobile_no_layput.visibility=View.GONE

               get_otp_layout.visibility=View.VISIBLE
            }

        }
    }
    private fun verify(){
        phonNo="+91"+phone_no_et.text.toString()

        verificationCallback()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonNo,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
            )


    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this,Resistration::class.java))
                    val user = task.result?.user
                    Log.d(TAG,"My $user" )
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }
    fun authenticate(){
        val verifiNo=get_otp_et.text.toString()
        val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(verificationId,verifiNo)
        signInWithPhoneAuthCredential(credential)
    }
    //


}

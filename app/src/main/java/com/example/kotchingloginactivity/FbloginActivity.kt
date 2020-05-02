package com.example.kotchingloginactivity

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fblogin.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.security.auth.callback.Callback

class FbloginActivity : AppCompatActivity() {
    companion object{
        val TAG="fblogin"
    }
    var firebaseAuth:FirebaseAuth?=null
    var callbackManager:CallbackManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fblogin)
        firebaseAuth=FirebaseAuth.getInstance()
        callbackManager= CallbackManager.Factory.create()
        login_button_fbacti.setReadPermissions("email")
        login_button_fbacti.setOnClickListener {
            signIn()
        }
    }
    fun signIn(){
        login_button_fbacti.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                 handleFacebookAccessToken(loginResult.accessToken);
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }

        })

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
            Log.d(TAG, "handleFacebookAccessToken:" + token)

            val credential = FacebookAuthProvider.getCredential(token.token)
            firebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = firebaseAuth!!.currentUser
                        Log.d(TAG, "signInWithCredential:${user!!.email}")
                        Log.d(TAG, "signInWithCredential:${user!!.phoneNumber}")
                        val intent=Intent(this, Resistration::class.java)
                        //intent.putExtra("user",)
                        startActivity(intent)
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException())
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode,data)
    }


}


package com.example.rayjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSingInClient:GoogleSignInClient
    private lateinit var googleSignInButton : Button

    private companion object{
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_login)

        googleSignInButton = findViewById(R.id.login_with_google)

        val clientID = "219392675473-qc1ualc422259erhuvn0nkjgevhhruks.apps.googleusercontent.com";
        // configure google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(clientID)
            .requestEmail() // get email
            .build()

        googleSingInClient = GoogleSignIn.getClient(this,gso)

        // init firebase auth
        auth = FirebaseAuth.getInstance()
        checkUserCredential()

        // Google Sign in, While Clicking Button
        googleSignInButton.setOnClickListener(this)
    }

    private fun checkUserCredential() {
        // check if user loged in or not
        val firebaseUser = auth.currentUser
        if(firebaseUser != null){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_with_google -> {
                // start sign in process
                Log.d(TAG,"onCreate: begin Google Sign in")
                val intent = googleSingInClient.signInIntent
                startActivityForResult(intent, RC_SIGN_IN)
                setResult(RC_SIGN_IN,intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // resullt from the intent
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google Sign In intent Result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign in Success then auth to firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseWithGoogleAccount(account);

            }catch (e:Exception){
                // failed google sign in
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseWithGoogleAccount: begin firebase auth")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {authResult->
                Log.d(TAG, "firebaseWithGoogleAccount: Loged In")

                // get logged in user
                val firebaseUser = auth.currentUser

                // get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG, "firebaseWithGoogleAccount: Email:${email}, Uid:${uid}")

                if(authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "firebaseWithGoogleAccount: Account Created.... ${email}")
                    Toast.makeText(this@LoginActivity,"New User Welcom!",Toast.LENGTH_SHORT).show()
                }else{
                    Log.d(TAG, "firebaseWithGoogleAccount: Account Loggedin.... ${email}")
                    Toast.makeText(this@LoginActivity,"Welcom Back!",Toast.LENGTH_SHORT).show()
                }

                // next activity
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener{e->
                Log.d(TAG, "firebaseWithGoogleAccount: Failed Login : ${e.message}")
                Toast.makeText(this@LoginActivity,"Failed Login!",Toast.LENGTH_SHORT).show()
            }
    }
}













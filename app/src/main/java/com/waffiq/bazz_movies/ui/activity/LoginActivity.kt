package com.waffiq.bazz_movies.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    showPassword()
    openTMDB()
    openTopTMDB()
  }

  private fun openTMDB() {
    binding.tvJoinTMDB.setOnClickListener {
      startActivity(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://www.themoviedb.org/signup")
        )
      )
    }
  }

  private fun showPassword() {
    binding.apply {
      btnEye.setOnClickListener {
        //if not clicked yet, then hide password
        if (edPass.transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
          //password visible hide it
          edPass.transformationMethod = PasswordTransformationMethod.getInstance()
          btnEye.setImageResource(R.drawable.ic_eye_off)
        } else {
          edPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
          btnEye.setImageResource(R.drawable.ic_eye)
        }
      }
    }
  }

  private fun openTopTMDB(){
    binding.tvGuest.setOnClickListener {
      startActivity(Intent(this,ListTopRatedMoviesActivity::class.java))
    }
  }
}
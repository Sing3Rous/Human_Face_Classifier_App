package com.example.humanfaceclassifier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_about.*
import org.w3c.dom.Text

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        about_app_source.movementMethod = LinkMovementMethod()
        about_app_source.isClickable = true
        about_nn_source.movementMethod = LinkMovementMethod()
        about_nn_source.isClickable = true
        about_contact_mail.movementMethod = LinkMovementMethod()
    }
}

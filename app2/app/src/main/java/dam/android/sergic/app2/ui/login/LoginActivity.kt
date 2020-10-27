package dam.android.sergic.app2.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import dam.android.sergic.app2.R
import dam.android.sergic.app2.tools.Tools

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        /*
            Avoid introducing credentials every time, storing in general
            preferences user and password.
         */
        val user = Tools.getUser(this)
        if(user.nickname!=null)
        {
            findViewById<EditText>(R.id.etNickname).setText(user.nickname)
            findViewById<EditText>(R.id.etPassword).setText(user.password)
            findViewById<Button>(R.id.btSignIn).performClick()
        }
    }
}
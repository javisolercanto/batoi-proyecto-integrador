package dam.android.sergic.compoundviews_di

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.AsyncTask
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import dam.android.sergic.app2.MainActivity
import dam.android.sergic.app2.R
import dam.android.sergic.app2.asyncTasks.AsyncFather
import dam.android.sergic.app2.asyncTasks.GetUser
import dam.android.sergic.app2.models.Deliveryman
import dam.android.sergic.app2.tools.Tools
import kotlinx.android.synthetic.main.login_compound_view.view.*
import java.util.*

class CompoundView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener, AsyncFather.AsyncResponse
{
    private var taskGetUser: GetUser? = null

    init
    {
        LayoutInflater.from(context).inflate(R.layout.login_compound_view,this,true)

        attrs?.let{
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.CompoundView, 0, 0)

            // Assigning to the correspondent views:
            ivImage.setImageResource(typedArray.getResourceId(R.styleable.CompoundView_image
                , R.drawable.logo))

            typedArray.recycle()
            btSignIn.setOnClickListener(this)
            pbLoad.visibility = View.GONE
        }

        // Depending if the dark mode is selected or not.
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                tvName.setTextColor(Color.WHITE)
                tvPassword.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                tvName.setTextColor(Color.BLACK)
                tvPassword.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onClick(v: View?)
    {
        val user : Deliveryman? = null

        if(etNickname.text.isNotEmpty() && etPassword.text.isNotEmpty())
        {
            cancel()
            taskGetUser = GetUser(context, pbLoad, this)
            taskGetUser!!.execute(etNickname.text.toString())

            etNickname.isEnabled = false
            etPassword.isEnabled = false
            btSignIn.isEnabled = false
        }
        else
            Toast.makeText(context, context.getString(R.string.empty_fields),Toast.LENGTH_LONG).show()
    }

    fun cancel()
    {
        if(taskGetUser!=null)
        {
            if (taskGetUser!!.getStatus() == AsyncTask.Status.PENDING
                    || taskGetUser!!.getStatus() == AsyncTask.Status.RUNNING)
            {
                taskGetUser!!.cancel(true)
                Toast.makeText(context, context.getString(R.string.interrupted), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun processFinish(output: ArrayList<*>?)
    {
        if (output != null)
        {
            if (output.size > 0 && output.get(0) != null)
            {
                val user = output.get(0) as Deliveryman
                if(user.password.equals(Tools.encryptThisSHA1(etPassword.text.toString())))
                {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    user.password = etPassword.text.toString()
                    Tools.saveUser(context, user)                                                           // Saving user into preferences file
                    Toast.makeText(context, context.getString(R.string.welcome) + " " + user.getName(), Toast.LENGTH_LONG).show()
                }
                else
                    Tools.showAlert(context,context.getString(R.string.error),context.getString(R.string.password_incorrect))
            }
            else
                Tools.showAlert(context,context.getString(R.string.error),context.getString(R.string.nickname_not_found))
        }
        else
            Tools.showAlert(context,context.getString(R.string.error),context.getString(R.string.network_not_available))

        etNickname.isEnabled = true
        etPassword.isEnabled = true
        btSignIn.isEnabled = true
    }
}
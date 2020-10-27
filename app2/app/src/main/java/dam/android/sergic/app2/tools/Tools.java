package dam.android.sergic.app2.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dam.android.sergic.app2.R;
import dam.android.sergic.app2.models.Deliveryman;

public class Tools
{
    public static AlertDialog dialog;

    public static void showAlert(Context context, String title, String message)
    {
        dialog = new AlertDialog.Builder(context).setTitle(title)
                .setPositiveButton(R.string.btnAccept, null)
                .setMessage(message).create();

        dialog.show();
    }

    public static void showConfirmation(Context context, String title, String message, AlertDialog.OnClickListener listener)
    {
        dialog = new AlertDialog.Builder(context).setTitle(title)
                .setPositiveButton(R.string.btnAccept, listener)
                .setNegativeButton(R.string.btnCancel, listener)
                .setMessage(message).create();
        dialog.show();
    }

    public static String encryptThisSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        String result;

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();

        digest.update(password.getBytes(StandardCharsets.UTF_8));

        result = String.format("%040x", new BigInteger(1, digest.digest()));
        return result;
    }

    public static void saveUser(Context context, Deliveryman user)
    {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        /*
         *  Only saving the user if there isn't any.
         */
        if (myPreferences.getString("nickname", null) == null)
        {
            // save data to file
            SharedPreferences.Editor editor = myPreferences.edit();

            editor.putInt("id",user.getId());
            editor.putString("name", user.getName());
            editor.putString("nickname",user.getNickname());
            editor.putString("password",user.getPassword());

            editor.apply();         // -> Asynchronous, avoid blocking the current thread.
        }
    }

    public static Deliveryman getUser(Context context)
    {
        Deliveryman user = new Deliveryman();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        user.setId(myPreferences.getInt("id", 0));
        user.setName(myPreferences.getString("name",null));
        user.setNickname(myPreferences.getString("nickname",null));
        user.setPassword(myPreferences.getString("password", null));

        return user;
    }

    public static void eraseUser(Context context)
    {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = myPreferences.edit().clear();
        editor.apply();
    }
}
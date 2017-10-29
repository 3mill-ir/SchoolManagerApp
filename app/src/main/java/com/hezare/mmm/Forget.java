package com.hezare.mmm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Toast;


import com.hezare.mmm.WebSide.Parser;
import com.hezare.mmm.WebSide.SendRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Forget extends AppCompatActivity {


    public enum DilogType {
        LOADING,
        ERROR
    }

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget);
        Utli.StrictMode();
        Utli.changeFont(getWindow().getDecorView());
        final TextInputLayout phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);
        findViewById(R.id.forgetbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneWrapper.getEditText().getText().toString().trim();
                if (phone.matches("")) {
                    phoneWrapper.setError("شماره موبایل نباید خالی باشد!");
                    return;
                }

                doForget(phone);

            }
        });


    }



    public void doForget(String Tell) {
        MakeDialog(DilogType.LOADING, null);
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SendRequest.SendPostForget(Tell);
        new SendRequest().setOnForgetCompleteListner(new SendRequest.OnForgetCompleteListner() {
            @Override
            public void OnForgetCompleteed(String response) {
                try {
                    JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                    String Text = oneObject.getString("Text");
                    String Key = oneObject.getString("Key");
                    if (Key.trim().matches("OK")) {
                        if (loading.isShowing()) {
                            loading.dismiss();
                        }
                        Toast.makeText(Forget.this, Text, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        MakeDialog(DilogType.ERROR, Text);

                    }

                } catch (JSONException e) {

                }
            }
        });
        new SendRequest().setOnForgetErrorListner(new SendRequest.OnForgetErrorListner() {
            @Override
            public void OnForgetErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                } else {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                }
            }
        });

    }

    private void MakeDialog(DilogType type, String Text) {
        if (type == DilogType.LOADING) {
            loading = new ProgressDialog(Forget.this);
            loading.setMessage("صبر کنید...");
            loading.setCancelable(false);
            loading.show();
        } else if (type == DilogType.ERROR) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            final AlertDialog alt = new AlertDialog.Builder(Forget.this).create();
            alt.setTitle(Html.fromHtml("<p style=\"color:red;\">خطا!</p>"));
            alt.setMessage(Text);
            alt.setButton(Dialog.BUTTON_POSITIVE, "تمام", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alt.dismiss();

                }
            });
            alt.show();
        }
    }
}


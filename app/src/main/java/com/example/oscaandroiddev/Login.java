package com.example.oscaandroiddev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {

    Button btnLogin;
    EditText etOSCA, etPassword;
    TextView tvForgotPassword;
    String password;
    String contactNumber;
    String myOSCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        adjustFontScale(getResources().getConfiguration());
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        etOSCA = findViewById(R.id.etOSCA);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        etOSCA.setText("0421-2000002");
        etPassword.setText("0421-2000002");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etOSCA.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals("")){
                    loginUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your OSCA ID or Password ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPasswordDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    private void openForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);

        final TextView btnClose4 = view.findViewById(R.id.btnClose4);
        final Button btnDone = view.findViewById(R.id.btnDone);
        final EditText etForgotOSCA = view.findViewById(R.id.etForgotOSCA);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_curved_edges);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

        btnClose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etForgotOSCA.getText().toString().trim().equals("")){
                    myOSCA = etForgotOSCA.getText().toString().trim();
                    fetchPassword();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your OSCA ID", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void fetchPassword() {
        String FORGOT_PASSWORD_URL = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/forgot_password.php";
        //String FORGOT_PASSWORD_URL = "http://192.168.1.6/arca_-master/mobile_app/forgot_password.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FORGOT_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.trim().equals("Account does not exist")) {
                                JSONObject details = new JSONObject(response);
                                password = details.getString("osca_id");
                                contactNumber = details.getString("contact_number");
                                int permissionCheck = ContextCompat.checkSelfPermission(Login.this, Manifest.permission.SEND_SMS);
                                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                                    SendSMSPassword();
                                } else {
                                    ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                                }
                            } else {
                                String oscaNotExist = getString(R.string.osca_not_exist);
                                Toast.makeText(getApplicationContext(), oscaNotExist, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oscaID", myOSCA);
                return params;
            }
        };
        Volley.newRequestQueue(getApplication()).add(stringRequest);
    }

    private void SendSMSPassword() {
        String message = "Your OSCA account password is : " + password;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(contactNumber, null, message, null, null);
        String checkInbox = getString(R.string.check_inbox);
        Toast.makeText(Login.this, checkInbox, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0:
                if(grantResults.length>=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SendSMSPassword();
                }
        }
    }

    private void loginUser() {
        String LOGIN_URL = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/login.php";
        //String LOGIN_URL = "http://192.168.1.6/arca_-master/mobile_app/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.trim().equals("Account does not exist")) {
                                JSONObject details = new JSONObject(response);
                                String oscaID = details.getString("osca_id");
                                String password = details.getString("password");
                                String picture = details.getString("picture");
                                String fullName = details.getString("full_name");
                                String birthDate = details.getString("bdate");
                                String sex = details.getString("sex");
                                String membershipDate = details.getString("memship_date");
                                String contactNumber = details.getString("contact_number");
                                String address = details.getString("address");
                                String nfc = details.getString("nfc_active");
                                String account = details.getString("account_enabled");

                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("EXTRA_OSCA_ID", oscaID);
                                intent.putExtra("EXTRA_PASSWORD", password);
                                intent.putExtra("EXTRA_PICTURE", picture);
                                intent.putExtra("EXTRA_FULL_NAME", fullName);
                                intent.putExtra("EXTRA_BIRTH_DATE", birthDate);
                                intent.putExtra("EXTRA_SEX", sex);
                                intent.putExtra("EXTRA_MEMBERSHIP_DATE", membershipDate);
                                intent.putExtra("EXTRA_CONTACT_NUMBER", contactNumber);
                                intent.putExtra("EXTRA_ADDRESS", address);
                                intent.putExtra("EXTRA_NFC", nfc);
                                intent.putExtra("EXTRA_ACCOUNT", account);
                                startActivity(intent);
                            } else {
                                String loginFailed = getString(R.string.login_failed);
                                Toast.makeText(getApplicationContext(), loginFailed, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
//                            Log.d("test", "test");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oscaID", etOSCA.getText().toString().trim());
                params.put("password", etPassword.getText().toString().trim());
                return params;
            }
        };
        Volley.newRequestQueue(getApplication()).add(stringRequest);
    }

    public void adjustFontScale(Configuration configuration)
    {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }
}
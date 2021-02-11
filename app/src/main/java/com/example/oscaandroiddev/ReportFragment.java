package com.example.oscaandroiddev;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ReportFragment extends Fragment {

    RadioGroup rgReport;
    LinearLayout llLost, llComplaints;
    Spinner spinCompany, spinBranch;
    EditText etRepAddNotes;
    TextView tvRepOSCA;
    ArrayList<String> companyNameList;
    ArrayList<String> branchNameList;
    Button btnReport;
    String company;
    String branch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);
        rgReport = view.findViewById(R.id.rgReport);
        llLost = view.findViewById(R.id.llLost);
        llComplaints = view.findViewById(R.id.llComplaints);
        spinCompany = view.findViewById(R.id.spinCompany);
        spinBranch = view.findViewById(R.id.spinBranch);
        tvRepOSCA = view.findViewById(R.id.tvRepOSCA);
        etRepAddNotes = view.findViewById(R.id.etRepAddNotes);
        btnReport = view.findViewById(R.id.btnReport);

        loadSpinnerCompanyData();

        rgReport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLost:
                        llLost.setVisibility(View.VISIBLE);
                        llComplaints.setVisibility(View.GONE);
                        FetchDetails fetchDetails = (FetchDetails) getActivity();
                        tvRepOSCA.setText(fetchDetails.getOscaID());
                        btnReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reportLost();
                            }
                        });
                        break;
                    case R.id.rbComplaints:
                        llComplaints.setVisibility(View.VISIBLE);
                        llLost.setVisibility(view.GONE);
                        btnReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!etRepAddNotes.getText().toString().trim().equals("")){
                                    reportComplaints();
                                } else {
                                    Toast.makeText(getContext(), "Do not leave additional notes field blank. Detailed explanation regarding the issue will help us resolve the problem.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        break;
                }
            }
        });

        spinCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company = spinCompany.getItemAtPosition(spinCompany.getSelectedItemPosition()).toString();
                loadSpinnerBranchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = spinBranch.getItemAtPosition(spinBranch.getSelectedItemPosition()).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void loadSpinnerCompanyData() {
        companyNameList = new ArrayList<>();
        branchNameList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < array.length(); i++) {
                JSONObject companyDetails = array.getJSONObject(i);
                String companyData = companyDetails.getString("company_name");
                if (!companyNameList.contains(companyData)) {
                    companyNameList.add(companyData);
                }
            }
            spinCompany.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.style_spinner, companyNameList));
            spinBranch.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.style_spinner, branchNameList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadSpinnerBranchData() {
        branchNameList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < array.length(); i++) {
                JSONObject companyDetails = array.getJSONObject(i);
                String branch = companyDetails.getString("branch");
                if (companyDetails.getString("company_name").equals(company)) {
                    branchNameList.add(branch);
                }
            }
            spinBranch.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.style_spinner, branchNameList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reportLost() {
        String LOST_URL = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/lost_report.php";
        //String LOST_URL = "http://192.168.1.6/arca_-master/mobile_app/lost_report.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View view = getLayoutInflater().inflate(R.layout.dialog_report_success, null);

                            final Button btnClose3 = view.findViewById(R.id.btnClose3);

                            builder.setView(view);
                            final AlertDialog dialog = builder.create();
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_curved_edges);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

                            btnClose3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
//                            Toast.makeText(getActivity().getApplicationContext(), "Report successfully sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            String reportFailed = getString(R.string.report_failed);
                            Toast.makeText(getActivity().getApplicationContext(), reportFailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                FetchDetails fetchDetails = (FetchDetails) getActivity();
                params.put("oscaID", fetchDetails.getOscaID());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(new Date());

                params.put("reportDate", date);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void reportComplaints() {
        String COMPLAINTS_URL = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/complaint_report.php";
        //String COMPLAINTS_URL = "http://192.168.1.6/arca_-master/mobile_app/complaint_report.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, COMPLAINTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respo: " + response);
                        if (response.trim().equals("success")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View view = getLayoutInflater().inflate(R.layout.dialog_report_success, null);

                            final Button btnClose3 = view.findViewById(R.id.btnClose3);

                            builder.setView(view);
                            final AlertDialog dialog = builder.create();
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_curved_edges);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

                            btnClose3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
//                            Toast.makeText(getActivity().getApplicationContext(), "Report successfully sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            String reportFailed = getString(R.string.report_failed);
                            Toast.makeText(getActivity().getApplicationContext(), reportFailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyName", company);
                params.put("branch", branch);

                FetchDetails fetchDetails = (FetchDetails) getActivity();
                params.put("oscaID", fetchDetails.getOscaID());

                params.put("desc", etRepAddNotes.getText().toString().trim());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(new Date());

                params.put("reportDate", date);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }
}

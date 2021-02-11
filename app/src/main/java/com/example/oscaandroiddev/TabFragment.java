package com.example.oscaandroiddev;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TabFragment extends Fragment implements TransactionHistoryAdapter.OnDetailsClickListener {

    RecyclerView rvTransaction;
    String historyURL;
    List<TransactionHistory> transactionList = new ArrayList<>();
    TransactionHistoryAdapter transactionHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_history, container, false);
        Bundle args = getArguments();
        if (args != null) {
            historyURL = args.getString("URL");
        }
        rvTransaction = view.findViewById(R.id.rvTransaction);

        transactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), transactionList, this);
        rvTransaction.setAdapter(transactionHistoryAdapter);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        loadTransaction();
        return view;
    }

    public void loadTransaction() {
        transactionList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, historyURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject transaction = array.getJSONObject(i);

                                //adding the product to product list
                                transactionList.add(new TransactionHistory(
                                        transaction.getString("trans_date"),
                                        transaction.getString("company_name"),
                                        transaction.getString("branch"),
                                        transaction.getString("business_type"),
                                        transaction.getString("desc"),
                                        transaction.getString("vat_exempt_price"),
                                        transaction.getString("discount_price"),
                                        transaction.getString("payable_price")
                                ));
                                Log.d(TAG, "Transaction List: " + transactionList);
                                transactionHistoryAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                FetchDetails fetchDetails = (FetchDetails) getActivity();
                if (fetchDetails != null) {
                    params.put("oscaID", fetchDetails.getOscaID());
                }
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onDetailsClick(TransactionHistory transactionHistory) {
        MyDetailsDialog myDetailsDialog = new MyDetailsDialog(transactionHistory.getTransDate(), transactionHistory.getCompanyName(), transactionHistory.getBranch(), transactionHistory.getBusiness_type(),
                transactionHistory.getDesc(), transactionHistory.getVatExemptPrice(), transactionHistory.getDisPrice(), transactionHistory.getPayPrice());
        myDetailsDialog.show(getActivity().getSupportFragmentManager(), "Details Dialog");
    }

    public void updateData() {
        loadTransaction();
        transactionHistoryAdapter.notifyDataSetChanged();
    }
}

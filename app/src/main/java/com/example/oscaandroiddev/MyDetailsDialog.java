package com.example.oscaandroiddev;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MyDetailsDialog extends AppCompatDialogFragment {

    private String transDate;
    private String companyName;
    private String branch;
    private String business_type;
    private String desc;
    private String vatExemptPrice;
    private String disPrice;
    private String payPrice;

    public MyDetailsDialog(String transDate, String companyName, String branch, String business_type, String desc, String vatExemptPrice, String disPrice, String payPrice) {
        this.transDate = transDate;
        this.companyName = companyName;
        this.branch = branch;
        this.business_type = business_type;
        this.desc = desc;
        this.vatExemptPrice = vatExemptPrice;
        this.disPrice = disPrice;
        this.payPrice = payPrice;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Details")
                .setMessage("Transaction Date: " + transDate + "\n" +
                        "Company Name: " + companyName + "\n" +
                        "Branch: " + branch + "\n" +
                        "Business Type: " + business_type + "\n" +
                        "Description: " + desc + "\n" +
                        "Vat Exempted Price: " + vatExemptPrice + "\n" +
                        "Discounted Price: " + disPrice + "\n" +
                        "Payable Price: " + payPrice + "\n")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}

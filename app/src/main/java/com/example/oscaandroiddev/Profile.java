package com.example.oscaandroiddev;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class Profile extends AppCompatActivity {

    TextView btnBack, btnSaveProfile, tvName, tvOSCA, tvSex, tvBirthDate, tvMemDate, tvConNumber, tvAddress, tvNFC, tvAccount;
    ImageView ivPicture;
    RelativeLayout profile;
//    LinearLayout llStatus;
    LinearLayout llAccount;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        btnBack = findViewById(R.id.btnBack);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        tvName = findViewById(R.id.tvName);
        tvOSCA = findViewById(R.id.tvOSCA);
        tvSex = findViewById(R.id.tvSex);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvMemDate = findViewById(R.id.tvMemDate);
        tvConNumber = findViewById(R.id.tvConNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvNFC = findViewById(R.id.tvNFC);
        tvAccount = findViewById(R.id.tvAccount);
        ivPicture = findViewById(R.id.ivPicture);
        profile = findViewById(R.id.profile);
//        llStatus = findViewById(R.id.lltStatus);
        llAccount = findViewById(R.id.llAccount);

        MemberDetails memberDetails = getIntent().getExtras().getParcelable("EXTRA_MEMBER DETAILS");

        Glide.with(getApplicationContext())
//                .load("https://my_server-ralphchri.pitunnel.com/arca_-master/resources/members/" + memberDetails.getPicture())
                .load("https://scitterminal-frederick2.pitunnel.com/arca_-master/resources/members/" + memberDetails.getPicture())
//                .placeholder(new ColorDrawable(Color.WHITE))
//                        .apply(RequestOptions.skipMemoryCacheOf(true))
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(ivPicture);

        tvName.setText(memberDetails.getFullName());
        tvOSCA.setText("OSCA ID: " + memberDetails.getOscaID());
        switch (memberDetails.getSex()) {
            case "0":
                tvSex.setText("Unknown");
                break;
            case "1":
                tvSex.setText("Male");
                break;
            case "2":
                tvSex.setText("Female");
                break;
            case "9":
                tvSex.setText("N/A");
        }
//        tvSex.setText(memberDetails.getSex());
        tvBirthDate.setText(memberDetails.getBirthDate());
        tvMemDate.setText(memberDetails.getMemDate());
        tvConNumber.setText(memberDetails.getConNumber());
        tvAddress.setText(memberDetails.getAddress());

        switch (memberDetails.getNFC()) {
            case "1":
                Drawable nfcActive = getApplicationContext().getResources().getDrawable(R.drawable.ic_enabled);
                tvNFC.setCompoundDrawablesWithIntrinsicBounds(nfcActive, null, null, null);
                for (Drawable drawable : tvNFC.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvNFC.getContext(), R.color.green), PorterDuff.Mode.SRC_IN));
                    }
                }
                tvNFC.setTextColor(getResources().getColor(R.color.green));
                tvNFC.setText("Active");
                break;
            case "0":
                Drawable nfcInactive = getApplicationContext().getResources().getDrawable(R.drawable.ic_disabled);
                tvNFC.setCompoundDrawablesWithIntrinsicBounds(nfcInactive, null, null, null);
                for (Drawable drawable : tvNFC.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvNFC.getContext(), R.color.red), PorterDuff.Mode.SRC_IN));
                    }
                }
                tvNFC.setTextColor(getResources().getColor(R.color.red));
                tvNFC.setText("Inactive");
                break;

        }

        switch (memberDetails.getAccount()) {
            case "1":
                Drawable accEnabled = getApplicationContext().getResources().getDrawable(R.drawable.ic_enabled);
                tvAccount.setCompoundDrawablesWithIntrinsicBounds(accEnabled, null, null, null);
                for (Drawable drawable : tvAccount.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvAccount.getContext(), R.color.green), PorterDuff.Mode.SRC_IN));
                    }
                }
                tvAccount.setTextColor(getResources().getColor(R.color.green));
                tvAccount.setText("Enabled");
                break;
            case "0":
                Drawable accDisabled = getApplicationContext().getResources().getDrawable(R.drawable.ic_disabled);
                tvAccount.setCompoundDrawablesWithIntrinsicBounds(accDisabled, null, null, null);
                for (Drawable drawable : tvAccount.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvAccount.getContext(), R.color.red), PorterDuff.Mode.SRC_IN));
                    }
                }
                tvAccount.setTextColor(getResources().getColor(R.color.red));
                tvAccount.setText("Disabled");
                break;

        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBack.setVisibility(View.INVISIBLE);
                btnSaveProfile.setVisibility(View.INVISIBLE);
//                llAccount.setVisibility(View.INVISIBLE);
//                llStatus.setVisibility(View.INVISIBLE);
                profile.setDrawingCacheEnabled(true);
                // this is the important code :)
                // Without it the view will have a dimension of 0,0 and the bitmap will be null

                profile.layout(0, 0, profile.getWidth(), profile.getHeight());

                profile.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(profile.getDrawingCache());
                view.setDrawingCacheEnabled(false); // clear drawing cache

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/req_images");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                File file = new File(myDir, fname);
//                Log.i(TAG, "" + file);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    b.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Toast.makeText(Profile.this, "Save Successfully!", Toast.LENGTH_LONG).show();
                    btnBack.setVisibility(View.VISIBLE);
                    btnSaveProfile.setVisibility(View.VISIBLE);
//                    llAccount.setVisibility(View.VISIBLE);
//                    llStatus.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
        });
    }
}
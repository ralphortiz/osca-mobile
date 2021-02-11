package com.example.oscaandroiddev;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static android.content.ContentValues.TAG;

public class AuthorizeFragment extends Fragment implements AuthorizationAdapter.OnEditClickListener, AuthorizationAdapter.OnDeleteClickListener {

    TextView btnGenQR, btnAddItem, btnClose, btnHelp;
    LinearLayout bottomSheet, emptyView;
    RecyclerView rvAuthorizeItems;
    FloatingActionButton btnAdd;
    BottomSheetBehavior bottomSheetBehavior;
    EditText etProdName, etProdQuantity, etAddNotes;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    View viewDim;
    Bitmap bitmap;
    OutputStream outputStream;
    AuthorizationAdapter authorizationAdapter;
    List<AuthorizeItem> authorizeItemList = new ArrayList<>();
    List<String> myQRList = new ArrayList<>();
    String authorization_token;
    String businessType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorize, container, false);
        btnAdd = view.findViewById(R.id.btnAdd);
        viewDim = view.findViewById(R.id.viewDim);
        bottomSheet = view.findViewById(R.id.bottomSheet);
        emptyView = view.findViewById(R.id.emptyView);
        rvAuthorizeItems = view.findViewById(R.id.rvAuthorizeItems);
        btnGenQR = view.findViewById(R.id.btnGenQR);
        btnHelp = view.findViewById(R.id.btnHelp);

        //bottomSheetDialog View
        etProdName = view.findViewById(R.id.etProdName);
        etProdQuantity = view.findViewById(R.id.etProdQuantity);
        etAddNotes = view.findViewById(R.id.etAddNotes);
        btnAddItem = view.findViewById(R.id.btnAddItem);
        btnClose = view.findViewById(R.id.btnClose);

        //RecyclerView and Authorization Adapter
        authorizationAdapter = new AuthorizationAdapter(getActivity(), authorizeItemList, this, this);
        rvAuthorizeItems.setAdapter(authorizationAdapter);
        rvAuthorizeItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        authorizationAdapter.notifyDataSetChanged();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    viewDim.setVisibility(View.GONE);
//                    btnAdd.setVisibility(View.VISIBLE);
                    etProdName.getText().clear();
                    etProdQuantity.getText().clear();
                    etAddNotes.getText().clear();

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                viewDim.setVisibility(View.VISIBLE);
//                btnAdd.setVisibility(View.INVISIBLE);
                viewDim.setAlpha(slideOffset);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //Generate QR
        btnGenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authorizeItemList.isEmpty()) {
                    String enterProductQuantity = getString(R.string.enter_product_and_quantity);
                    Toast.makeText(getContext(), enterProductQuantity, Toast.LENGTH_SHORT).show();
                } else {
                    generateQR();
                }
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelp();
            }
        });

        return view;
    }

    private void openHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_help, null);

        final Button btnOkay = view.findViewById(R.id.btnOkay);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_curved_edges);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void generateQR() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_qr, null);

        TextView btnClose2 = view.findViewById(R.id.btnClose2);
        final EditText etPWAuthorize = view.findViewById(R.id.etPWAuthorize);
//        final ImageView ivQR = view.findViewById(R.id.ivQR);
        List<String> businessTypeList = new ArrayList<>();

//        final Spinner spinBusinessType = view.findViewById(R.id.spinBusinessType);
        Button btnSave = view.findViewById(R.id.btnSave);
//        businessTypeList.add("");
//        businessTypeList.add("food");
//        businessTypeList.add("pharmacy");

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_curved_edges);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

//        spinBusinessType.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.style_spinner, businessTypeList));
//
//        spinBusinessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedBusiness = spinBusinessType.getItemAtPosition(spinBusinessType.getSelectedItemPosition()).toString();
//                if (selectedBusiness.equals("")) {
//                    businessType = "none";
//                } else if (selectedBusiness.equals("food")) {
//                    businessType = "food";
//                } else if (selectedBusiness.equals("pharmacy")) {
//                    businessType = "pharmacy";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), businessType, Toast.LENGTH_LONG).show();

                FetchDetails fetchDetails = (FetchDetails) getActivity();
                String password = fetchDetails.getOscaID();
                if(etPWAuthorize.getText().toString().trim().equals(password)) {

                    WindowManager manager = getActivity().getWindowManager();
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            + "0123456789"
                            + "abcdefghijklmnopqrstuvxyz";
                    int MAX_LENGTH = 16;
                    StringBuilder randomStringBuilder = new StringBuilder();
                    for (int i = 0; i < MAX_LENGTH; i++) {
                        int index
                                = (int)(AlphaNumericString.length()
                                * Math.random());
                        randomStringBuilder.append(AlphaNumericString
                                .charAt(index));
                    }

                    authorization_token = randomStringBuilder.toString();
                    addQRRequest();
                    QRGEncoder qrgEncoder = new QRGEncoder(authorization_token, null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
//                        ivQR.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }

                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String qrFilename = "AUTH_ITEM_" + timeStamp;
                            boolean save = new QRGSaver().save(savePath, qrFilename, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            String result = save ? "Image Saved" : "Image Not Saved";
//                        authorizeItemList.clear();
                            authorizeItemList.clear();
                            authorizationAdapter.notifyDataSetChanged();
                            emptyView.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }

                } else {
                    Toast.makeText(getActivity(), "Please enter a correct password", Toast.LENGTH_LONG).show();
                }

//                WindowManager manager = getActivity().getWindowManager();
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension * 3 / 4;
//
//                String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                        + "0123456789"
//                        + "abcdefghijklmnopqrstuvxyz";
//                int MAX_LENGTH = 16;
//                StringBuilder randomStringBuilder = new StringBuilder();
//                for (int i = 0; i < MAX_LENGTH; i++) {
//                    int index
//                            = (int)(AlphaNumericString.length()
//                            * Math.random());
//                    randomStringBuilder.append(AlphaNumericString
//                            .charAt(index));
//                }
//
//                authorization_token = randomStringBuilder.toString();
//                addQRRequest();
//                QRGEncoder qrgEncoder = new QRGEncoder(authorization_token, null, QRGContents.Type.TEXT, smallerDimension);
//                try {
//                    bitmap = qrgEncoder.encodeAsBitmap();
//                    ivQR.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    Log.v(TAG, e.toString());
//                }
//
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    try {
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                        String qrFilename = "AUTH_ITEM_" + timeStamp;
//                        boolean save = new QRGSaver().save(savePath, qrFilename, bitmap, QRGContents.ImageType.IMAGE_JPEG);
//                        String result = save ? "Image Saved" : "Image Not Saved";
////                        authorizeItemList.clear();
//                       authorizeItemList.clear();
//                       authorizationAdapter.notifyDataSetChanged();
//                       emptyView.setVisibility(View.VISIBLE);
//                       dialog.dismiss();
//                        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//                }
            }
        });

        btnClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addQRRequest() {
        String AUTHORIZE_URL = "https://scitterminal-frederick2.pitunnel.com/arca_-master/mobile_app/add_qr_request.php";
        //String AUTHORIZE_URL = "http://192.168.1.6/arca_-master/mobile_app/add_qr_request.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AUTHORIZE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respo: " + response);
                        if (response.trim().equals("success")) {
//                            String successSent = getString(R.string.success_sent);
//                            Toast.makeText(getActivity().getApplicationContext(), successSent, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                StringBuilder builder = new StringBuilder();
                for (AuthorizeItem authorizeItem : authorizeItemList) {
                    builder.append(authorizeItem);
                }
                String authorizeItems = builder.toString();
                params.put("desc", authorizeItems);
                params.put("token", authorization_token);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(new Date());
                //params.put("businessType", businessType);
                params.put("requestDate", date);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }


    private void openDialog() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        String addItem = getResources().getString(R.string.add_item);
        btnAddItem.setHint(addItem);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etProdName.getText().toString().equals("") && !etProdQuantity.getText().toString().equals("")) {

                    //Save Data to AuthorizeItem Object
                    authorizeItemList.add(new AuthorizeItem(etProdName.getText().toString().trim() , etProdQuantity.getText().toString().trim(), etAddNotes.getText().toString().trim()));
                    authorizationAdapter.notifyDataSetChanged();
                    etProdName.getText().clear();
                    etProdQuantity.getText().clear();
                    etAddNotes.getText().clear();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Log.d(TAG, "Authorize List: " + authorizeItemList);

                    //RecyclerView and FAB interaction
                    rvAuthorizeItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            switch (newState) {
                                case RecyclerView.SCROLL_STATE_IDLE:
                                    btnAdd.show();
                                    break;
                                default:
                                    btnAdd.hide();
                                    break;
                            }
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });

                    //Display Empty View
                    if (authorizationAdapter.getItemCount() == 0) {
                        rvAuthorizeItems.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        rvAuthorizeItems.setVisibility(View.VISIBLE);
                        btnGenQR.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(getContext(), "Enter product name and quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public void onEditClick(final AuthorizeItem authorizeItem) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        String updateItem = getResources().getString(R.string.update_item);
        btnAddItem.setHint(updateItem);
        etProdName.setText(authorizeItem.getProductName());
        etProdQuantity.setText(authorizeItem.getProductQuantity());
        etAddNotes.setText(authorizeItem.getAdditionalNotes());

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etProdName.getText().toString().equals("") && !etProdQuantity.getText().toString().equals("")) {

                    authorizeItem.setProductName(etProdName.getText().toString().trim());
                    authorizeItem.setProductQuantity(etProdQuantity.getText().toString().trim());
                    authorizeItem.setAdditionalNotes(etAddNotes.getText().toString().trim());
                    authorizationAdapter.notifyDataSetChanged();
                    etProdName.getText().clear();
                    etProdQuantity.getText().clear();
                    etAddNotes.getText().clear();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Log.d(TAG, "Authorize List: " + authorizeItemList);

                } else {
                    Toast.makeText(getContext(), "Enter product name and quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        authorizeItemList.remove(position);
        authorizationAdapter.notifyDataSetChanged();
        authorizationAdapter.notifyItemRemoved(position);
        authorizationAdapter.notifyItemRangeChanged(position, authorizeItemList.size());

        if (authorizationAdapter.getItemCount() == 0) {
            rvAuthorizeItems.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rvAuthorizeItems.setVisibility(View.VISIBLE);
            btnGenQR.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }

        Log.d(TAG, "Authorize List: " + authorizeItemList);
    }
}

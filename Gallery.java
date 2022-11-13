package com.example.android.billsplittingapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class Gallery extends BaseActivity {

    RecyclerView recyclerView;
    TextView noImages;
    FloatingActionButton addImage;
    public  static final int RequestPermissionCode  = 1 ;
    private Bitmap bitmap;
    String userID;

    ArrayList<PersonGallery> myGalleryList ;

    GalleryAdapter mAdapter;

    JSONObject jsonObject ;

    ProgressDialog progressDialog;
    String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        group=bundle.getString("group");

        //initToolbar("My Gallery");
        initUI();

        EnableRuntimePermission();


    }

    public void initUI(){

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_my_gallery);
        noImages = (TextView)findViewById(R.id.tv_no_gallery);
        addImage = (FloatingActionButton)findViewById(R.id.fab_add_gallery);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = new ContextThemeWrapper(Gallery.this, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(context, addImage);
                popup.getMenuInflater().inflate(R.menu.menu_my_gallery, popup.getMenu());
                popup.setGravity(Gravity.END);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.select_photo :

                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);
                                return true ;

                            case R.id.take_photo:

                                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 2);
                                return true;
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });

//        if(sharedPreferences.contains("user_id")){
//            userID = sharedPreferences.getString("user_id",userID);
//        }
        userID=sharedPreferences.getString("user_id","id");

        jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getMyGallery(jsonObject);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                onSelectFromGalleryResult(data);

            }
            if (requestCode == 2 && resultCode == RESULT_OK) {

                onCaptureImageResult(data);
            }
        } catch (Exception e) {

            Log.e("pic_excep",e+"");

        }

    }
    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getData();
        bitmap = (Bitmap) data.getExtras().get("data");

        ImageUploadToServerFunction(bitmap,Utils.UPLOAD_IMAGE_MY_GALLERY);

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        final Uri uri = data.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            // selectedFilePath = getPath(uri);
            Log.v("bit_map",bitmap+"");

            ImageUploadToServerFunction(bitmap,Utils.UPLOAD_IMAGE_MY_GALLERY);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void EnableRuntimePermission(){

        if(ContextCompat.checkSelfPermission(Gallery.this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Gallery.this, android.Manifest.permission.CAMERA))
            {

                Toast.makeText(Gallery.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(Gallery.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Gallery.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Gallery.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    public void getMyGallery(JSONObject jsonObject){
        final ArrayList<String> imagenames=new ArrayList<>();
        myGalleryList = new ArrayList<>();
        String url="http://phpstack-127383-542740.cloudwaysapps.com/api/v1/getgallaryimages";
        RequestQueue queue1 = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("my_gallery_resp",response+"");
                        Utils.dissmisProgress();
                        Log.e("111111111111","?????????????????????????????????/");
                        final JSONObject jsonObject;
                        final JSONArray jsonArray ;
                        try {
                            jsonObject = response.getJSONObject("data");
                            jsonArray    = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<PersonGallery>() {}.getType();
                                    PersonGallery myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                    myGalleryList.add(myQuestions);
                                    Log.v("listSize",myGalleryList.size()+"");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if(myGalleryList.size()!=0&&!jsonArray.equals("[]")){
                                    mAdapter = new GalleryAdapter(Gallery.this,myGalleryList,"myGallery",userID);
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    noImages.setVisibility(View.GONE);
                                }else {
                                    noImages.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener) {


        };
        queue1.add(jsonObjReq1);
        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this,"Loading....");
        Utils.showProgress();
        JSONObject js=new JSONObject();
        try {
            js.put("user_id", sharedPreferences.getString("user_id", "id"));
        }catch (Exception e){
            Log.e("exception","msggggggggggggggggggggg");
        }
        for(int j=0;j<imagenames.size();j++){
            String url1="http://phpstack-127383-542740.cloudwaysapps.com/public/uploads/gallary/"+imagenames.get(j);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url1, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.dissmisProgress();
                            Log.e("2222222222222222222222","+++++++++++++++++++++++++++++++++++++");
                            final JSONObject jsonObject;
                            final JSONArray jsonArray ;
                            try {
                                jsonObject = response.getJSONObject("data");
                                jsonArray    = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<PersonGallery>() {}.getType();
                                        PersonGallery myQuestions = gson.fromJson(jsonArray.get(i).toString(), type);
                                        myGalleryList.add(myQuestions);
                                        Log.v("listSize",myGalleryList.size()+"");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

//                                if(myGalleryList.size()!=0&&!jsonArray.equals("[]")){
//                                    mAdapter = new GalleryAdapter(Gallery.this,myGalleryList,"myGallery",userID);
//                                    recyclerView.setAdapter(mAdapter);
//                                    mAdapter.notifyDataSetChanged();
//                                    noImages.setVisibility(View.GONE);
//                                }else {
//                                    noImages.setVisibility(View.VISIBLE);
//                                }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, errorListener) {


            };
            queue.add(jsonObjReq);
        }

        if(myGalleryList.size()!=0){
            mAdapter = new GalleryAdapter(Gallery.this,myGalleryList,"myGallery",userID);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            noImages.setVisibility(View.GONE);
        }else {
            noImages.setVisibility(View.VISIBLE);
        }

    }

    public void ImageUploadToServerFunction(Bitmap bitmap,final String URL) {

        Log.v("Image_Url",URL);

        ByteArrayOutputStream byteArrayOutputStreamObject;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        Log.v("byte_array",byteArrayVar+"");

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        Log.v("convert_img",ConvertImage);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog   = ProgressDialog.show(Gallery.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", sharedPreferences.getString("user_id","id"));
                    jsonObject.put("image", ConvertImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pic_input",jsonObject+"");
                Log.e("url",URL);


                String FinalData = imageProcessClass.ImageHttpRequest(URL, jsonObject.toString());

                return FinalData;
            }

            @Override
            protected void onPostExecute(String res) {
                super.onPostExecute(res);

                Log.e("pic_response",res);

                JSONObject response = null;
                String mssg;
                try {
                    response = new JSONObject(res);
                    mssg=response.getString("message");
                    Toast.makeText(Gallery.this, mssg+"", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getMyGallery(jsonObject);

                progressDialog.dismiss();
            }
        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, String s) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setRequestProperty("Content-Type", "application/json");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(s);
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }

    }
}


package com.vatos.visio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FloatingActionButton images, camera;
    ImageView img;
    File saveFolder, encodeFile;
    String UPLOAD_URL = "http://www.visio.16mb.com/Visio/include/call.php";

    private final static int CAMERA_RQ = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("com.vatos.visio", MODE_PRIVATE);
        editor = pref.edit();

        if (pref.getString("user_name", null) == null) {
//            startActivity(new Intent(MainActivity.this, LoginSignUpActivity.class));
//            finish();
        }

        saveFolder = new File(Environment.getExternalStorageDirectory(), "Visio");
        encodeFile = new File(saveFolder, "encode.txt");
        if (saveFolder.mkdir()){
            System.out.println("Created");
        }else {
            System.out.println("Not Created");
        }

        img = (ImageView) findViewById(R.id.img);
        rv = (RecyclerView) findViewById(R.id.rv);
        new FetchFiles().execute();
        images = (FloatingActionButton) findViewById(R.id.image);
        camera = (FloatingActionButton) findViewById(R.id.camera);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera.getVisibility() == View.GONE) {
                    camera.setVisibility(View.VISIBLE);
                    images.setVisibility(View.VISIBLE);
                }else{
                    camera.setVisibility(View.GONE);
                    images.setVisibility(View.GONE);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rc = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    new MaterialCamera(MainActivity.this)
                            .stillShot()
                            .saveDir(saveFolder)
                            .start(CAMERA_RQ);
                }else {
                    requestCameraPermission();
                }


            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rdc = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (rdc == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(MainActivity.this, "Select a picture");
                }else{
                    requestStoragePermission();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
                String s = data.getDataString();
                String loc[] = s.split("/");
                int n = loc.length;
                String imgLoc = Environment.getExternalStorageDirectory()+"/"+loc[n-2]+"/"+loc[n-1];
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bm = BitmapFactory.decodeFile(imgLoc);
                bm = Bitmap.createScaledBitmap(bm, 100, 100, true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                uploadImage(bm);
            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == ImagePicker.PICK_IMAGE_REQUEST_CODE){
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            String s = data.getDataString();
            System.out.println(data.getDataString());
            String loc[] = s.split("/");
            int n = loc.length;
            String imgLoc = Environment.getExternalStorageDirectory()+"/"+loc[n-2]+"/"+loc[n-1];
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            uploadImage(bitmap);
        }
    }


    private void requestStoragePermission(){
        Log.w("Visio", "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, permissions, 4);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        4);
            }
        };
    }

    private void requestCameraPermission() {
        Log.w("Visio", "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, 2);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        2);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != 2) {
            Log.d("Visio", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (requestCode != 4) {
            Log.d("Visio", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Visio", "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            ImagePicker.pickImage(MainActivity.this);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Visio", "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            new MaterialCamera(MainActivity.this)
                    .stillShot()
                    .saveDir(saveFolder)
                    .start(CAMERA_RQ);
            return;
        }

        Log.e("Visio", "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Visio")
                .setMessage("No Camera Permission")
                .setPositiveButton("OK", listener)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void uploadImage(final Bitmap bitmap) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                        callAPI(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = getStringImage(bitmap);

                Map<String,String> params = new Hashtable<String, String>();

                params.put("photo", image);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    class FetchFiles extends AsyncTask<Void, Void, Void>{

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://www.visio.16mb.com/Visio/include/files.php?user_name=da9ish");
//                urlConnection = (HttpURLConnection) url.openConnection();

//                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

//                String line;
//                StringBuffer json = new StringBuffer();
//                while ((line = reader.readLine()) != null) {
//                    json.append(line);
//                }
//                System.out.println(json);


                HashMap<String, String> map1 = new HashMap<>();
                map1.put("name", "Danish Shah");
                map1.put("file_name", "Top 10 things blah blah...");
                map1.put("date_created", "2 days ago");
                list.add(map1);

                HashMap<String, String> map2 = new HashMap<>();
                map2.put("name", "Danish Shah");
                map2.put("file_name", "How to become a millionaire");
                map2.put("date_created", "1 week ago");
                list.add(map2);

                HashMap<String, String> map3 = new HashMap<>();
                map3.put("name", "Danish Shah");
                map3.put("file_name", "LetterHead");
                map3.put("date_created", "3 weeks ago");
                list.add(map3);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rv.setAdapter(new FilesAdapter(MainActivity.this, list));
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

    class FilesAdapter extends RecyclerView.Adapter<FilesViewHolder>{

        ArrayList<HashMap<String, String>> list;
        Context cxt;

        public FilesAdapter(Context cxt, ArrayList<HashMap<String, String>> list) {
            this.cxt = cxt;
            this.list = list;
            System.out.println(list.size());
        }

        @Override
        public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(cxt).inflate(R.layout.item_files, parent, false);

            return new FilesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(FilesViewHolder holder, int position) {
            HashMap<String, String> map = list.get(position);

            holder.name.setText(map.get("file_name"));
            holder.owner.setText(map.get("name"));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class FilesViewHolder extends RecyclerView.ViewHolder{

        TextView name, owner;

        public FilesViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.file_name);
            owner = (TextView) itemView.findViewById(R.id.owner);
        }
    }

    public void callAPI(String s){
        String url = "https://api.ocr.space/parse/imageurl?apikey=10911dfda588957&url="+s+"&language=eng&isOverlayRequired=true";
    }

}

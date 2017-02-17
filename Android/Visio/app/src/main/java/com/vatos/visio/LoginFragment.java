package com.vatos.visio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bilal on 15-02-2017.
 */

public class LoginFragment extends Fragment {

    EditText user_name, password;
    Button login;
    RequestQueue queue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        pref = getContext().getSharedPreferences("com.vatos.visio", Context.MODE_PRIVATE);
        editor = pref.edit();
        user_name = (EditText) v.findViewById(R.id.user_name);
        password = (EditText) v.findViewById(R.id.pass);
        login = (Button) v.findViewById(R.id.btn_login);

        queue = Volley.newRequestQueue(getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLogin();
            }
        });

        return v;
    }

    public void verifyLogin(){
        String u_name = user_name.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(u_name != "" && pass != ""){
            validate(u_name, pass);
        }
    }

    public void validate(final String u_name, final String pass){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.visio.16mb.com/Visio/include/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Verified")){
                    editor.putString("user_name", u_name).commit();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }else{
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_name",u_name);
                params.put("password",pass);
                return params;
            }


        };

        queue.add(stringRequest);
    }
}

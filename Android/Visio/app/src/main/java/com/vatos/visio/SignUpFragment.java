package com.vatos.visio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignUpFragment extends Fragment {

    EditText user_name, name, email, password, con_password;
    TextInputLayout pass_l, con_p_l;
    Button sign_up;
    RequestQueue queue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        pref = getContext().getSharedPreferences("com.vatos.visio", Context.MODE_PRIVATE);
        editor = pref.edit();
        user_name = (EditText) v.findViewById(R.id.user_name);
        name = (EditText) v.findViewById(R.id.name);
        email = (EditText) v.findViewById(R.id.email);
        password = (EditText) v.findViewById(R.id.pass);
        con_password = (EditText) v.findViewById(R.id.con_pass);
        sign_up = (Button) v.findViewById(R.id.btn_sign_up);

        queue = Volley.newRequestQueue(getContext());

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignUp();
            }
        });

        return v;
    }

    public void verifySignUp() {
        String u_name = user_name.getText().toString().trim();
        String name_txt = name.getText().toString().trim();
        String email_txt = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String con_pass = con_password.getText().toString().trim();

        if (u_name != "" && name_txt != "" && email_txt != "" && pass != "" && con_pass != "") {
            if (!pass.equals(con_pass)) {
                Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_SHORT).show();
            }else {
                validate(u_name, name_txt, email_txt, pass, con_pass);
            }
        }
    }

    public void validate(final String u_name, final String name, final String email, final String pass, final String con_pass) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://visio.16mb.com/Visio/include/sign_up.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Confirmed")) {
                    editor.putString("user_name", u_name).commit();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", u_name);
                params.put("name", name);
                params.put("email", email);
                params.put("password", pass);
                params.put("con_password", con_pass);
                return params;
            }

        };

        queue.add(stringRequest);
    }
}

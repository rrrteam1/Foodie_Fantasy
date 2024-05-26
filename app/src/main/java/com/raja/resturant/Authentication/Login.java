package com.raja.resturant.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.UserAuthModel;
import com.raja.resturant.R;

public class Login extends AppCompatActivity implements View.OnClickListener, MyFunctionBox.OnSignInListener {
    private TextInputEditText et_login_mail, et_login_pass;
    private TextView tv_goto_reg;
    private ConstraintLayout cl_login_btn;
    private MyFunctionBox myFunctionBox;
    private Intent oldDataGetIntent;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        et_login_mail = findViewById(R.id.login_et_mail_id);
        et_login_pass = findViewById(R.id.login_et_password_id);
        tv_goto_reg = findViewById(R.id.tv_goto_reg_txt_id);
        cl_login_btn = findViewById(R.id.login_cl_login_btn_id);

        oldDataGetIntent = getIntent();
        Bundle bundle = oldDataGetIntent.getExtras();
        if (bundle != null){
            String m = bundle.getString("mail");
            String p = bundle.getString("pass");
            et_login_mail.setText(m);
            et_login_pass.setText(p);
        }

        myFunctionBox = new MyFunctionBox(Login.this);


        tv_goto_reg.setOnClickListener(this);
        cl_login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_goto_reg_txt_id){
            Intent intent = new Intent(Login.this, Register.class);
            Bundle bundle = new Bundle();
            bundle.putString("mail", et_login_mail.getText().toString().trim());
            bundle.putString("pass", et_login_pass.getText().toString().trim());
            intent.putExtras(bundle);
            startActivity(intent);
        }else if (v.getId() == R.id.login_cl_login_btn_id){
            mMakeReadyForLogIn();
        }
    }
    private void mMakeReadyForLogIn() {
        String s_user_mail, s_user_pass;
        s_user_mail = et_login_mail.getText().toString().trim();
        s_user_pass = et_login_pass.getText().toString().trim();
        myFunctionBox.mFirebaseLogIn(s_user_mail, s_user_pass);

    }

    @Override
//    public void onSignInSuccess(FirebaseUser user) {
    public void onSignInSuccess(UserAuthModel model) {
        Bundle bundle = new Bundle();
        bundle.putString("mail", model.getUser_mail());
        bundle.putString("name", model.getUser_name());
        myFunctionBox.mGoToActivity(new MainActivity(), bundle);
//        myFunctionBox.mNotice("Welcome!", "Welcome Back! Proceed to Home.", new MainActivity(), bundle);
    }
}
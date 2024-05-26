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
import com.google.firebase.auth.FirebaseUser;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.Model.UserAuthModel;
import com.raja.resturant.R;

public class Register extends AppCompatActivity implements View.OnClickListener, MyFunctionBox.OnSignUpListener{
    private TextInputEditText et_reg_user, et_reg_mail, et_reg_pass;
    private TextView tv_goto_login;
    private ConstraintLayout cl_reg_btn;
    private Intent oldDataGetIntent;
    private MyFunctionBox myFunctionBox;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_reg_user = findViewById(R.id.reg_et_name_id);
        et_reg_mail = findViewById(R.id.reg_et_mail_id);
        et_reg_pass = findViewById(R.id.reg_et_password_id);
        tv_goto_login = findViewById(R.id.tv_goto_login_txt_id);
        cl_reg_btn = findViewById(R.id.reg_cl_register_btn_id);


        oldDataGetIntent = getIntent();
        Bundle bundle = oldDataGetIntent.getExtras();
        if (bundle != null){
            String m = bundle.getString("mail");
            String p = bundle.getString("pass");
            et_reg_mail.setText(m);
            et_reg_pass.setText(p);
        }




        myFunctionBox = new MyFunctionBox(Register.this);
        tv_goto_login.setOnClickListener(this);
        cl_reg_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_goto_login_txt_id){
            Intent intent = new Intent(Register.this, Login.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", et_reg_user.getText().toString().trim());
            bundle.putString("mail", et_reg_mail.getText().toString().trim());
            bundle.putString("pass", et_reg_pass.getText().toString().trim());
            intent.putExtras(bundle);
            startActivity(intent);
        }else if (v.getId() == R.id.reg_cl_register_btn_id){
            mMakeReadyForRegister();
        }
    }

    private void mMakeReadyForRegister() {
        String s_user_name, s_user_mail, s_user_pass;
        s_user_name = et_reg_user.getText().toString().trim();
        s_user_mail = et_reg_mail.getText().toString().trim();
        s_user_pass = et_reg_pass.getText().toString().trim();
        myFunctionBox.mFirebaseRegistration(s_user_name, s_user_mail, s_user_pass);

    }

    @Override
//    public void onSignUpSuccess(FirebaseUser user) {
    public void onSignUpSuccess(UserAuthModel model) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mail", model.getUser_mail());
        bundle.putString("name", model.getUser_name());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void mOnEmailIsAlreadyExist(UserAuthModel userAuthModel, String user_pass) {
        Bundle bundle = new Bundle();
        bundle.putString("name", userAuthModel.getUser_name());
        bundle.putString("mail", userAuthModel.getUser_mail());
        bundle.putString("pass", user_pass);
        myFunctionBox.mGoToActivity(new MainActivity(), bundle);
//        myFunctionBox.mNotice("Worning", "This user is already exist. You may have to go for login...", new Login(), bundle);
    }



}
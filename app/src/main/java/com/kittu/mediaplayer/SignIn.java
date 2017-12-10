package com.kittu.mediaplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;





public class SignIn extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {


    private LinearLayout profile;
    private Button btnLogOut;
    private SignInButton btnSignIn;
    private TextView tvName;
    private TextView tvEmail;
    private ImageView pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        profile=(LinearLayout)findViewById(R.id.profile);
        btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnSignIn=(SignInButton)findViewById(R.id.btnSignIn);

        tvName=(TextView)findViewById(R.id.tvName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        pic=(ImageView)findViewById(R.id.pic);
        btnSignIn.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        profile.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSignIn:
                signIn();
                break;
            case R.id.btnLogOut:
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
                break;


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent i=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i,REQ_CODE);

    }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }
    private void handleResult(GoogleSignInResult result)
    {

        if(result.isSuccess())
        {

            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            Uri img_url=account.getPhotoUrl();
            tvName.setText(name);
            tvEmail.setText(email);
            Glide.with(this).load(img_url).override(300,200).into(pic);
            updateUI(true);
        }
        else
        {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogIn){

        if(isLogIn)
        {
            profile.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);


        }
        else {
            profile.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {

            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        }
    }
}

package com.example.android.googlesignindemo;

import android.content.Intent;
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

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout prof_sec;
    private ImageView profile_pic;
    private TextView name, email;
    private Button sign_out;
    private SignInButton sign_in;
    private GoogleApiClient googleApiClient;
    private static int REQ_CODE=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof_sec = (LinearLayout) findViewById(R.id.prof_section);
        profile_pic = (ImageView) findViewById(R.id.prof_pic);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        sign_out = (Button) findViewById(R.id.signOutBtn);
        sign_in = (SignInButton) findViewById(R.id.SigninBtn);
        sign_out.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        prof_sec.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SigninBtn:
                signIn();
                break;
            case R.id.signOutBtn:
                signOut();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private  void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void signOut(){
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    updateUI(false);
                }
            });
    }
    private void handleResultt(GoogleSignInResult result){
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String usrName = account.getDisplayName();
                String usrEmail = account.getEmail();
                String img_url = account.getPhotoUrl().toString();
                name.setText(usrName);
                email.setText(usrEmail);
                Glide.with(this).load(img_url).into(profile_pic);
                updateUI(true);

            }
            else{
                updateUI(false);
            }
    }

    private void updateUI(boolean isLogin){
            if(isLogin){
                prof_sec.setVisibility(View.VISIBLE);
                sign_in.setVisibility(View.GONE);
            }
            else {
                sign_in.setVisibility(View.VISIBLE);
                prof_sec.setVisibility(View.GONE);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResultt(result);
        }
    }
}

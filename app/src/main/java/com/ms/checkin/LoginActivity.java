package com.ms.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkFieldsIfValid()) {
                    login();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkFieldsIfValid() {
        if (mEtUsername.getText().toString().trim().equals(""))
            return false;
        if (mEtPassword.getText().toString().trim().equals(""))
            return false;
        return true;
    }

    private void login() {
        mPbLoading.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(getFromEditText(mEtUsername, true), getFromEditText(mEtPassword, true), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                mPbLoading.setVisibility(View.INVISIBLE);
                if (parseUser != null) {
                    User user = new User();
                    user.setId(parseUser.getObjectId());
                    user.setUserName(parseUser.getString(Constants.PARSE_USERNAME_COLUMN));
                    user.setCheckedIn(parseUser.getBoolean(Constants.PARSE_CHECKED_IN_COLUMN));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(Constants.USER_TAG, user);
                    startActivity(intent);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_USER_TABLE);
        query.whereEqualTo(Constants.PARSE_USERNAME_COLUMN, getFromEditText(mEtUsername, true));
        query.whereEqualTo(Constants.PARSE_PASSWORD_COLUMN, getFromEditText(mEtPassword, true));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userObject, ParseException e) {
                if (e == null) {
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });*/
    }


    private String getFromEditText(EditText editText, boolean trim) {
        String text = editText.getText().toString();
        if (trim)
            return text.trim();
        return text;
    }

}

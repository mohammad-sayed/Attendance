package com.ms.checkin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUsername;
    private TextView mTvStatus;
    private Button mBtnCheckIn;
    private Button mBtnCheckOut;
    private ProgressBar mPbLoading;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvUsername = (TextView) findViewById(R.id.tv_username);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mBtnCheckIn = (Button) findViewById(R.id.btn_checkin);
        mBtnCheckOut = (Button) findViewById(R.id.btn_checkout);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        if (getIntent().hasExtra(Constants.USER_TAG))
            mUser = (User) getIntent().getSerializableExtra(Constants.USER_TAG);

        if (mUser != null) {
            mTvUsername.setText(mUser.getUserName());
            if (mUser.isCheckedIn())
                mTvStatus.setText(R.string.checked_in);
            else
                mTvStatus.setText(R.string.checked_out);
        }
        mBtnCheckIn.setOnClickListener(this);
        mBtnCheckOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkin:
                checkInOrOut(true);
                break;
            case R.id.btn_checkout:
                checkInOrOut(false);
                break;
        }
    }

    private void checkInOrOut(final boolean checkIn) {
        if (mUser != null && mUser.getUserName() != null) {
            mPbLoading.setVisibility(View.VISIBLE);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            //ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_USER_TABLE);
            query.getInBackground(mUser.getId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    mPbLoading.setVisibility(View.INVISIBLE);
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        object.put(Constants.PARSE_CHECKED_IN_COLUMN, checkIn);
                        object.saveInBackground();
                        mUser.setCheckedIn(checkIn);
                        if (checkIn)
                            mTvStatus.setText(R.string.checked_in);
                        else
                            mTvStatus.setText(R.string.checked_out);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.problem_updating, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

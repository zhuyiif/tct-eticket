package com.funenc.eticket.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.api.SubwayService;
import com.funenc.eticket.engine.QrSeedFetcher;
import com.funenc.eticket.engine.SelfUserInfoFetcher;
import com.funenc.eticket.model.SeedInfo;
import com.funenc.eticket.model.User;
import com.funenc.eticket.model.UserInfoResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.okhttp.HttpUtils;
import com.funenc.eticket.storage.AppStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mIdentityView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @InjectView(R.id.captcha_bt)
    protected Button btnCaptcha;
    @InjectView(R.id.login_bt)
    protected Button btnLogin;

    private HttpUtils httpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        // Set up the login form.
        mIdentityView = (EditText) findViewById(R.id.phone_et);
//        populateAutoComplete();
        mIdentityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    btnCaptcha.setEnabled(false);
                }else{
                    btnCaptcha.setEnabled(true);
                }
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password_et);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mIdentityView.getText().toString().trim().isEmpty() || mPasswordView.getText().toString().trim().isEmpty()){
                    btnLogin.setEnabled(false);
                }else{
                    btnLogin.setEnabled(true);
                }
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.login_bt);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        httpUtils = new HttpUtils();

        final Handler countDownHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what<1){
                    btnCaptcha.setText("重新获取");
                    btnCaptcha.setEnabled(true);
                }else{
                    btnCaptcha.setText(msg.what+"s后重新获取");
                }
                super.handleMessage(msg);
            }
        };
        btnCaptcha.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("login", "code button click");
                btnCaptcha.setEnabled(false);
                final Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int downTime = 60;
                    @Override
                    public void run() {
                        countDownHandler.sendEmptyMessage(downTime);
                        if(downTime<1){
                            timer.cancel();
                            return;
                        }
                        downTime--;
                    }
                }, 0, 1000);

                try {
                    httpUtils.postGetSMSCode(mIdentityView.getText().toString().trim());
                } catch (IOException e) {
                   Log.e("login",e.toString());
                } catch (JSONException e) {
                    Log.e("login",e.toString());
                }

            }
        });


        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("login", "login button click");
                String code = mPasswordView.getText().toString().trim();
                final String phoneNumber = mIdentityView.getText().toString().trim();

                try {
                    httpUtils.login(phoneNumber,code, new Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    Log.d("login", response.toString());
                                    String responseString = response.body().string();
                                    Log.d("login", responseString);

                                    try {

                                        JSONObject respObj = new JSONObject(responseString);
                                        Log.d("login", respObj.toString());

                                        AppStore.saveLoginContent(LoginActivity.this,respObj);

                                        JSONObject contentObj = respObj.getJSONObject("content");
                                        Log.d("contentobj",contentObj.toString());
                                        int code = respObj.getInt("code");

                                        Log.d("code", String.valueOf(code));

                                        if(code == 0){
                                            User selfUser = new User();
                                            selfUser.setPhone(phoneNumber);
                                            AppStore.setSelfUser(selfUser);
                                            finish();
                                        }

                                        new Thread(new QrSeedFetcher()).start();

                                        new Thread(new SelfUserInfoFetcher()).start();

                                    } catch (Throwable t) {
                                        Log.e("login", t.toString(), t);
                                    }

                                }
                            }
                    );
                } catch (IOException e) {
                    Log.e("login",e.toString(), e);
                } catch (JSONException e) {
                    Log.e("login",e.toString(), e);
                }

            }
        });

//       String loginInfo =  AppStore.getLoginContent(this);
//       Log.d("logininfo",loginInfo);
//
//
//        JSONObject respObj = null;
//        try {
//            respObj = new JSONObject(loginInfo);
//            JSONObject contentObj = respObj.getJSONObject("content");
//            Log.d("contentobj",contentObj.toString());
//            int code = respObj.getInt("code");
//
//            Log.d("code", String.valueOf(code));
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mIdentityView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mIdentityView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mIdentityView.setError(getString(R.string.error_field_required));
            focusView = mIdentityView;
            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mIdentityView.setError(getString(R.string.error_invalid_email));
//            focusView = mIdentityView;
//            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

//        mIdentityView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @OnClick(R.id.skip_tv)
    protected void onSkip(View view){
        finish();
    }
}


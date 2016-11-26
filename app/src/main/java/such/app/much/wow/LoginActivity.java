package such.app.much.wow;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String emailId = null, userNameText= null;

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    public static final List<String> mPermssions = new ArrayList<String>(){{
        add("public_profile");
        add("email");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else{
            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "such.app.much.wow",
                    PackageManager.GET_SIGNATURES);



            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }



    }


    public void facebook_login(View v)
    {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, mPermssions, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null)
                    Log.d("SuchMuchAppWow", "User Canceled Facebook Login");


                else if (parseUser.isNew())
                {
                    Log.d("SuchAppMuchAppWow", "User signed up and logged in through facebook");
                    getUserDetailsFromFB();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);


                } else

                {
                    Log.d("SuchAppMuchAppWow", "User logged in through facebook");
                    getUserDetailsFromParse();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);


                }
            }
        });




    }



    private void getUserDetailsFromParse()
    {
        ParseUser parseUser = ParseUser.getCurrentUser();
       // _usernameText.setText(parseUser.getUsername());
        String new_user;
        new_user=parseUser.getUsername();
        //_passwordText.setText(parseUser.getEmail());
        Toast.makeText(LoginActivity.this, "Welcome Back: "+new_user,Toast.LENGTH_LONG).show();
    }

    private void getUserDetailsFromFB(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                try {
                    userNameText = jsonObject.getString("name");

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                try {
                    emailId = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveNewUser();
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email");
        request.setParameters(parameters);
        request.executeAsync();


    }

    private void saveNewUser(){
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(userNameText);
        parseUser.setEmail(emailId);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(LoginActivity.this, "New User Signed Up: "+_usernameText.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }






    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    //Login Successful
                    //you can display sth or do sth
                    //For example Welcome + ParseUser.getUsername()
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();

                } else {
                    //Login Fail
                    //get error by calling e.getMessage()
                    onLoginFailed();
                    progressDialog.dismiss();

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default_post_image we just finish the Activity and log them in automatically
                this.finish();
            }
        }

        ParseFacebookUtils.onActivityResult(requestCode,resultCode,data);


    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("enter username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5) {
            _passwordText.setError("atleast 5 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.API.ApiUserService;
import com.example.topcvrecruiter.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginButton;
    private Button Register_Button;

    private ImageButton facebookButton;
    private ImageButton googleButton;

    private int id_Recruiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void setClick(){

        loginButton.setOnClickListener(view -> loginUser());

        facebookButton.setOnClickListener(view -> loginWithFacebook());

        googleButton.setOnClickListener(view -> signInWithGoogle());

        Register_Button.setOnClickListener(view -> registerButton());
    }

    private void registerButton(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("isSignUpButtonClicked", true);
        startActivity(intent);
        finish();
    }

    private void setWidget(){
        usernameInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        Register_Button = findViewById(R.id.Register_Button);
        facebookButton = findViewById(R.id.btnImage2);
        googleButton = findViewById(R.id.btnImage3);
        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        EdgeToEdge.enable(this);
    }

    @SuppressLint("CheckResult")
    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your username and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiUserService.apiUserService.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    boolean isValidUser = false;

                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            isValidUser = true;

                            if (!user.getPassword().equals(password)) {
                                Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            int userId = user.getId();

                            if(user.isRecruiter()){
                                ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(response -> {
                                            if (response.isIs_Confirm()) {
                                                navigateToMainActivity(userId, response.getRecruiterName(), response.getPhoneNumber());
                                            }
                                            else if (!response.isIs_Confirm() && response.isIs_Registered()){
                                                navigateToWaitingConfirmActivity(userId, response.getRecruiterName(), response.getPhoneNumber());
                                            }
                                            else if(!response.isIs_Registered()){
                                                navigateToInformationActivity(userId);
                                            } else {
                                                navigateToInformationActivity(userId);
                                            }
                                        }, throwable -> {
                                            Log.e("API Error", "Error fetching applicant: " + throwable.getMessage());
                                            navigateToInformationActivity(userId);
                                        });
                                return;
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "This account is not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if (!isValidUser) {
                        Toast.makeText(LoginActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> Toast.makeText(LoginActivity.this, "Error connecting to server: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("CheckResult")
    private void navigateToWaitingConfirmActivity(int id_User, String recruiterName, String phoneNumber){
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(id_User)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                id_Recruiter = recruiter.getId();
                                Intent intent = new Intent(LoginActivity.this, WaitingConfirmActivity.class);
                                intent.putExtra("user_id", id_User);
                                intent.putExtra("recruiterName", recruiterName);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("id_Recruiter", id_Recruiter);
                                Log.e("LoginActivity","ID: "+ id_Recruiter);
                                startActivity(intent);
                            } else {
                                Log.e("AccountFragment", "Recruiter not found");
                            }
                        },
                        throwable -> Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage())
                );
        finish();
    }

    @SuppressLint("CheckResult")
    private void navigateToInformationActivity(int id_User) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(id_User)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                id_Recruiter = recruiter.getId();
                                Intent intent = new Intent(LoginActivity.this, InformationActivity.class);
                                intent.putExtra("user_id", id_User);
                                intent.putExtra("id_Recruiter", id_Recruiter);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void navigateToMainActivity(int id_User, String recruiterName, String phoneNumber) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(id_User)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                id_Recruiter = recruiter.getId();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user_id", id_User);
                                intent.putExtra("recruiterName", recruiterName);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("id_Recruiter", id_Recruiter);
                                Log.e("LoginActivity","ID: "+ id_Recruiter);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, null);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                        String username = user.getEmail();
                        int loginCount = sharedPreferences.getInt(username + "_login_count", 0);
                        loginCount++;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(username + "_login_count", loginCount);
                        editor.apply();

                        if (loginCount == 1) {
                            startActivity(new Intent(LoginActivity.this, InformationActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Facebook login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                        String username = user.getEmail();
                        int loginCount = sharedPreferences.getInt(username + "_login_count", 0);
                        loginCount++;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(username + "_login_count", loginCount);
                        editor.apply();

                        if (loginCount == 1) {
                            startActivity(new Intent(LoginActivity.this, InformationActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Google login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

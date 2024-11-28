package com.example.topcvrecruiter.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.topcvrecruiter.AboutActicity;
import com.example.topcvrecruiter.ChangePasswordActivity;
import com.example.topcvrecruiter.LoginActivity;
import com.example.topcvrecruiter.PrivatePolicyActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.TermOfServiceActivity;
import com.example.topcvrecruiter.API.ApiUserService;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.API.ApiCompanyService;
import com.example.topcvrecruiter.Model.Company;
import com.example.topcvrecruiter.Model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountFragment extends Fragment {

    private Button about_application_button;
    private Button term_of_services_button;
    private Button privacy_policy_button;
    private Button sign_out_button;
    private Button change_background_button;
    private Button change_password_button;
    private Button edt_field;
    private Button edt_company;

    private ImageView background;
    private ImageView avatar;
    private ImageView change_avatar;

    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int id_User;
    private int id_Recruiter;
    private int companyId ;

    private String username;
    private String password;
    private String currentAvatarUrl;
    private String currentBackgroundUrl;

    private Uri backgroundImageUri;
    private Uri avatarImageUri;

    private TextView recruiter_name;
    private TextView email_address;
    private TextView ac_company_name;
    private TextView ac_field;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setWidget(view);

        initImagePicker();

        initListeners();

        return view;
    }

    private void setWidget(View view) {
        about_application_button = view.findViewById(R.id.about_application_button);
        term_of_services_button = view.findViewById(R.id.term_of_services_button);
        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        change_background_button = view.findViewById(R.id.change_background_button);
        background = view.findViewById(R.id.background);
        avatar = view.findViewById(R.id.avatar);
        change_avatar = view.findViewById(R.id.change_avatar);
        change_password_button = view.findViewById(R.id.change_password_button);
        recruiter_name = view.findViewById(R.id.recruiter_name);
        email_address = view.findViewById(R.id.email_address);
        ac_company_name = view.findViewById(R.id.ac_company_name);
        ac_field = view.findViewById(R.id.ac_field);
        edt_company = view.findViewById(R.id.edt_company);
        edt_field = view.findViewById(R.id.edt_field);
        assert getArguments() != null;
        id_User = getArguments().getInt("user_id", 0);
        id_Recruiter = getArguments().getInt("id_Recruiter", 0);

        getUserById(id_User);
        getRecruiterById(id_User);
    }

    private void initListeners() {
        about_application_button.setOnClickListener(view -> startActivity(new Intent(getContext(), AboutActicity.class)));
        privacy_policy_button.setOnClickListener(view -> startActivity(new Intent(getContext(), PrivatePolicyActivity.class)));
        term_of_services_button.setOnClickListener(view -> startActivity(new Intent(getContext(), TermOfServiceActivity.class)));

        sign_out_button.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show());

        change_background_button.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickerLauncherBackground.launch(intent);
                        return null;
                    });
        });

        change_avatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);
                        return null;
                    });
        });

        change_password_button.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            intent.putExtra("user_id", id_User);
            startActivity(intent);
        });

        edt_company.setOnClickListener(v -> {
            String companyName = ac_company_name.getText().toString();
            showEditDialog("Company name", companyName, newName -> {
                ac_company_name.setText(newName);
                updateCompanyInfo(newName, ac_field.getText().toString());
            });
        });

        edt_field.setOnClickListener(v -> {
            String field = ac_field.getText().toString();
            showEditDialog("Field", field, newField -> {
                ac_field.setText(newField);
                updateCompanyInfo(ac_company_name.getText().toString(), newField);
            });
        });
    }

    private void showEditDialog(String title, String currentValue, OnSaveListener onSaveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit " + title);
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit, null);
        EditText editText = customView.findViewById(R.id.edit_value);
        editText.setText(currentValue);
        builder.setView(customView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newValue = editText.getText().toString();
            onSaveListener.onSave(newValue);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public interface OnSaveListener {
        void onSave(String newValue);
    }

    private void initImagePicker() {
        imagePickerLauncherBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        backgroundImageUri = result.getData().getData();
                        background.setImageURI(backgroundImageUri);
                        updateBackground(backgroundImageUri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        avatarImageUri = result.getData().getData();
                        avatar.setImageURI(avatarImageUri);
                        updateAvatar(avatarImageUri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @SuppressLint("CheckResult")
    private void getUserById(int userId) {
        ApiUserService.apiUserService.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            if (user != null) {
                                username = user.getUsername();
                                password = user.getPassword();
                                currentAvatarUrl = user.getAvatar();
                                currentBackgroundUrl = user.getImageBackground();
                                setUserImages(currentAvatarUrl, currentBackgroundUrl);
                            } else {
                                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching user: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load user", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void updateAvatar(Uri avatarUri) {
        String avatarUrl = (avatarUri != null) ? avatarUri.toString() : (currentAvatarUrl != null ? currentAvatarUrl : "");

        if (avatarUrl.isEmpty()) {
            Log.e("AccountFragment", "Avatar is empty, skipping update.");
            return;
        }

        String backgroundUrl = (backgroundImageUri != null) ? backgroundImageUri.toString() : (currentBackgroundUrl != null ? currentBackgroundUrl : "");

        User user = new User();
        user.setId(id_User);
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(avatarUrl);
        user.setImageBackground(backgroundUrl);

        ApiUserService.apiUserService.updateUserById(id_User, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("AccountFragment", "User updated successfully");
                            Toast.makeText(getContext(), "User info updated successfully", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error updating user: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void updateBackground(Uri backgroundUri) {
        String backgroundUrl = (backgroundUri != null) ? backgroundUri.toString() : (currentBackgroundUrl != null ? currentBackgroundUrl : "");

        if (backgroundUrl.isEmpty()) {
            Log.e("AccountFragment", "Background is empty, skipping update.");
            return;
        }

        String avatarUrl = (avatarImageUri != null) ? avatarImageUri.toString() : (currentAvatarUrl != null ? currentAvatarUrl : "");

        User user = new User();
        user.setId(id_User);
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(avatarUrl);
        user.setImageBackground(backgroundUrl);

        ApiUserService.apiUserService.updateUserById(id_User, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d("AccountFragment", "User updated successfully");
                            Toast.makeText(getContext(), "User info updated successfully", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error updating user: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void setUserImages(String avatarUrl, String backgroundUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .override(200, 200)
                    .fitCenter()
                    .placeholder(R.drawable.account_ic)
                    .error(R.drawable.account_ic)
                    .into(avatar);
        }

        if (backgroundUrl != null && !backgroundUrl.isEmpty()) {
            Glide.with(this)
                    .load(backgroundUrl)
                    .override(1080, 720)
                    .fitCenter()
                    .placeholder(R.drawable.google_ic)
                    .error(R.drawable.google_ic)
                    .into(background);
        }
    }

    @SuppressLint("CheckResult")
    private void getRecruiterById(int userId) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterById(id_Recruiter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                id_Recruiter = recruiter.getId();
                                recruiter_name.setText(recruiter.getRecruiterName());
                                email_address.setText(recruiter.getEmailAddress());
                                getCompanyByRecruiterId(id_Recruiter);
                            } else {
                                Toast.makeText(getContext(), "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void getCompanyByRecruiterId(int recruiterId) {
        ApiCompanyService.ApiCompanyService.getCompanyByRecruiterId(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        company -> {
                            if (company != null) {
                                ac_company_name.setText(company.getName() != null ? company.getName() : "Not updated yet");
                                ac_field.setText(company.getField() != null ? company.getField() : "Not updated yet");
                                companyId = company.getId();
                            } else {
                                ac_company_name.setText("No company yet");
                                ac_field.setText("No field yet");
                                companyId = -1;
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching company: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Lỗi khi tải thông tin công ty", Toast.LENGTH_SHORT).show();
                            ac_company_name.setText("None");
                            ac_field.setText("None");
                            companyId = -1;
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void updateCompanyInfo(String companyName, String field) {
        if (companyId == -1) {
            Toast.makeText(getContext(), "Cannot update because there is no company information yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        Company company = new Company(companyName, "", "", field, "", false);

        ApiCompanyService.ApiCompanyService.updateCompanyById(companyId, company)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(getContext(), "Company update successful", Toast.LENGTH_SHORT).show(),
                        throwable -> Log.e("AccountFragment", "Error updating company: " + throwable.getMessage())
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

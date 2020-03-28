package com.example.blogapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.blogapp.Activities.RegisterActivity;
import com.example.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private Switch theme;
    private Button delete;
    private EditText pwd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences appSettingsPref = this.getActivity().getSharedPreferences("AppSettingsPref", 0);
        final SharedPreferences.Editor sharedPrefsEdit = appSettingsPref.edit();
        Boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);

        if(isNightModeOn){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        theme = view.findViewById(R.id.switchTheme);
        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPrefsEdit.putBoolean("NightMode", true);
                    sharedPrefsEdit.apply();
                }
                else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("NightMode", false);
                    sharedPrefsEdit.apply();
                }
            }
        });

        theme.setChecked(isNightModeOn);

        delete = view.findViewById(R.id.delete);
        pwd = view.findViewById(R.id.settings_pwd);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = pwd.getText().toString();

                if(password.isEmpty()){

                    Toast.makeText(getContext(), "Insert your current password to delete your account", Toast.LENGTH_SHORT).show();
                } else {

                    deleteUser(password);
                }
            }
        });

        return view;
    }

    public void deleteUser(final String password) {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        currentUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                            Intent restart = new Intent(getActivity().getApplicationContext(), RegisterActivity.class);
                                            startActivity(restart);
                                            getActivity().finish();
                                        }
                                        else {

                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }
}
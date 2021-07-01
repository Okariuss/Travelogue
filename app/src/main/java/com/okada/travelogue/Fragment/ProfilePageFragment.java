package com.okada.travelogue.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.okada.travelogue.Activity.ChangePasswordActivity;
import com.okada.travelogue.Activity.EditProfileActivity;
import com.okada.travelogue.Activity.HomeActivity;
import com.okada.travelogue.Activity.SettingsActivity;
import com.okada.travelogue.Activity.SettingsStartActivity;
import com.okada.travelogue.Activity.SignInStartActivity;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.HelperClasses.User;
import com.okada.travelogue.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilePageFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button btnToSignIn, profileSignOutButton;
    private CircleImageView profilePhotoView;
    private FirebaseStorage storage;
    private ImageButton toEditProfileButton, toSettingsButton, toChangePasswordButton;
    private TextView changeProfilePhotoTextView, editProfileTextView,
            changePasswordTextView, settingsTextView, pleaseLoginTextView;
    private StorageReference storageRef;
    private View view;
    private LayoutInflater layoutInflater;
    private ViewGroup viewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        viewContainer = container;
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        getData();
        return view;
    }

    private void initialization() {
        changeProfilePhotoTextView = view.findViewById(R.id.change_profile_photo_text_view);
        editProfileTextView = view.findViewById(R.id.edit_profile_text_view);
        changePasswordTextView = view.findViewById(R.id.change_password_text_view);
        settingsTextView = view.findViewById(R.id.settings_text_view);
        profileSignOutButton = view.findViewById(R.id.profile_sign_out_button);
        profilePhotoView = view.findViewById(R.id.account_photo_view);
        toEditProfileButton = view.findViewById(R.id.to_edit_profile_button);
        toSettingsButton = view.findViewById(R.id.to_settings_button);
        toChangePasswordButton = view.findViewById(R.id.to_change_password_button);
    }

    private void checkLanguage() {
        if (getActivity() != null) {
            Resources resources = LanguageResourceClass.getResource(getActivity());
            changeProfilePhotoTextView.setText(resources.getString(R.string.change_profile_photo));
            editProfileTextView.setText(resources.getString(R.string.edit_profile));
            changePasswordTextView.setText(resources.getString(R.string.change_password));
            settingsTextView.setText(resources.getString(R.string.settings));
            profileSignOutButton.setText(resources.getString(R.string.sign_out));

        }
    }


    private void getData() {
        if (firebaseAuth.getCurrentUser() != null) {
            view = layoutInflater.inflate(R.layout.fragment_profile_page, viewContainer, false);
            initialization();
            checkLanguage();
            if (firebaseAuth.getCurrentUser().getDisplayName() != null) {
                if (!firebaseAuth.getCurrentUser().getDisplayName().equals("")) {
                    ((RelativeLayout) view.findViewById(R.id.edit_profile_change_password_layout)).setVisibility(View.GONE);
                    ((View) view.findViewById(R.id.edit_profile_change_password_view)).setVisibility(View.GONE);
                }
            }
            LinearLayout linearLayout = view.findViewById(R.id.change_profile_photo_layout);
            toEditProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                    Animatoo.animateSwipeLeft(getActivity());
                }
            });
            toSettingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingsStartActivity.class);
                    startActivity(intent);
                    Animatoo.animateSwipeLeft(getActivity());
                }
            });
            profileSignOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    GoogleSignInOptions gso = new GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                    if (googleSignInClient != null) {
                        googleSignInClient.signOut();
                    }
                    Intent toHome = new Intent(getActivity(), HomeActivity.class);
                    toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(toHome);
                    Animatoo.animateDiagonal(getActivity());

                }
            });
            toChangePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent);
                    Animatoo.animateSwipeLeft(getActivity());
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intentToGallery, 2);
                    }
                }
            });

            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.child("users").getChildren()) {
                        try {
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ds.getKey())) {
                                HashMap hashMap = (HashMap) ds.getValue();
                                if (!(hashMap.get("photoUrl") + "").equals("null")) {
                                    Picasso.get().load(hashMap.get("photoUrl") + "").into(profilePhotoView);
                                }
                                break;
                            }
                        } catch (Exception e) {

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view = layoutInflater.inflate(R.layout.sign_in_for_vindow_layout, viewContainer, false);
            initialization1();
            checkLanguage1();
            btnToSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toSignInStartActivity = new Intent(getActivity(), SignInStartActivity.class);
                    startActivity(toSignInStartActivity);
                    Animatoo.animateInAndOut(getActivity());
                }
            });
        }
    }

    public void initialization1() {
        btnToSignIn = view.findViewById(R.id.please_login_sign_in);
        pleaseLoginTextView = view.findViewById(R.id.please_login_text_view);
    }

    public void checkLanguage1() {
        if (getActivity() != null) {
            Resources resources = LanguageResourceClass.getResource(getActivity());
            btnToSignIn.setText(resources.getString(R.string.sign_in));
            pleaseLoginTextView.setText(resources.getString(R.string.please_login_to_view));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            Uri imageData = data.getData();
            Bitmap bitmap;

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageData);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    profilePhotoView.setImageBitmap(bitmap);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageData);
                    profilePhotoView.setImageBitmap(bitmap);
                }
                StorageReference mountainsRef = storageRef.child("UserProfileImages").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(data1);
                String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        storageRef.child("UserProfileImages/" + uuid + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                                FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.child("users").getChildren()) {
                                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ds.getKey())) {
                                                HashMap hashMap = (HashMap) ds.getValue();
                                                User user = new User((String) hashMap.get("name"),
                                                        (String) hashMap.get("surname"),
                                                        (String) hashMap.get("email"),
                                                        (String) hashMap.get("password"),
                                                        Integer.parseInt((Long) hashMap.get("birthYear") + ""),
                                                        Integer.parseInt((Long) hashMap.get("birthMonth") + ""),
                                                        Integer.parseInt((Long) hashMap.get("birthDay") + ""),
                                                        uri.toString());
                                                database.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "").setValue(user);

                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        //System.out.println(taskSnapshot.getUploadSessionUri()                    );

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
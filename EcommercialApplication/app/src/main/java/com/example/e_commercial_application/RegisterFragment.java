package com.example.e_commercial_application;

import static android.app.Activity.RESULT_OK;
import static com.google.common.io.Files.getFileExtension;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private TextInputLayout layoutFullName, layoutEmail,layoutDOB, layoutMobile,layoutPassword,layoutAddress;
    private TextInputEditText edtFullName, edtEmail, edtDOB, edtMobile, edtPassword,edtAddress;
    private Button btnRegister;
    private CardView cardViewPP;
    private ImageView img;
    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uriImage;
    private DatePickerDialog picker;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    private static final String IMAGE_DIRECTORY = "/encoded_image";
    private static final int GALLERY = 1, CAMERA = 2;

    private static final String TAG = "RegisterFragment";

    public RegisterFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutFullName = view.findViewById(R.id.layoutName);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutDOB = view.findViewById(R.id.layoutDob);
        layoutMobile = view.findViewById(R.id.layoutMobile);
        layoutPassword = view.findViewById(R.id.layoutPassword);
        layoutAddress = view.findViewById(R.id.layoutAddress);

        cardViewPP = view.findViewById(R.id.cardViewPP);
        img = view.findViewById(R.id.imagePP);

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");

        cardViewPP.setOnClickListener(view1 -> {
            openFileChooser();
        });


        edtFullName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtDOB = view.findViewById(R.id.edtDob);
        edtMobile = view.findViewById(R.id.edtMobile);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtAddress = view.findViewById(R.id.edtAddress);

        btnRegister = view.findViewById(R.id.btnRegister);

        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        edtDOB.setOnClickListener(view1 -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(getContext(), (datePicker, year1, month1, dayOdMonth) -> edtDOB.setText(dayOdMonth + "/" + (month1 +1) + "/" + year1),year,month,day);
            picker.show();
        });

        btnRegister.setOnClickListener(view1 -> {
            
            String name = edtFullName.getText().toString();
            String email = edtEmail.getText().toString();
            String dob = edtDOB.getText().toString();
            String mobile = edtMobile.getText().toString();
            String password = edtPassword.getText().toString();
            String address = edtAddress.getText().toString();

            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$");
            Matcher matcher = pattern.matcher(password);
            
            if (name.isEmpty()){
                Toast.makeText(getContext(), "Please enter your full name", Toast.LENGTH_SHORT).show();
                layoutFullName.setError("Full Name is required");
                edtFullName.requestFocus();
            } else if (email.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your e-mail", Toast.LENGTH_SHORT).show();
                layoutEmail.setError("E-mail is required");
                edtEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please re-enter your e-mail", Toast.LENGTH_SHORT).show();
                layoutEmail.setError("Invalid E-mail");
                edtEmail.requestFocus();
            } else if (dob.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your Date of Birth", Toast.LENGTH_SHORT).show();
                layoutDOB.setError("Date of Birth is required");
                edtDOB.requestFocus();
            } else if (mobile.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                layoutMobile.setError("Mobile number is required");
                edtMobile.requestFocus();
            } else if (mobile.length()!=9) {
                Toast.makeText(getContext(), "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                layoutMobile.setError("Mobile number should be 9 digit");
                edtMobile.requestFocus();
            } else if (password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                layoutPassword.setError("Password is required");
                edtPassword.requestFocus();
            } else if (password.length()<6) {
                Toast.makeText(getContext(), "Please re-enter your password", Toast.LENGTH_SHORT).show();
                layoutPassword.setError("Password is too weak(at least 7 character)");
                edtPassword.requestFocus();
            } else if (!matcher.matches()) {
                Toast.makeText(getContext(), "Please re-enter your password", Toast.LENGTH_SHORT).show();
                layoutPassword.setError("Password is too weak(at least 1 lower case, 1 upper case and 1 special character)");
                edtPassword.requestFocus();
            }else if (address.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your Address", Toast.LENGTH_SHORT).show();
                layoutAddress.setError("Address is required");
                edtAddress.requestFocus();
            }else if (uriImage==null){
                img.requestFocus();
                Toast.makeText(getContext(), "No File Selected", Toast.LENGTH_SHORT).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                registerUser(name,email,dob,mobile,password,address);
            }


        });
    }



//


    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void registerUser(String name, String email, String dob, String mobile, String password,String address) {

        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    firebaseUser = auth.getCurrentUser();

                    UserDetails userDetails = new UserDetails(name, dob, mobile,address);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

                    databaseReference.child(firebaseUser.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                                firebaseUser.sendEmailVerification();

                                edtFullName.setText("");
                                edtEmail.setText("");
                                edtDOB.setText("");
                                edtMobile.setText("");
                                edtAddress.setText("");
                                UploadPic();
                                HomePage homePage = (HomePage) getActivity();
                                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                                bottomNavigationView.setVisibility(View.GONE);

                                UserProfile userProfile = new UserProfile();
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.containerFrame, userProfile); // Replace "fragment_container" with the actual ID of your fragment container in the layout
                                transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
                                transaction.commit();
                            } else {
                                Toast.makeText(getContext(), "User registration failed", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        layoutPassword.setError("At least 1 Uppercase, 1 Lowercase and 1 Special Character");
                        edtPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        layoutEmail.setError("Invalid Email");
                        edtEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        layoutEmail.setError("User is already registered with this email");
                        edtEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        }

    private void UploadPic() {

        if (uriImage !=null){
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() + "/displaypic." + getFileExtension((uriImage)));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = auth.getCurrentUser();
                            if (firebaseUser !=null){
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                                firebaseUser.updateProfile(profileUpdates);
                            }
                        }
                    });


                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    UserProfile userProfile = new UserProfile();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerFrame, userProfile); // Replace "fragment_container" with the actual ID of your fragment container in the layout
                    transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
                    transaction.commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No File was Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){

            uriImage = data.getData();
            Log.d("Image URI", uriImage.toString()); // Log the URI to check its value

            img.setImageURI(uriImage);

            img.setImageTintList(null);
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            img.setLayoutParams(layoutParams);
        }
    }





}
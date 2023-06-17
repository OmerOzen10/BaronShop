package com.example.e_commercial_application;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfile extends Fragment {

    private TextView txtWelcome,txtName,txtEmail,txtDOB,txtMobile,txtAddress,txtLogOut,txtChangePass,txtUpdateProfile;
    private ProgressBar progressBar3;
    private ImageView imagePP,settings;

    private String name, email, dob, mobile,address;
    CardView cardViewSetting;
    LinearLayout linear;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    private FirebaseUser firebaseUser;


    View omerDivider,logOutDivider;
    String TAG = "UserProfile";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtWelcome = view.findViewById(R.id.txtWelcome);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtDOB = view.findViewById(R.id.txtDOB);
        txtMobile = view.findViewById(R.id.txtMobile);
        txtAddress = view.findViewById(R.id.txtAddress);
        imagePP = view.findViewById(R.id.imageP);
        settings = view.findViewById(R.id.settings);
        cardViewSetting = view.findViewById(R.id.cardSettings);
        linear = view.findViewById(R.id.linear);
        txtUpdateProfile = view.findViewById(R.id.txtUpdateProfile);
        txtLogOut = view.findViewById(R.id.txtLogOut);
        txtChangePass = view.findViewById(R.id.txtChangePass);
        omerDivider = view.findViewById(R.id.omerDivider);
        logOutDivider = view.findViewById(R.id.LogOutDivider);



        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment.", Toast.LENGTH_SHORT).show();
        }else {
            showUserData(firebaseUser);
        }

        linear.setLayoutTransition(new LayoutTransition());
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);

        cardViewSetting.setOnClickListener(view1 -> {
            int v = (txtUpdateProfile.getVisibility() == View.GONE
                    && txtChangePass.getVisibility() == View.GONE &&
                    txtLogOut.getVisibility() == View.GONE
                    && omerDivider.getVisibility() == View.GONE
                    && logOutDivider.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());
            transitionSet.setInterpolator(new DecelerateInterpolator());
            transitionSet.setDuration(400); //
            TransitionManager.beginDelayedTransition(linear, transitionSet);

            if (txtUpdateProfile.getVisibility() != View.VISIBLE) {
                new Handler().postDelayed(() -> {
                    cardViewSetting.setCardElevation(60f);
                    cardViewSetting.setRadius(20f);
                }, 100);
            } else {
                cardViewSetting.setCardElevation(0f);
                cardViewSetting.setRadius(0f);
            }

            txtUpdateProfile.setVisibility(v);
            txtChangePass.setVisibility(v);
            txtLogOut.setVisibility(v);
            omerDivider.setVisibility(v);
            logOutDivider.setVisibility(v);
        });

        txtLogOut.setOnClickListener(view12 -> {
            auth.signOut();
            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.containerFrame, new UserFragment()).commit();
        });

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");

        imagePP.setOnClickListener(view13 -> openFileChooser());

        txtUpdateProfile.setOnClickListener(view1 -> {

            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.containerFrame, new UpdateProfileFragment()).addToBackStack(null).commit();
        });








    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);




    }
    private void UploadPic() {

       if (uriImage !=null){
           StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid() + "/displaypic." + getFileExtension(uriImage));

           fileReference.putFile(uriImage).addOnSuccessListener(taskSnapshot -> {
               fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                   Uri downloadUri = uri;
                   firebaseUser = auth.getCurrentUser();

                   UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                   firebaseUser.updateProfile(profileChangeRequest);
               });

               Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
           }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
       }else {
           Toast.makeText(getContext(), "No file was selected", Toast.LENGTH_SHORT).show();
       }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){

            uriImage = data.getData();
            UploadPic();
            Log.d("Image URI", uriImage.toString());

            imagePP.setImageURI(uriImage);

            imagePP.setImageTintList(null);
            ViewGroup.LayoutParams layoutParams = imagePP.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            imagePP.setLayoutParams(layoutParams);
        }
    }



    private void showUserData(FirebaseUser user) {

        if (HomePage.currentUser != null){
            String userID = firebaseUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    UserDetails userDetails = snapshot.getValue(UserDetails.class);
                    if (userDetails !=null){
                        name = userDetails.getName();
                        email = firebaseUser.getEmail();
                        dob = userDetails.getDob();
                        mobile = userDetails.getMobile();
                        address = userDetails.getAddress();

                        txtWelcome.setText("Welcome, " + name + "!");
                        txtName.setText(name);
                        txtEmail.setText(email);
                        txtDOB.setText(dob);
                        txtMobile.setText(mobile);
                        txtAddress.setText(address);

                        Uri uri = user.getPhotoUrl();
                        Glide.with(requireActivity()).load(uri).into(imagePP);
                        imagePP.setImageTintList(null);
                        ViewGroup.LayoutParams layoutParams = imagePP.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        imagePP.setLayoutParams(layoutParams);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            //if the user didn't log out and want to see his/her data

                txtWelcome.setText("Welcome, " + HomePage.currentUser.getName() + "!");
                txtEmail.setText(user.getEmail());
                txtDOB.setText(HomePage.currentUser.getDob());
                txtMobile.setText(HomePage.currentUser.getMobile());
                txtAddress.setText(HomePage.currentUser.getAddress());
                txtName.setText(HomePage.currentUser.getName());


                Uri uri = user.getPhotoUrl();
                Glide.with(requireActivity()).load(uri).into(imagePP);
                imagePP.setImageTintList(null);
                ViewGroup.LayoutParams layoutParams = imagePP.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imagePP.setLayoutParams(layoutParams);

                UserFragment userFragment = new UserFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containerFrame, userFragment);
                transaction.commit();

        }

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);


    }
}
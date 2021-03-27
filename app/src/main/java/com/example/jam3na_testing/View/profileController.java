package com.example.jam3na_testing.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jam3na_testing.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

/**
 * this activity Just to display the data that user added from sign up
 * can edit the data from edit button
 */
public class profileController extends AppCompatActivity {
    public static final String TAG = "TAG";
    RadioGroup radioGroup ;
    RadioButton gender;
   TextView profileFirstName,profileEmail,profilePhone,profileLastName , UserAge , UserHeight , UserWeight;
    ImageView profileImageView;
    Button backBtn , editBtn;
    FirebaseAuth fAuth ;
    DatabaseReference reff;
    FirebaseFirestore fStore;
    String visitorID ;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        profileFirstName = findViewById(R.id.profileFirstUsername);
        profileLastName = findViewById(R.id.profileLastUsername);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhoneNumber);
        profileImageView = findViewById(R.id.profileImageView);
        editBtn=findViewById(R.id.EditProfile);
        backBtn = findViewById(R.id.Back);
        radioGroup=findViewById(R.id.gender);
        UserAge = findViewById(R.id.UserAge);
        UserHeight= findViewById(R.id.UserHeight);
        UserWeight = findViewById(R.id.UserWeight);

        int redioId = radioGroup.getCheckedRadioButtonId() ;
        gender = findViewById(redioId);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        visitorID = fAuth.getCurrentUser().getUid();


        /**
         * Connection member Model to firebase datastore "Visitor" table to collect the data saved in sign up activity
         * */

        DocumentReference documentReference = fStore.collection("Visitor").document(visitorID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                profileFirstName.setText(value.getString("fname"));
                profileLastName.setText(value.getString("lname"));
                profileEmail.setText(value.getString("Uemail"));
                profilePhone.setText(value.getString("Uphone"));

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ModifyProfileActivity.class));

            }
        });
      /**  DocumentReference documentReferenceEdit = fStore.collection("member").document(visitorID);
        documentReferenceEdit.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                UserAge.setText(value.getString("Uage"));
                UserHeight.setText(value.getString("Uheight"));
                UserWeight.setText(value.getString("Uweight"));

            }
        });
*/



        //authentication for google login
        GoogleSignInAccount SignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(SignInAccount != null){
            profileFirstName.setText(SignInAccount.getDisplayName());
            profileEmail.setText(SignInAccount.getEmail());
            profileLastName.setText(SignInAccount.getFamilyName());

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu , menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), profileController.class));
                return true;
            case R.id.Settings:
                Toast.makeText(this , " settings not ready yet !",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.LogOut:
                fAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginController.class));
                return true;
            case R.id.CreateGroup:
                startActivity(new Intent(getApplicationContext(),CreateGroupController.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
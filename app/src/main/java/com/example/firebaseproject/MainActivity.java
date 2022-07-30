package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class MainActivity<R_sign_in> extends AppCompatActivity {

    Button btnNote , btnTodo ;
    EditText ettitle , etsubtitle ;
    RecyclerView rView ;

    FirebaseUser firebaseUser ;
    ArrayList<Notes> list = new ArrayList<>() ;
    public static final int R_sign_in = 123 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNote = findViewById(R.id.btnNote) ;
        ettitle = findViewById(R.id.ettitle) ;
        etsubtitle = findViewById(R.id.etsubtitle) ;
        rView = findViewById(R.id.rView) ;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        if(firebaseUser != null){
            //logged in
            addListener() ;
        }
        else{
            //logged out
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build()
                            ))
                            .build() , R_sign_in
            );

        }

    }





    protected void onActivityResult(int requestcode , int result , Intent data) {

        super.onActivityResult(requestcode, result, data);

        if(requestcode == R_sign_in){

            IdpResponse response = IdpResponse.fromResultIntent(data) ;

            if (result == RESULT_OK) {
                // Successfully signed in
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                addListener();
                Log.d("TAG" , firebaseUser.getDisplayName()) ;
                Log.d("TAG" , firebaseUser.getUid()) ;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button

                    Toast.makeText(this , "Login cancel" , Toast.LENGTH_SHORT).show();
                    Log.d("TAG" , "Login cancel") ;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {

                    Toast.makeText(this , "Network error" , Toast.LENGTH_SHORT).show();
                    Log.d("TAG" , "Network error") ;
                }


                Toast.makeText(this , "Some other issue" , Toast.LENGTH_SHORT).show();
                Log.d("TAG" , "Some other issue") ;
            }
        }
    }







    private void addListener() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference() ;
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ettitle.getText().toString() ;
                String subtitle = etsubtitle.getText().toString() ;

                Notes notes = new Notes(title , subtitle) ;
                //Upload your note here
                dbref.child("notes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("note")
                        .push().setValue(notes) ;

            }
        });



        NotesAdapter notesAdapter = new NotesAdapter(list) ;

        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(notesAdapter);




        dbref.child("notes")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("note")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear() ;

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Notes n = snapshot1.getValue(Notes.class) ;
                    list.add(n) ;
                }

                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
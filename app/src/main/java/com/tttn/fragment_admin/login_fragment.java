package com.tttn.fragment_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tttn.R;


public class login_fragment  extends Fragment {

     private  EditText username, password;
     private   Button loginButton;
     private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String eusername, epass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eusername = username.getText().toString();
                epass = password.getText().toString();
                if(TextUtils.isEmpty(eusername)){
                    Toast.makeText(requireContext(),"Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(epass)){
                    Toast.makeText(requireContext(),"Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(eusername, epass)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(requireContext(), "Đăng nhập thành công",
                                            Toast.LENGTH_SHORT).show();
                                    if(eusername== "admin@email.com")
                                    {

                                    }
                                     else{
                                         String userID = user.getUid();
                                        Query query = db.collection("person")
                                                .whereEqualTo("loginID", userID);
                                        query.get()
                                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                        String personID = document.getId();
                                                        NavDirections action = login_fragmentDirections.actionLoginFragmentToUsermainFragment(personID);
                                                        Navigation.findNavController(view).navigate(action);
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                });
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "Đăng nhập không thành công, vui lòng kiểm tra lại email, password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        );
            }
        });
    }


    void init() {
        username = getView().findViewById(R.id.username);
        password = getView().findViewById(R.id.password);
        loginButton = getView().findViewById(R.id.loginButton);
    }
}

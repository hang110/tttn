package com.tttn;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tttn.model.ChamCongModel;
import com.tttn.model.LichLamModel;
import com.tttn.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager_M {
    private static DataManager_M instance;
    private static final String TAG = "Firebase log: ";
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    public List<LichLamModel> listLichLam;
    public List<ChamCongModel> listChamCong;
    public List<UserModel> listUser;

    private DataManager_M() {
        db = FirebaseFirestore.getInstance();
        listLichLam = new ArrayList<>();
        listChamCong = new ArrayList<>();
        listUser = new ArrayList<>();
        startListening();
    }
    public static synchronized DataManager_M getInstance() {
        if (instance == null) {
            instance = new DataManager_M();
        }
        return instance;
    }
    public List<LichLamModel> getListLichLam() {
        return listLichLam;
    }


    public void startListening() {
        Query query = db.collection("lichlam");
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                listLichLam.clear();
                for (DocumentSnapshot document : value.getDocuments()) {
                    LichLamModel lichLam = document.toObject(LichLamModel.class);
                    listLichLam.add(lichLam);
                }
            }
        });
    }
    public void DebugCustom(String msg){
        Log.d(TAG,msg);
    }


    public void getAllLichlam(String date, LichlamCallback callback) {
        List<LichLamModel> lmd = new ArrayList<>();
        Query query = db.collection("lichlam")
                .whereEqualTo("workday", date);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        LichLamModel model = document.toObject(LichLamModel.class);
                        DebugCustom(model.getWorkday());
                        lmd.add(model);
                    }
                    callback.onSuccess(lmd);
                } else {

                    callback.onSuccess(lmd);
                }
            } else {
                // Xử lý lỗi
                callback.onFailure(task.getException());
            }
        });
    }

    public void getUser(String idU,UserCallback callback){
        UserModel x = new UserModel();
        db.collection("user").whereEqualTo("idUser", idU)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            UserModel model = querySnapshot.getDocuments().get(0).toObject(UserModel.class);
                            callback.onSuccess(model, querySnapshot.getDocuments().get(0).getId());
                        } else {
                            callback.onSuccess(x,"");
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}

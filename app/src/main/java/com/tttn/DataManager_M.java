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

public class DataManager {
    private static DataManager instance;
    private static final String TAG = "Firebase log: ";
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    public String idUser;
    public List<LichLamModel> listLichLam;
    public List<ChamCongModel> listChamCong;
    public List<UserModel> listUser;

    private DataManager() {
        db = FirebaseFirestore.getInstance();
        listLichLam = new ArrayList<>();
        listChamCong = new ArrayList<>();
        listUser = new ArrayList<>();
        startListening();
    }
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    public void SetIdUser(String id){
        idUser = id;
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
    public void getChamCong(String documentId, String date, ChamCongCallback callback) {
        Query query = db.collection("chamcong").whereEqualTo("idUser", documentId).whereEqualTo("date", date);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    ChamCongModel model = querySnapshot.getDocuments().get(0).toObject(ChamCongModel.class);
                    callback.onSuccess(model,querySnapshot.getDocuments().get(0).getId());
                } else {
                    callback.onSuccess(new ChamCongModel("", "", "", "","",""),"");
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public  void PutDataChamCong(ChamCongModel model){
        db.collection("chamcong").add(model);
    }

    public void updateChamCongData(String documentId,ChamCongModel model) {
        DocumentReference docRef = db.collection("chamcong").document(documentId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("idUser", model.getIdUser());
        updates.put("date", model.getDate());
        updates.put("time_CI", model.getTime_CI());
        updates.put("time_CO", model.getTime_CO());
        updates.put("image_CI",model.getImage_CI());
        updates.put("image_CO",model.getImage_CO());
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> System.out.println("Cập nhật dữ liệu thành công"))
                .addOnFailureListener(e -> System.out.println("Lỗi khi cập nhật dữ liệu: " + e.getMessage()));
    }

    public void putDangky (LichLamModel model)
    {
        db.collection("lichlam").add(model);
    }

    public void DebugCustom(String msg){
        Log.d(TAG,msg);
    }

    public void getLichlam(String startDate, String endDate, LichlamCallback callback) {
        List<LichLamModel> lmd = new ArrayList<>();
        Query query = db.collection("lichlam").whereEqualTo("idUser", idUser)
                .whereGreaterThanOrEqualTo("workday", "01/11/2023")
                .whereLessThanOrEqualTo("workday", "30/12/2023");

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

    public void getMotLichlam(String date, DangkyCallback callback) {
        LichLamModel x = new LichLamModel("",1,"");
        db.collection("lichlam").whereEqualTo("idUser", idUser)
                .whereEqualTo("workday", date)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            LichLamModel model = querySnapshot.getDocuments().get(0).toObject(LichLamModel.class);
                            callback.onSuccess(model, querySnapshot.getDocuments().get(0).getId());
                        } else {
                            callback.onSuccess(x,"");
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void checkDangky(String date, int caID, LichlamCallback callback){
        List<LichLamModel> lmd = new ArrayList<>();
        Query query = db.collection("lichlam")
                .whereEqualTo("workday", date).whereEqualTo("caID", caID);
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

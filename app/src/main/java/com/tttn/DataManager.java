package com.tttn;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
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
    public void updateLichlam(String documentId,LichLamModel lichLamModel) {
        DocumentReference docRef = db.collection("lichlam").document(documentId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("idUser",lichLamModel.getIdUser());
        updates.put("caID",lichLamModel.getCaID());
        updates.put("workday",lichLamModel.getWorkday());
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> System.out.println("Cập nhật dữ liệu thành công"))
                .addOnFailureListener(e -> System.out.println("Lỗi khi cập nhật dữ liệu: " + e.getMessage()));
    }


    public void DebugCustom(String msg){
        Log.d(TAG,msg);
    }

    public void getLichlam(String startDate, String endDate, LichlamCallback callback) {
        List<LichLamModel> lmd = new ArrayList<>();
        Query query = db.collection("lichlam").whereEqualTo("idUser", idUser)
                .whereGreaterThanOrEqualTo("workday", startDate)
                .whereLessThanOrEqualTo("workday", endDate);

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

    public void getUserbyID(String id,UserCallback callback){
        UserModel x = new UserModel();
        db.collection("user").document(id)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot querySnapshot = task.getResult();
                        if (querySnapshot.exists()) {
                            UserModel model = querySnapshot.toObject(UserModel.class);
                            callback.onSuccess(model, id);
                        } else {
                            callback.onSuccess(x,"");
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
    public void getAllLichlam(String date, LichlamCallback callback) {
        List<LichLamModel> lmd = new ArrayList<>();
        List<String> id = new ArrayList<>();
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
                        id.add( querySnapshot.getDocuments().get(0).getId());
                    }
                    callback.onSuccessGetAll(lmd,id);
                } else {

                    callback.onSuccessGetAll(lmd,id);
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
    public void GetAllEmployee(UserCallback callback){
        List<UserModel> x = new ArrayList<>();
        List<String > id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<String> idU = new ArrayList<>();
        db.collection("user").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    int index=0;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        UserModel model = document.toObject(UserModel.class);
                        x.add(model);
                        id.add(querySnapshot.getDocuments().get(index).getId());
                        name.add(model.getName());
                        idU.add(model.getIdUser());
                        index++;
                    }
                    callback.onSuccessGetList(x, id);
                    callback.onSuccessGetName(name, idU);
                } else {
                    callback.onSuccessGetList(x,id);
                    callback.onSuccessGetName(name, idU);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void GetChamCongtheothang(String startDate, String endDate, ChamCongCallback chamCongCallback){
        Map<String , Float> map= new HashMap<>();
        Query query = db.collection("chamcong")
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ChamCongModel model = document.toObject(ChamCongModel.class);
                        if (map.containsKey(model.getIdUser())) {
                            float gio = 0;
                            if (model.getTime_CO().isEmpty()) {
                                gio = 5;
                            } else {
                                String[] splipCI = model.getTime_CI().split(":");
                                String[] splipCO = model.getTime_CO().split(":");
                                float timeSeconds = - Integer.parseInt(splipCI[0]) * 3600 + Integer.parseInt(splipCI[1]) * 60 + Integer.parseInt(splipCI[2])
                                        + Integer.parseInt(splipCO[0]) * 3600 + Integer.parseInt(splipCO[1]) * 60 + Integer.parseInt(splipCO[2]);
                                gio = timeSeconds / 3600;
                            }
                            float value = map.get(model.getIdUser()) + gio;
                            map.put(model.getIdUser(), value);
                        } else {
                            float gio = 0;
                            if (model.getTime_CO().isEmpty()) {
                                gio = 5;
                            } else {
                                String[] splipCI = model.getTime_CI().split(":");
                                String[] splipCO = model.getTime_CO().split(":");
                                float timeSeconds = - Integer.parseInt(splipCI[0]) * 3600 + Integer.parseInt(splipCI[1]) * 60 + Integer.parseInt(splipCI[2])
                                        + Integer.parseInt(splipCO[0]) * 3600 + Integer.parseInt(splipCO[1]) * 60 + Integer.parseInt(splipCO[2]);
                                gio = timeSeconds / 3600;
                            }
                            map.put(model.getIdUser(), gio);
                        }

                    } chamCongCallback.onSuccessTotal(map);
                }else {
                    chamCongCallback.onSuccessTotal(map);
                }
            } else {
            // Xử lý lỗi
            chamCongCallback.onFailure(task.getException());
        }
        });
    }
    public void updateUser(String documentId, UserModel user, Context context) {
        DocumentReference docRef = db.collection("user").document(documentId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", user.getName());
        updates.put("address", user.getAddress());
        updates.put("idUser", user.getIdUser());
        updates.put("luong", user.getLuong());
        updates.put("tele", user.getTele());
        docRef.update(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cập nhật thông tin nhân viên thành công", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    public void getChamCongofUser(String startDate, String endDate, String idU ,ChamCongCallback callback) {
        List<ChamCongModel> lmd = new ArrayList<>();
        Query query = db.collection("chamcong").whereEqualTo("idUser", idU)
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ChamCongModel model = document.toObject(ChamCongModel.class);
                        lmd.add(model);
                    }
                    callback.onSuccessAll(lmd);
                } else {
                    callback.onSuccessAll(lmd);
                }
            } else {
                // Xử lý lỗi
                callback.onFailure(task.getException());
            }
        });
    }
}

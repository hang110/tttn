package com.tttn.fragment_user;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tttn.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.tttn.fragment_admin.login_fragmentDirections;

import androidx.camera.core.CameraSelector;
import androidx.camera.view.PreviewView;
import androidx.camera.core.Preview;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.Calendar;

public class CheckinFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private TextView date, time, ca;
    private Handler handler;
    private Runnable updateTimeRunnable;
    private ImageButton back, his;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.checkin, container, false);
        previewView = rootView.findViewById(R.id.previewView);
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
            }
        }, ContextCompat.getMainExecutor(requireContext()));
        return rootView;
    }
    private void bindPreview(ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = String.valueOf(day);
        }
        String dayOfWeekString;
        if( dayOfWeek >1)
        {
            dayOfWeekString = "Thứ " + dayOfWeek;
        } else {
            dayOfWeekString = "Chủ nhật";
        }
        date.setText(dayOfWeekString+", "+dayString+"/"+month+'/'+year);
        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                time.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                handler.postDelayed(this, 100);
            }
        };
        String string = CheckinFragmentArgs.fromBundle(getArguments()).getUserID();

        CollectionReference dangky = db.collection("dangkylich");
        Query lichLamQuery = db.collection("dangkylich")
                .whereEqualTo("userID",string );
        lichLamQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String lichLamID = document.getString("lichlamID");
                        getCaInfo(lichLamID);
                    }
                })
                .addOnFailureListener(e -> {
                });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = CheckinFragmentDirections.actionCheckinBackto(string);
                Navigation.findNavController(view).navigate(action);
            }
        });
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = CheckinFragmentArgs.fromBundle(getArguments()).getUserID();
                NavDirections action = CheckinFragmentDirections.actionCheckinFragmentToHistoryFragment(string);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }
    void init() {
        date = getView().findViewById(R.id.date_now);
        time = getView().findViewById(R.id.time_now);
        ca = getView().findViewById(R.id.ca);
        back = getView().findViewById(R.id.backbt);
        his = getView().findViewById(R.id.history);
    }
    void getCaInfo(String lichLamID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query lichLamInfoQuery = db.collection("lichlam")
                .whereEqualTo("ID", lichLamID);
        lichLamInfoQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String caID = document.getString("caID");
                        getCaDetails(caID);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
    void getCaDetails(String caID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query caDetailsQuery = db.collection("ca")
                .whereEqualTo("ID", caID);

        caDetailsQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("name");
                        String timeStart = document.getString("time_start");
                        String timeEnd = document.getString("time_end");
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);

                    }
                })
                .addOnFailureListener(e -> {
                });
    }

}

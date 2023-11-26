package com.tttn.fragment_user;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tttn.ChamCongCallback;
import com.tttn.DataManager;
import com.tttn.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.tttn.fragment_admin.login_fragmentDirections;
import com.tttn.model.ChamCongModel;
import com.tttn.model.LichLamModel;

import androidx.camera.core.CameraSelector;
import androidx.camera.view.PreviewView;
import androidx.camera.core.Preview;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

public class CheckinFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private TextView date, time, ca;
    private Button chamcong;
    private Handler handler;
    private Runnable updateTimeRunnable;
    private ImageButton back, his;
    private List<LichLamModel> listLichlam;
    private ImageCapture imageCapture;
    private File photoFile;
    private boolean isDoneTest;
    private String idUser, ngay, formattedTime;



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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setupUi();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CheckinFragmentDirections.actionCheckinBackto();
                Navigation.findNavController(view).navigate(action);
            }
        });
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CheckinFragmentDirections.actionCheckinFragmentToHistoryFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        listLichlam = DataManager.getInstance().getListLichLam();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
        formattedTime = dateFormatTime.format(currentDate);
        ngay = dateFormat.format(currentDate);
        idUser = DataManager.getInstance().idUser;
        isDoneTest = false;
        for (LichLamModel x: listLichlam) {
            if(x.getWorkday().equals(ngay) && x.getCaID() == 1 && x.getIdUser().equals(idUser)){
                if(hour>7 && hour <14) {
                    chamcong.setEnabled(true);
                    isDoneTest = true;
                    ca.setText("Ca sáng 8:00 - 13:00");
                    break;
                }
            }else if(x.getWorkday().equals(ngay) && x.getCaID() == 2 && x.getIdUser().equals(idUser)){
                if(hour>12 && hour <19) {
                    chamcong.setEnabled(true);
                    isDoneTest = true;
                    ca.setText("Ca chiều 13:00 - 18:00");
                    break;
                }
            }else if(x.getWorkday().equals(ngay) && x.getCaID() == 3 && x.getIdUser().equals(idUser)){
                if(hour>17) {
                    chamcong.setEnabled(true);
                    isDoneTest = true;
                    ca.setText("Ca tối 18:00 - 23:00");
                    break;
                }
            }
            else {
                ca.setText("Không có ca nào hợp lệ!!!");
                chamcong.setEnabled(false);
            }
        }
        chamcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {TakePictureImage();
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
        chamcong = getView().findViewById(R.id.capture_button);
        ca = getView().findViewById(R.id.ca);
    }
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
        imageCapture = new ImageCapture.Builder().build();
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }
    private void setupUi(){
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
        handler.post(updateTimeRunnable);
    }
    private void TakePictureImage(){
        photoFile = createPhotoFile();
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputFileOptions, Executors.newSingleThreadExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        uploadImageToFirebase(photoFile);
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.d("Error", exception.toString());
                    }
                });
    }

    private void uploadImageToFirebase(File imageFile) {
        String fileName = imageFile.getName();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
        Uri fileUri = Uri.fromFile(imageFile);
        storageRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        funTimekeeping(imageUrl);
                    });
                })
                .addOnFailureListener(exception -> {
                    Log.e("Firebase", "Upload failed: " + exception.getMessage());
                });
    }
    private void funTimekeeping(String imageUrl){
        if(isDoneTest){
            ChamCongModel _model = new ChamCongModel(idUser,ngay,"","", "","");
            DataManager.getInstance().getChamCong(idUser, ngay, new ChamCongCallback() {
                @Override
                public void onSuccess(ChamCongModel model,String id) {
                    if(model.getIdUser().isEmpty()){
                        _model.setTime_CI(formattedTime);
                        _model.setImage_CI(imageUrl);
                        DataManager.getInstance().PutDataChamCong(_model);

                    }else {
                        _model.setTime_CI(model.getTime_CI());
                        _model.setTime_CO(formattedTime);
                        _model.setImage_CI(model.getImage_CI());
                        _model.setImage_CO(imageUrl);
                        DataManager.getInstance().updateChamCongData(id, _model);
                    }
                }
                @Override
                public void onSuccessAll(List<ChamCongModel> model){}
                @Override
                public void onSuccessTotal(Map<String, Float> map){}
                @Override
                public void onFailure(Exception e) {

                }
            });
            Toast.makeText(requireContext(), "Chấm công thành công",Toast.LENGTH_SHORT).show();
        }}

    private File createPhotoFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";
        File storageDir = new File(requireContext().getExternalFilesDir(null), "your_app_name");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return new File(storageDir, fileName);
    }

}

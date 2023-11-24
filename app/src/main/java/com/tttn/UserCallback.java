package com.tttn;


import com.tttn.model.LichLamModel;
import com.tttn.model.UserModel;

public interface UserCallback {
    void onSuccess(UserModel model, String id);
    void onFailure(Exception e);
}


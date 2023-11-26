package com.tttn;


import com.tttn.model.LichLamModel;
import com.tttn.model.UserModel;

import java.util.List;

public interface UserCallback {
    void onSuccess(UserModel model, String id);
    void onFailure(Exception e);
    void onSuccessGetList(List<UserModel> models, List<String> id);
    void onSuccessGetName(List<String> name, List<String> id);
}


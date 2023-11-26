package com.tttn;


import com.tttn.model.LichLamModel;

import java.util.List;

public interface LichlamCallback {
    void onSuccess(List<LichLamModel> model);
    void onSuccessGetAll(List <LichLamModel> model, List<String> id);
    void onFailure(Exception e);
}


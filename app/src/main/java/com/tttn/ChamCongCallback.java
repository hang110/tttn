package com.tttn;

import com.tttn.model.ChamCongModel;

public interface ChamCongCallback {
    void onSuccess(ChamCongModel model, String id);

    void onFailure(Exception e);
}


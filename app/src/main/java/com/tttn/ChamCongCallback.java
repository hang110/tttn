package com.tttn;

import com.tttn.model.ChamCongModel;

import java.util.List;
import java.util.Map;

public interface ChamCongCallback {
    void onSuccess(ChamCongModel model, String id);
    void onSuccessTotal(Map<String, Float> total);
    void onSuccessAll(List<ChamCongModel> model);
    void onFailure(Exception e);
}


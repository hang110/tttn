package com.tttn;


import com.tttn.model.LichLamModel;

import java.util.List;

public interface DangkyCallback {
    void onSuccess(LichLamModel model, String id);
    void onFailure(Exception e);
}


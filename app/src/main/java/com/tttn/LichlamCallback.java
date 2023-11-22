package com.tttn;


import com.tttn.model.LichLamModel;

import java.util.List;

public interface LichlamCallback {
    void onSuccess(List<LichLamModel> model);
    void onFailure(Exception e);
}


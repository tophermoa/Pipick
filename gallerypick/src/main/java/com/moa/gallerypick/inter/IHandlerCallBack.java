package com.moa.gallerypick.inter;

import java.util.List;



public interface IHandlerCallBack {

    void onStart();

    void onSuccess(List<String> photoList);

    void onCancel();

    void onFinish();

    void onError();

}

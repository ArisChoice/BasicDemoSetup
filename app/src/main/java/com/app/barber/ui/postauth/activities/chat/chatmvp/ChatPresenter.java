package com.app.barber.ui.postauth.activities.chat.chatmvp;

import android.content.Context;
import android.util.Log;

import com.app.barber.core.BarberApplication;
import com.app.barber.core.BaseActivity;
import com.app.barber.models.request.UpdateChatDialogRequest;
import com.app.barber.models.response.BaseResponse;
import com.app.barber.models.response.ChatUsersResponseModel;
import com.app.barber.models.response.LoginResponseModel;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.net.RestCallback;
import com.app.barber.net.RestProcess;
import com.app.barber.net.RestService;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.PreferenceManager;
import com.app.barber.util.di.DaggerValues;
import com.app.barber.util.mvp.BasePresenter;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 26/10/18.
 */

public class ChatPresenter extends BasePresenter<ChatMVPView> implements RestCallback {
    @Inject
    @Named(DaggerValues.AUTH)
    RestService appService;
    @Inject
    public PreferenceManager mPref;
    Context context;

    public ChatPresenter(Context context) {
        this.context = context;
        BarberApplication.getMyComponent(context).inject(this);
    }

    @Override
    public void attachView(ChatMVPView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        Log.d("on Success", " " + model.body());
        switch (serviceMode) {

            case NetworkConstatnts.RequestCode.API_GET_CHAT_PARTICIPENT:
                if (((ChatUsersResponseModel) model.body()).getStatus() == NetworkConstatnts.ResponseCode.success) {
                    getMvpView().onSuccessResponse(serviceMode, model.body());
                } else getMvpView().onfaliurResponse(serviceMode, model.body());
                break;
            case NetworkConstatnts.RequestCode.API_UPDTAE_CHAT_DIALOG:
                if (((BaseResponse) model.body()).getStatus() == NetworkConstatnts.ResponseCode.success) {

                }
                break;
        }
    }

    @Override
    public void onLogout() {

    }


    public void saveUserData(LoginResponseModel.UserBean user) {
        mPref.setUserData(new Gson().toJson(user));
        mPref.setPrefrencesBoolean(GlobalValues.KEYS.isLogedIn, true);
        Log.d(" presenter ", "" + new Gson().toJson(user));
    }


    public void getChatParticipent(int apiGetChatParticipent, boolean b) {
        Call<ChatUsersResponseModel> call = appService.getChatUsers(GlobalValues.UserTypes.CUSTOMER);
        call.enqueue(new RestProcess(apiGetChatParticipent, this, context, b));
    }

    public void updateChatDilaog(int apiUpdtaeChatDialog, UpdateChatDialogRequest cRequest, boolean b) {
        Call<BaseResponse> call = appService.updateChatDialog(cRequest);
        call.enqueue(new RestProcess(apiUpdtaeChatDialog, this, context, b));
    }
}

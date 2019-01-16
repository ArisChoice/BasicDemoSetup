package com.app.barber.core.core_mvp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.models.AvailableSlotsModel;
import com.app.barber.models.RateData;
import com.app.barber.models.request.BookPaymentRequestModel;
import com.app.barber.models.request.PreBookingRequestModel;
import com.app.barber.models.request.RatingRequestModel;
import com.app.barber.models.request.RequestBarberModel;
import com.app.barber.models.request.RequestFavUnfavModel;
import com.app.barber.models.request.SaveQbDialogRequestModel;
import com.app.barber.models.response.BarberDetialResponse;
import com.app.barber.models.response.BarberListResponseModel;
import com.app.barber.models.response.BarberRatingsResponseModel;
import com.app.barber.models.response.BaseResponse;
import com.app.barber.models.response.BookingRatingResponseModel;
import com.app.barber.models.response.BookingResponseModel;
import com.app.barber.models.response.LoginResponseModel;
import com.app.barber.models.response.PaymentResponseModel;
import com.app.barber.models.response.ResponseAvailableSlotsModel;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.net.RestCallback;
import com.app.barber.net.RestProcess;
import com.app.barber.net.RestService;
import com.app.barber.ui.postauth.activities.barber.PreBookingDetailResponse;
import com.app.barber.ui.postauth.activities.barber.barber_adapter.HorizontalListAdapter;
import com.app.barber.util.CommonUtils;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.PreferenceManager;
import com.app.barber.util.di.DaggerValues;
import com.app.barber.util.mvp.BasePresenter;
import com.app.barber.util.quickblox.utils.SharedPrefsHelper;
import com.app.barber.util.quickblox.utils.SubscribeToNotification;
import com.app.barber.util.quickblox.utils.chat.ChatHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.Performer;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.barber.util.quickblox.utils.qb.QbDialogUtils.createDialog;

/**
 * Created by harish on 26/10/18.
 */

public class CorePresenter extends BasePresenter<CoreMVPView> implements RestCallback {
    @Inject
    @Named(DaggerValues.NON_AUTH)
    RestService appService;
    @Inject
    @Named(DaggerValues.AUTH)
    RestService appServiceAuth;
    @Inject
    public PreferenceManager mPref;
    Context context;

    String TAG = CorePresenter.class.getSimpleName();

    public CorePresenter(Context context) {
        this.context = context;
        BarberApplication.getMyComponent(context).inject(this);
    }

    @Override
    public void attachView(CoreMVPView mvpView) {
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
        Log.e("on Success", " " + model.body());
        switch (serviceMode) {
            case NetworkConstatnts.RequestCode.API_SAVE_QB_DIALOG:
                if (((BaseResponse) model.body()).getStatus() == NetworkConstatnts.ResponseCode.success) {
                    getMvpView().onSuccessResponse(serviceMode, model.body());
                } else {
                    getMvpView().onfaliurResponse(serviceMode, model.body());
                }
                break;
        }

    }

    @Override
    public void onLogout() {

    }


    public void loginToChat(QBUser user) {
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()" + user.getId());

//                ProgressDialogFragment.hide(getFragmentManager());
                mPref.saveQbUser(user);

                if (SubscribeToNotification.getInstance().isSubscribeToNotification(context))
                    SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser qbUser = new QBUser();
                qbUser.setId(Integer.parseInt(String.valueOf(user.getId())));

                qbUsers.add(qbUser);
                createDialog(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
//                ProgressDialogFragment.hide(getFragmentManager());
                Log.w(TAG, "Chat login onError(): " + e);

            }
        });
    }

    public void login(LoginResponseModel.UserBean userData) {
        if (userData.getQBId() != null) {
            Performer<QBUser> ussr = QBUsers.getUser(Integer.parseInt(userData.getQBId()));
            ussr.performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    final QBUser user = qbUser;
                    user.setPassword(NetworkConstatnts.QB.GLOBAL_PASSWORD);
                    QBUsers.signIn(user).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
//                  callback.onSuccess(qbUser, bundle);
                            Log.d(TAG, "  onSuccess  " + qbUser.getId());

                            qbUser.setPassword(NetworkConstatnts.QB.GLOBAL_PASSWORD);
                            Log.d(TAG, "  onSuccess  " + qbUser.getPassword());
                            SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                            PreferenceManager.getInstance(context).saveQbUser(qbUser);

                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.d(TAG, "  onError " + e.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "  onError " + e.getLocalizedMessage());
                }
            });
        }
    }
}


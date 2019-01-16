package com.app.barber.ui.preauth.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.core.BaseActivity;
import com.app.barber.models.response.BaseResponse;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.ui.preauth.authmvp.PreAuthMVPView;
import com.app.barber.ui.preauth.authmvp.PreAuthPresenter;
import com.app.barber.util.CommonUtils;
import com.app.barber.views.CustomEditText;
import com.app.barber.views.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 22/10/18.
 */

public class ChangePasswordActivity extends BaseActivity implements PreAuthMVPView {

    @BindView(R.id.edtxt_mobile)
    CustomEditText edtxtMobile;
    @BindView(R.id.change_password_btn)
    CustomTextView changePasswordBtn;
    private PreAuthPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_change_password;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BarberApplication) getApplication()).getMyComponent(this).inject(this);

        presenter = new PreAuthPresenter(this);
        presenter.attachView(this);
    }

    private boolean validated() {
        if (CommonUtils.isEmpty(edtxtMobile)) {
            CommonUtils.getInstance(this).displayMessage(this, getString(R.string.error_phone_number));
            return false;
        } else
            return true;
    }

    @Override
    public void onSuccessResponse(int serviceMode, Object o) {
        switch (serviceMode) {
            case NetworkConstatnts.RequestCode.API_FORGET_PASS:
                BaseResponse baseResponse = (BaseResponse) o;
                if (baseResponse != null)
                    if (baseResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                        new CommonUtils().displayMessage(ChangePasswordActivity.this, baseResponse.getMessage());
                    } else
                        new CommonUtils().displayMessage(ChangePasswordActivity.this, "");
                break;
        }
    }

    @Override
    public void onfaliurResponse(int serviceMode, Object o) {

    }

    @Override
    public void showProgres() {

    }

    @Override
    public void hidePreogress() {

    }

    @Override
    public void onSuccess(Object o, int type) {

    }

    @Override
    public void onError(String localizedMessage) {

    }

    @Override
    public void onException(Exception e) {

    }

    @OnClick(R.id.change_password_btn)
    public void onClick() {
        if (validated()) {
//            presenter.forgotPassword(NetworkConstatnts.RequestCode.API_FORGET_PASS, edtxtMobile.getText().toString(), true);
        }
    }
}

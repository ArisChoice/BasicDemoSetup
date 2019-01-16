package com.app.barber.ui.postauth.activities.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.app.barber.R;
import com.app.barber.core.BaseActivity;
import com.app.barber.views.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 12/11/18.
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    CustomTextView txtTitleToolbar;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.toolbar_common)
    Toolbar toolbarCommon;
    @BindView(R.id.notification_btn_top)
    CustomTextView notificationBtnTop;
    @BindView(R.id.switch_top)
    Switch switchTop;
    @BindView(R.id.app_notification_btn)
    CustomTextView appNotificationBtn;
    @BindView(R.id.switch_app_notification)
    Switch switchAppNotification;
    @BindView(R.id.email_btn)
    CustomTextView emailBtn;
    @BindView(R.id.switch_email)
    Switch switchEmail;
    @BindView(R.id.change_password_btn)
    CustomTextView changePasswordBtn;
    @BindView(R.id.language_btn)
    CustomTextView languageBtn;

    @Override
    public int getLayoutId() {
        return R.layout.layout_setting_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtTitleToolbar.setText(R.string.str_setting);
    }


    @OnClick({R.id.back_toolbar, R.id.change_password_btn, R.id.language_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.change_password_btn:
                break;
            case R.id.language_btn:
                break;
        }
    }
}

package com.app.barber.ui.postauth.activities.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.app.barber.R;
import com.app.barber.core.BaseActivity;
import com.app.barber.views.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 20/11/18.
 */

public class ShareActivity extends BaseActivity {
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    CustomTextView txtTitleToolbar;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.toolbar_common)
    Toolbar toolbarCommon;

    @Override
    public int getLayoutId() {
        return R.layout.layout_share_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtTitleToolbar.setText(R.string.str_share_trim_check);

    }

    @OnClick({R.id.back_toolbar, R.id.img_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.img_edit:
                break;
        }
    }
}

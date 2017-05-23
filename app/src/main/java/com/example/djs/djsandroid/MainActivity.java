package com.example.djs.djsandroid;

import android.app.Dialog;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.djs.djsandroid.model.CommonClassItem;
import com.example.djs.djsandroid.utils.AppDialogManager;
import com.example.djs.djsandroid.utils.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.view_time) LinearLayout lnViewTime;
    @Nullable
    @BindView(R.id.temperature) LinearLayout lntemperature;
    @Nullable
    @BindView(R.id.setting_time) LinearLayout lnSettingTime;
    @Nullable
    @BindView(R.id.counter) LinearLayout lnCounter;
    @Nullable
    @BindView(R.id.btn) LinearLayout lnButton;
    @Nullable
    @BindView(R.id.output) LinearLayout lnOutput;

    private CustomFontTextView txtViewTime, txtTemperature, txtSettingTime, txtCounter, txtButton, txtOutput;


    private Dialog mDialog;
    private CustomFontEditText edtContent;
    private CustomFontButton btnAccept, btnCancel;
    CommonClassItem item;
    CountDownTimer Timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        txtViewTime = (CustomFontTextView) findViewById(R.id.txt_vew_time);
        txtTemperature = (CustomFontTextView) findViewById(R.id.txt_temperature);
        txtSettingTime = (CustomFontTextView) findViewById(R.id.txt_setting_time);

        item = new CommonClassItem();

        initAddDialog();
    }

    @OnClick(R.id.view_time)
    public void setLnViewTime(View view) {

    }

    @OnClick(R.id.temperature)
    public void setLntemperature(View view) {
    }

    @OnClick(R.id.setting_time)
    public void setLnSettingTime(View view) {
        showDialogAdd();
    }

    @OnClick(R.id.counter)
    public void setLnCounter(View view) {
    }

    @OnClick(R.id.btn)
    public void setLnButton(View view) {
    }

    @OnClick(R.id.output)
    public void setLnOutput(View view) {
    }

    void initAddDialog() {
        mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_add);
        edtContent = (CustomFontEditText) mDialog.findViewById(R.id.edt_content);
        btnAccept = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);

    }

    private void showDialogAdd(){
        edtContent.setText("");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  content;

                content = edtContent.getText().toString();
                 if (Validation.checkNullOrEmpty(content)) {
                    edtContent.setError("Please enter setting time!");
                } else {
                     item.setName(content);
                     mDialog.dismiss();
                     txtViewTime.setText(item.getName());
                     txtSettingTime.setText(item.getName());

                     Timer = new CountDownTimer(Integer.parseInt(edtContent.getText().toString())*1000,1000) {
                         @Override
                         public void onTick(long millisUntilFinished) {
                             txtViewTime.setText(String.valueOf(millisUntilFinished/1000));
                         }

                         @Override
                         public void onFinish() {
                             txtViewTime.setText("Hết giờ");
                         }
                     }.start();
                 }
            }
        });

        mDialog.show();
    }
}

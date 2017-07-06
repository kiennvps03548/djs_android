package com.example.djs.djsandroid;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.djs.djsandroid.adapter.TasklistAdapter;
import com.example.djs.djsandroid.model.CommonClass;
import com.example.djs.djsandroid.model.Machine;
import com.example.djs.djsandroid.utils.AppConstants;
import com.example.djs.djsandroid.utils.AppDialogManager;
import com.example.djs.djsandroid.utils.HTTPRequest;
import com.example.djs.djsandroid.utils.Validation;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private CustomFontTextView txtViewTime, txtTemperature, txtSettingTime, txtCounter, txtButton, txtOutput;


    private Dialog mDialog;
    private CustomFontEditText edtContent;
    CustomFontTextView edtTitle;
    private CustomFontButton btnAccept, btnCancel;
    private Button btn_save;
    CommonClass item;
    CountDownTimer Timer;
    MaterialDialog mDialogLoading;
    Machine machine;
    ArrayList<CommonClass> taskList;
    TasklistAdapter adapter;
    RecyclerView recyclerView;
    public IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        getSupportActionBar().hide();
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        qrScan = new IntentIntegrator(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(this);
        LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        btn_save = (Button) findViewById(R.id.btn_save);
        taskList = new ArrayList<>();

        adapter = new TasklistAdapter(this, taskList, new TasklistAdapter.NavigationClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showDialogAdd(position);
            }
        });
        recyclerView.setAdapter(adapter);
        //ShowDialogChoice("Please input machine id");
        initAddDialog();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });
        test();
    }

    private void test(){
        String output = "{\"PLN_DATE\":\"2017-05-28\",\"MCHN_ID\":\"M10026925\",\"DLY_SEQ\":1,\"MCHN_MST_CD\":\"M000000000353\",\"FCTR_CD\":null,\"LINE_CD\":null,\"STYL_NM\":\"STYLE-01\",\"PRCS_NM\":\"STYLE-01\",\"SEAT_SEQ\":\"1\",\"TRGT_QTY\":50,\"OPRT_EMP_NO\":\"22151354\",\"SENT_YN\":\"N\",\"MCHN_IP_ADDR\":\"192.168.25.226:9999\",\"MCHN_MAC_ADDR\":\"b8:27:eb:31:c6:b5\",\"TYPE_CD\":\"S04\",\"TYPE_NM\":\"S04\",\"NAME_VALUES\":[{\"Name\":\"TARGET\",\"Value\":\"50\"},{\"Name\":\"TEMPERATURE\",\"Value\":\"50\"},{\"Name\":\"SPEED\",\"Value\":\"50\"},{\"Name\":\"WIDTH\",\"Value\":\"0.5\"}]}";
        try {

            JSONObject object = new JSONObject(output);
            JSONArray array = object.getJSONArray("NAME_VALUES");
            if(array.length()<=0){
                ShowDialogChoice("Machine have no setting! Please try another machine");
                return;
            }
            machine = new Machine();
            machine.setMCHN_ID(object.getString("MCHN_ID"));
            machine.setMCHN_IP_ADDR(object.getString("MCHN_IP_ADDR"));
            for(int i=0; i<array.length(); i++){
                CommonClass c = new CommonClass();
                c.setId(array.getJSONObject(i).getString("Name"));
                c.setName(array.getJSONObject(i).getString("Value"));
                taskList.add(c);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            if(machine!=null)
                AppDialogManager.ShowDialogError(MainActivity.this, "Server error", "Please try again!");
            else ShowDialogChoice("An error has occurred!");
            Log.d(AppConstants.TAG, "Error while parsing json: "+e.toString());
        }
    }

    private void saveSetting(){
        JSONObject object = new JSONObject();
        try {
            object.put("MCHN_ID", machine.getMCHN_ID());
            object.put("MCHN_IP_ADDR", machine.getMCHN_IP_ADDR());
            JSONArray array = new JSONArray();
            for (int i=0; i<taskList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("Name", taskList.get(i).getId());
                obj.put("Value", taskList.get(i).getName());
                array.put(obj);
            }
            object.put("J_NAME_VALUES", array);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d(AppConstants.TAG, output);
                    try {
                        JSONObject res = new JSONObject(output);
                        if(res.getString("infor").equalsIgnoreCase("OK")){
                            Toast.makeText(MainActivity.this, "Save setting successfully!", Toast.LENGTH_LONG).show();
                        }else {
                            AppDialogManager.ShowDialogError(MainActivity.this, "Save setting error", "Please try again!");
                        }
                    } catch (JSONException e) {
                        Log.e(AppConstants.TAG, "Error json "+e.toString());
                    }
                }
            }, this).execute(AppConstants.URL_POST_TASK, object.toString());
        } catch (JSONException e) {
            Log.e(AppConstants.TAG, "Error json "+e.toString());
            AppDialogManager.ShowDialogError(MainActivity.this, "Server error", "Please try again!");
        }
    }

    void ShowDialogChoice(String title) {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_choice);
        final AppCompatImageView choice1 = (AppCompatImageView) mDialog.findViewById(R.id.btn_choice1);
        LinearLayout choice2 = (LinearLayout) mDialog.findViewById(R.id.layout_scan);
        final CustomFontEditText txt = (CustomFontEditText) mDialog.findViewById(R.id.edt_content);
        final CustomFontTextView txt_title = (CustomFontTextView) mDialog.findViewById(R.id.textView_titles);
        AppCompatImageView img_close = (AppCompatImageView) mDialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        txt_title.setText(title);
        choice1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if(!Validation.checkNullOrEmpty(txt.getText().toString())) {
                    getTaskList(txt.getText().toString());
                    mDialog.dismiss();
                }
                else txt.setError("Please enter machine id");
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mDialogLoading.show();
                qrScan.initiateScan();
            }
        });
        mDialog.show();
    }


    private void getTaskList(String id){
        mDialogLoading.show();
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.d(AppConstants.TAG, output);
                mDialogLoading.dismiss();
                try {

                    JSONObject object = new JSONObject(output);
                    JSONArray array = object.getJSONArray("NAME_VALUES");
                    if(array.length()<=0){
                        ShowDialogChoice("Machine have no setting! Please try another machine");
                        return;
                    }
                    machine = new Machine();
                    machine.setMCHN_ID(object.getString("MCHN_ID"));
                    machine.setMCHN_IP_ADDR(object.getString("MCHN_IP_ADDR"));
                    for(int i=0; i<array.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(array.getJSONObject(i).getString("Name"));
                        c.setName(array.getJSONObject(i).getString("Value"));
                        taskList.add(c);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    if(machine!=null)
                        AppDialogManager.ShowDialogError(MainActivity.this, "Server error", "Please try again!");
                    else ShowDialogChoice("An error has occurred!");
                    Log.d(AppConstants.TAG, "Error while parsing json: "+e.toString());
                }
            }
        }, this).execute(AppConstants.URL_GET_TASK+id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        mDialogLoading.dismiss();
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                ShowDialogChoice("Machine not found!");
            } else {
                getTaskList(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    void initAddDialog() {
        mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_add);
        edtContent = (CustomFontEditText) mDialog.findViewById(R.id.edt_content);
        edtTitle = (CustomFontTextView) mDialog.findViewById(R.id.textView_titles);
        btnAccept = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);

    }

    private void showDialogAdd(final int position){
        edtTitle.setText(taskList.get(position).getId());
        edtContent.setText(taskList.get(position).getName());
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  content;
                content = edtContent.getText().toString();
                if (Validation.checkNullOrEmpty(content)) {
                    edtContent.setError("Field can not null!");
                } else {
                    taskList.get(position).setName(content);
                    mDialog.dismiss();
                    adapter.notifyItemChanged(position);

                 }
            }
        });

        mDialog.show();
    }
}

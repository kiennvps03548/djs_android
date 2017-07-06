package com.example.djs.djsandroid.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.djs.djsandroid.R;

/**
 * Created by kien on 29-Aug-16.
 */

public class AppDialogManager {
    private DialogAcceptClickListener mClick;
    private static String titles;
    private static String content;
    private static String button;
    private static String buttonYes;
    private static String buttonNo;
    private static DialogAcceptClickListener mClickCancel;

    public static String getButton() {
        return button;
    }

    public static void setButton(String button) {
        AppDialogManager.button = button;
    }

    public DialogAcceptClickListener getmClick() {
        return mClick;
    }

    public void setmClick(DialogAcceptClickListener mClick) {
        this.mClick = mClick;
    }

    public static String getTitles() {
        return titles;
    }

    public static void setTitles(String titles) {
        AppDialogManager.titles = titles;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        AppDialogManager.content = content;
    }

    public static Dialog onShowCustomDialog(final Context context, int layoutId){
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        CustomFontButton btnDenice = (CustomFontButton) view.findViewById(R.id.btn_cancel);
        if(btnDenice!=null) {
            btnDenice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        AppCompatImageView img_close = (AppCompatImageView) view.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog onShowCustomDialogCallback(final Context context, int layoutId, final DialogAcceptClickListener listener){
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        CustomFontButton btnDenice = (CustomFontButton) view.findViewById(R.id.btn_cancel);
        CustomFontButton btnAccept = (CustomFontButton) view.findViewById(R.id.btn_accept);
        if(btnDenice!=null) {
            btnDenice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCloseClick(v);
                    dialog.dismiss();
                }
            });
        }
        if(btnAccept!=null){
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAcceptClick(v);
                }
            });
        }

        AppCompatImageView img_close = (AppCompatImageView) view.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog ShowDialogError(Context context, String title, String message) {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(context, R.layout.dialog_error);
        CustomFontTextView txt = (CustomFontTextView) mDialog.findViewById(R.id.txt_content1);
        CustomFontTextView txt1 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        CustomFontButton btn = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        txt.setText(title);
        txt1.setText(message);
        mDialog.show();
        return mDialog;
    }

    public static MaterialDialog onCreateDialogLoading(Context context) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.content("Loading").progress(true, 0).cancelable(false);
        MaterialDialog mDialog = mBuilder.build();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        return mDialog;
    }

}

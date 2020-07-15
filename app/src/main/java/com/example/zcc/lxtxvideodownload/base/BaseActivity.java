package com.example.zcc.lxtxvideodownload.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.zcc.lxtxvideodownload.base.utils.UltimateBar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pbrx.mylib.base.LibBaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * Created by Iverson on 2017/10/10 下午3:41
 * 此类用于：
 */

public abstract class BaseActivity extends LibBaseActivity {
    protected Unbinder mBinder;
    private KProgressHUD hud;
    public Activity activity;

    private Dialog reloginDialog;
    protected boolean flag;//是否使用多图和照相机选择

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //给自定义显示界面 增添第四种界面 即正常界面
        mBinder = ButterKnife.bind(this, rootView);
        activity = this;
        findView();
        initView();
//        if(StateBarUtils.checkDeviceHasNavigationBar(mContext)){
//            ViewGroup.MarginLayoutParams marginLayoutParams= (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
//            marginLayoutParams.bottomMargin= StateBarUtils.getNavigationBarHeight(mContext);
//            rootView.setLayoutParams(marginLayoutParams);
//        }

    }

    protected void createLayoutManger(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }



    protected void createLayoutMangerHorizontal(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void toIntent(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void toIntent(Class cls, String key, String value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    protected void toIntent(Class cls, String key, String value, String key2, String value2) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        startActivity(intent);
    }



    @Override
    public void showLoadingComplete() {
        super.showLoadingComplete();

    }
    /**
     *  3.沉浸式状态栏和导航栏：在 onCreate() 方法中：
     * @param isApplyNav
     */
    public void setStatueBar(boolean isApplyNav){
        UltimateBar.newImmersionBuilder()
                .applyNav(false)    // 是否应用到导航栏
                .build(this)
                .apply();
    }


    @Override
    protected void goLogin() {
//        showRelogin();
    }

//    public void showRelogin() {
//        if (reloginDialog == null) {
//            reloginDialog = getDialog.getHintDialog2(activity, "提示", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finishAllActivity();
//                    SPUtils.remove(LibConstant.token);
//                    goActivity(LoginActivity.class);
//                }
//            }, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SPUtils.remove(LibConstant.token);
//                    finishAllActivity();
//                }
//            }, "登录已过期，请重新登录", "重新登录", "取消");
//            reloginDialog.setCancelable(false);
//            reloginDialog.setCanceledOnTouchOutside(false);
//        }
//        if (!reloginDialog.isShowing()) {
//            reloginDialog.show();
//        }
//    }

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示等待框
     */
    public void showLoading() {
        showLoading("请稍候...");
    }

    /**
     * 显示等待框
     *
     * @param loading
     */
    public void showLoading(String loading) {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("      " + loading + "      ")
                .setMaxProgress(100)
                .show();
    }

    @Override
    public void showDataException(String msg) {
        super.showDataException(msg);
        hideLoading();
    }

    /**
     * 隐藏等待框
     */
    public void hideLoading() {
        if (hud == null) {
            return;
        }
        if (hud.isShowing()) {
            if (this.getWindow() != null) {
                hud.dismiss();
            }
        }
    }



    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }
        super.onDestroy();
    }

    public void goActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}

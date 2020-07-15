package com.example.zcc.lxtxvideodownload;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.zcc.lxtxvideodownload.base.BaseActivity;
import com.example.zcc.lxtxvideodownload.file.FileFragment;
import com.example.zcc.lxtxvideodownload.home.HomeFragment;
import com.example.zcc.lxtxvideodownload.setting.SettingFragment;

import java.lang.reflect.Field;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private Fragment curFragment;
    private HomeFragment indexFragment;
    private FileFragment mFileFragment;
    private SettingFragment mSettingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected void findView() {
        indexFragment=new HomeFragment();
//        mFileFragment=new FileFragment();
//        mSettingFragment=new SettingFragment();
//        mNavigation.setItemIconTintList(null);
//        mNavigation.setSelectedItemId(R.id.navigation_index);
//        disableShiftMode(mNavigation);
//        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchFragment(curFragment, indexFragment);
        setStatueBar(false);
    }

    @Override
    protected void initView() {

    }
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            resetToDefaultIcon();
//            switch (item.getItemId()) {
//                case R.id.navigation_index:
//                    item.setIcon(R.drawable.home);
//                    switchFragment(curFragment, indexFragment);
//                    return true;
//                case R.id.navigation_file:
//                    item.setIcon(R.drawable.file);
//                    switchFragment(curFragment, mFileFragment);
//                    return true;
//                case R.id.navigation_setting:
//                    item.setIcon(R.drawable.setting);
//                    switchFragment(curFragment, mSettingFragment);
//
//                    return true;
//
//            }
//            return false;
//        }
//    };

//    private void resetToDefaultIcon() {
//        MenuItem index = mNavigation.getMenu().findItem(R.id.navigation_index);
//        MenuItem message = mNavigation.getMenu().findItem(R.id.navigation_file);
//        MenuItem msg = mNavigation.getMenu().findItem(R.id.navigation_setting);
//        index.setIcon(R.drawable.home);
//        message.setIcon(R.drawable.file);
//        msg.setIcon(R.drawable.setting);
//    }

    private void switchFragment(Fragment from, Fragment to) {
        if (null == from) {
            curFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.add(R.id.frameLayout, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        } else if (curFragment != to) {
            curFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.frameLayout, to).commitAllowingStateLoss();
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss();
            }
        }
//        curFragment = fragment;
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }
    @SuppressLint("RestrictedApi")
    public  void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}

package com.firefly.faceEngine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.firefly.api.HardwareCtrl;
import com.firefly.faceEngine.dblib.SaveInfo;
import com.firefly.faceEngine.dblib.bean.Setting;
import com.firefly.faceEngine.utils.Tools;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.i("生命周期","onCreate");
        super.onCreate(savedInstanceState);
        context = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (enableBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            showActionBarTitle();
        }
    }

    protected boolean enableBack() {
        return true;
    }

    protected static final int IMAGE_PICKER_ONE = 100;
    public void showShortToast(String content) {
        Tools.toast(content);
    }
    public void showShortToast(int resId) {
        Tools.toast(resId);
    }

    protected void setActionBarTitle(@StringRes int var1){
        setActionBarTitle(getString(var1));
    }

    protected void showActionBarTitle(){
        try {
            if (getIntent().getExtras() != null) {
                String title = getIntent().getExtras().getString("title", "");
                if (!TextUtils.isEmpty(title)) {
                    setActionBarTitle(title);
                }
            }
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    protected void setActionBarTitle(CharSequence title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //是否支持红外补光灯
    protected boolean isSupportInfraredFillLight() {
        try {
            return HardwareCtrl.isSupportInfraredFillLight();
        } catch (Exception e) {
            Tools.printStackTrace(e);
            return false;
        }
    }

    //打开/关闭 红外补光灯
    protected void setInfraredFillLight(boolean enable) {
        try {

            List<Setting> saveInformation = SaveInfo.getSaveInformation(context);
            Integer white = saveInformation.get(0).getRed();
            Integer red = saveInformation.get(0).getInfrared();
            Boolean whiteFlag,redFlag,flag;
            Integer recognition = saveInformation.get(0).getRecognition();

            if (white==0)
                whiteFlag = false;
            else
                whiteFlag = true;
            if (red == 0)
                redFlag = false;
            else
                redFlag = true;
            if (recognition == 0)
                flag = false;
            else
                flag = true;


            Tools.debugLog("isSupportInfraredFillLight = %s", isSupportInfraredFillLight());
            HardwareCtrl.setInfraredFillLight(flag);

            Integer brightness = saveInformation.get(0).getBrightness();

            HardwareCtrl.ctrlLedWhite(whiteFlag&&flag, (brightness - 0) / 2);
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }

    protected void setLightLight(boolean enable, int l){
        try {
//            int maxValue = HardwareCtrl.getFillLightBrightnessMax();
//            int minValue = HardwareCtrl.getFillLightBrightnessMin();
            HardwareCtrl.ctrlLedWhite(enable, l);
        } catch (Exception e) {
            Tools.printStackTrace(e);
        }
    }
}

package com.tignioj.freezeapp.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.tignioj.freezeapp.MyDeviceAdminReceiver;
import com.tignioj.freezeapp.config.MyConfig;

/**
 * 来源：https://www.jianshu.com/p/8934d47aed3b
 * 感谢作者 Javen205 提供封装
 * 侵删
 */
public class DeviceMethod {
    private static DeviceMethod mDeviceMethod;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Context mContext;



    private DeviceMethod(Context context) {
        mContext = context;
        //获取设备管理服务
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
    }

    public static DeviceMethod getInstance(Context context) {
        if (mDeviceMethod == null) {
            synchronized (DeviceMethod.class) {
                if (mDeviceMethod == null) {
                    mDeviceMethod = new DeviceMethod(context);
                }
            }
        }
        return mDeviceMethod;
    }

    // 激活程序
    public void onActivate() {
        Toast.makeText(mContext, "激活", Toast.LENGTH_SHORT).show();
        //判断是否激活  如果没有就启动激活设备
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "提示文字");
            mContext.startActivity(intent);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 移除程序 如果不移除程序 APP无法被卸载
     */
    public void onRemoveActivate() {
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    /**
     * 设置解锁方式 不需要激活就可以运行
     */
    public void startLockMethod() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        mContext.startActivity(intent);
    }

    /**
     * 设置解锁方式
     */
    public void setLockMethod() {
        // PASSWORD_QUALITY_ALPHABETIC
        // 用户输入的密码必须要有字母（或者其他字符）。
        // PASSWORD_QUALITY_ALPHANUMERIC
        // 用户输入的密码必须要有字母和数字。
        // PASSWORD_QUALITY_NUMERIC
        // 用户输入的密码必须要有数字
        // PASSWORD_QUALITY_SOMETHING
        // 由设计人员决定的。
        // PASSWORD_QUALITY_UNSPECIFIED
        // 对密码没有要求。
        if (devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            devicePolicyManager.setPasswordQuality(componentName,
                    DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
            mContext.startActivity(intent);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 立刻锁屏
     */
    public void lockNow() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Inform.showError("请先激活设备");
        }
    }


    /**
     * 设置多长时间后锁屏
     *
     * @param time
     */
    public void LockByTime(long time) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, time);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 恢复出厂设置
     */
    public void WipeData() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 设置密码锁
     *
     * @param password
     */
    public void setPassword(String password) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(password,
                    DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 冻结App
     *
     * @param packageName
     * @param isFreeze
     */
    public void freeze(String packageName, boolean isFreeze) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d(MyConfig.MY_TAG, "freeze:" + isFreeze + " " + packageName);
                devicePolicyManager.setApplicationHidden(componentName, packageName, isFreeze);
            }
        } else {
            Inform.showError("请先激活设备");
        }
    }



    /**
     * 检测App是否隐藏
     * @param packageName
     * @return
     */
    public boolean isHidden(String packageName) {
        boolean b = false;
        if (devicePolicyManager.isAdminActive(componentName)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                b = devicePolicyManager.isApplicationHidden(componentName, packageName);
            }
        } else {
            Inform.showError("请先激活设备");
        }
        return b;
    }


    public boolean isAdmin() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return devicePolicyManager;
    }
}
package com.example.freezeappdemo1.ui.home.applist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.freezeappdemo1.MainActivity;
import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;

import java.util.List;

public class AppListAdapter extends ArrayAdapter<AppInfo> {

    private List<AppInfo> appInfos;
    private HomeViewModel homeViewModel;
    private int resourceLayout;
    private Context context;
    private FragmentActivity fragmentActivity;

    public AppListAdapter(@NonNull Context context, int resource, List<AppInfo> appInfos,
                          HomeViewModel homeViewModel,
                          FragmentActivity fragmentActivity
    ) {

        super(context, resource);
        this.resourceLayout = resource;
        this.context = context;
        this.appInfos = appInfos;
        this.homeViewModel = homeViewModel;
        this.fragmentActivity = fragmentActivity;
    }

    public void updateInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, resourceLayout, null);
        } else {
            view = convertView;
        }
        AppInfo item = getItem(position);

        CheckBox checkBox = view.findViewById(R.id.checkbox_cell);
        final TextView appName = view.findViewById(R.id.tv_appname);
        TextView packageName = view.findViewById(R.id.tv_packagename);
        ImageView imageView = view.findViewById(R.id.imageView);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem(position).setSelected(isChecked);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(MyConfig.MY_TAG, "new Thread" + Thread.currentThread().getId());

                        int i = 0;
                        for (AppInfo a : appInfos) {
                            if (a.isSelected()) {
                                i++;
                            }
                        }
                        final int finalI = i;
                        fragmentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                homeViewModel.setSelectedReadyToFreezeCount(finalI);
                            }
                        });
                    }
                }).start();
            }
        });
        checkBox.setChecked(item.isSelected());
        appName.setText(item.getAppName());
        packageName.setText(item.getPackageName());
        imageView.setImageDrawable(item.getIcon());
        return view;
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }
}

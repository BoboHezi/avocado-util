package com.eli.avocado_util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        RecyclerView activitiesRv = findViewById(R.id.activities_rv);

        PackageManager pm = getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("elifli", "NameNotFoundException: " + e);
        }
        if (pi != null) {
            ActivityInfo[] activities = new ActivityInfo[pi.activities.length - 1];
            int index = 0;
            for (ActivityInfo ai : pi.activities) {
                if (!new ComponentName(ai.packageName, ai.name).equals(getComponentName())) {
                    activities[index] = ai;
                    index ++;
                }
            }
            activitiesRv.setLayoutManager(new LinearLayoutManager(this));
            activitiesRv.setAdapter(new ActivitiesAdapter(this, activities, pm));
            activitiesRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
    }

    class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityHolder> {

        private ActivityInfo[] activities;

        private Context context;

        private PackageManager pm;

        public ActivitiesAdapter(Context context, ActivityInfo[] activities, PackageManager pm) {
            this.activities = activities;
            this.context = context;
            this.pm = pm;
        }

        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ActivityHolder(LayoutInflater.from(context).inflate(R.layout.activities_holder, parent, false));
        }

        @Override
        public int getItemCount() {
            return activities != null ? activities.length : 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            final ActivityInfo ai = activities[position];
            final ComponentName pn = new ComponentName(ai.packageName, ai.name);

            holder.icon.setImageDrawable(ai.loadIcon(pm));
            holder.label.setText(ai.loadLabel(pm));
            holder.className.setText(pn.flattenToShortString());

            holder.itemView.setOnClickListener(v -> startActivity(new Intent().setComponent(pn)));
        }

        class ActivityHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView label;
            TextView className;

            public ActivityHolder(View itemView) {
                super(itemView);

                icon = itemView.findViewById(R.id.activity_icon);
                label = itemView.findViewById(R.id.activity_label);
                className = itemView.findViewById(R.id.activity_class);
            }
        }
    }
}

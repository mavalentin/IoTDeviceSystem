package it.manuelvalentin.iotdevicemanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenai.jffi.Main;
import com.squareup.picasso.Picasso;

import it.manuelvalentin.iotdevicemanager.AppFragment.OnListFragmentInteractionListener;

import java.math.BigInteger;
import java.util.ArrayList;

import static it.manuelvalentin.iotdevicemanager.AppsActivity.instanceData;


public class MyAppRecyclerViewAdapter extends RecyclerView.Adapter<MyAppRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<InstanceData.App> apps;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;
    private final Activity activity;
    private InstanceData.App selectedItem;
    private String type;

    public MyAppRecyclerViewAdapter(ArrayList<InstanceData.App> apps, String type, OnListFragmentInteractionListener listener, Context context, Activity activity) {
        this.apps = apps;
        mListener = listener;
        this.context = context;
        this.activity = activity;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.app = apps.get(position);
        Picasso.get().load(holder.app.getServerAddress() + "appicon.png").into(holder.mIconView);
        holder.mContentView.setText(holder.app.getAppName());
        holder.update();
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mIconView;
        final TextView mContentView;
        public InstanceData.App app;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIconView = view.findViewById(R.id.appIcon);
            mContentView = view.findViewById(R.id.content);
        }

        void update() {
            //selected = false;
            mView.setBackgroundColor(Color.WHITE);
            ((AppsActivity)activity).getSupportActionBar().show();
            mView.setOnClickListener(v -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //selected = true;
                    mView.setBackgroundColor(Color.LTGRAY);
                    selectedItem = app;
                    ((AppCompatActivity)mView.getContext()).startSupportActionMode(actionModeCallbacks);
                    ((AppsActivity)activity).getSupportActionBar().hide();
                    //mListener.onListFragmentInteraction(this);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.app_menu, menu);

            if(instanceData.getBlockchainHandler().getLoginStatus().getRole().equals("user")) {
                menu.findItem(R.id.install).setVisible(false);
                menu.findItem(R.id.uninstall).setVisible(false);
                menu.findItem(R.id.restart_container).setVisible(false);
            }
            if(instanceData.getBlockchainHandler().getLoginStatus().getRole().equals("technician")) {
                menu.findItem(R.id.register_to_app).setVisible(false);
                menu.findItem(R.id.unregister_from_app).setVisible(false);
                menu.findItem(R.id.access_app).setVisible(false);
            }
            if(instanceData.getBlockchainHandler().getLoginStatus().getRole().equals("developer")) {
                menu.findItem(R.id.register_to_app).setVisible(false);
                menu.findItem(R.id.unregister_from_app).setVisible(false);
                menu.findItem(R.id.access_app).setVisible(false);
                menu.findItem(R.id.restart_container).setVisible(false);
            }
            if(type.equals("installed")) menu.findItem(R.id.install).setVisible(false);
            if(type.equals("installable")) menu.findItem(R.id.uninstall).setVisible(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()) {
                case R.id.install:
                    MainActivity.instanceData.getDeviceHandler().deployApp(selectedItem);
                    break;
                case R.id.uninstall:
                    MainActivity.instanceData.getDeviceHandler().uninstallApp(selectedItem);
                    break;
                case R.id.register_to_app:
                    Log.d("start aregister", System.currentTimeMillis() + " ms");
                    MainActivity.instanceData.getBlockchainHandler().activateSubscription(new BigInteger(selectedItem.getAppGuid(), 16));
                    break;
                case R.id.unregister_from_app:
                    Log.d("start aunregister", System.currentTimeMillis() + " ms");
                    MainActivity.instanceData.getBlockchainHandler().deactivateSubscription(new BigInteger(selectedItem.getAppGuid(), 16));
                    break;
                case R.id.access_app:
                    MainActivity.instanceData.getDeviceHandler().accessApp(selectedItem);
                    break;
                case R.id.restart_container:
                    MainActivity.instanceData.getDeviceHandler().restartContainer(selectedItem);
                    break;
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectedItem = null;
            notifyDataSetChanged();
        }
    };
}

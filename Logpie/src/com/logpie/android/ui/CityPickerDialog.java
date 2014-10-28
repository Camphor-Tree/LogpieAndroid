package com.logpie.android.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;

import com.logpie.android.R;
import com.logpie.android.logic.CityManager;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.util.LogpieLog;

public class CityPickerDialog extends DialogFragment
{
    private static final String TAG = CityPickerDialog.class.getName();
    public static final String KEY_CITY_ID = "city_id";
    public static final String KEY_CITY = "city";

    private ExpandableListView mListView;
    private SimpleExpandableListAdapter mAdapter;
    private List<Map<String, String>> mGroupData;
    private List<List<Map<String, String>>> mChildData;
    private CityManager mCityManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCityManager = CityManager.getInstance(getActivity());
        mGroupData = mCityManager.getProvinceList();
        mChildData = mCityManager.getCityList();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(LanguageHelper.getId(LanguageHelper.KEY_CITY_PICKER_TITLE, getActivity()));
        builder.setNegativeButton(
                LanguageHelper.getString(LanguageHelper.KEY_BUTTON_CANCEL, getActivity()),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_expandable_list, null);

        mListView = (ExpandableListView) v.findViewById(R.id.list);

        // set group indicator
        TypedArray expandableListViewStyle = getActivity().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.expandableListViewStyle });
        TypedArray groupIndicator = getActivity().getTheme().obtainStyledAttributes(
                expandableListViewStyle.getResourceId(0, 0),
                new int[] { android.R.attr.groupIndicator });
        mListView.setGroupIndicator(groupIndicator.getDrawable(0));
        expandableListViewStyle.recycle();
        groupIndicator.recycle();

        mAdapter = new SimpleExpandableListAdapter(getActivity(), mGroupData,
                R.layout.layout_expandable_list_group,
                new String[] { CityManager.KEY_PROVINCE_STRING },
                new int[] { R.id.expandable_list_group_text }, mChildData,
                R.layout.layout_expandable_list_child, new String[] {
                        CityManager.KEY_PROVINCE_STRING, CityManager.KEY_CITY_STRING }, new int[] {
                        R.id.expandable_list_group_text, R.id.expandable_list_child_text });
        mListView.setAdapter(mAdapter);

        mListView.setOnChildClickListener(new OnChildClickListener()
        {
            @SuppressWarnings("unchecked")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id)
            {
                v.setBackgroundColor(getResources().getColor(R.color.LightGrey));

                HashMap<String, String> hs = (HashMap<String, String>) parent
                        .getExpandableListAdapter().getChild(groupPosition, childPosition);
                String cityID = hs.get(CityManager.KEY_CITY_ID);
                String city = hs.get(CityManager.KEY_CITY_STRING);
                if (city == null)
                {
                    LogpieLog.e(TAG, "city data is null.");
                    sendResult(Activity.RESULT_CANCELED, cityID, city);
                }
                else
                {
                    LogpieLog.d("CityPickerDialog", "child city is: " + city);
                    sendResult(Activity.RESULT_OK, cityID, city);
                }
                getDialog().dismiss();
                return true;
            }
        });

        builder.setView(v);

        return builder.create();
    }

    private void sendResult(int resultCode, String cityID, String city)
    {
        if (getTargetFragment() == null)
        {
            LogpieLog.d(TAG, "target fragment is null.");
            return;
        }

        Intent i = new Intent();
        i.putExtra(KEY_CITY_ID, cityID);
        i.putExtra(KEY_CITY, city);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static void setupCityPicker(final View view, final Fragment targetFragment,
            final FragmentActivity activity)
    {
        view.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                FragmentManager fm = activity.getSupportFragmentManager();

                DialogFragment dialog = new CityPickerDialog();
                dialog.setTargetFragment(targetFragment,
                        LogpieDialogHelper.REQUEST_CODE_CITY_DIALOG);
                dialog.show(fm, LogpieDialogHelper.KEY_CITY_PICKER_DIALOG);
            }
        });
    }
}

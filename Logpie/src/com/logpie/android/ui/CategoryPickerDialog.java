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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SimpleExpandableListAdapter;

import com.logpie.android.R;
import com.logpie.android.logic.CategoryManager;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.util.LogpieLog;

public class CategoryPickerDialog extends DialogFragment
{
    private static final String TAG = CategoryPickerDialog.class.getName();
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_SUBCATEGORY_ID = "subcategory_id";
    public static final String KEY_CATEGORY_STRING = "category_string";
    public static final String KEY_SUBCATEGORY_STRING = "subcategory_string";

    private ExpandableListView mListView;
    private SimpleExpandableListAdapter mAdapter;
    private List<Map<String, String>> mGroupData;
    private List<List<Map<String, String>>> mChildData;
    private CategoryManager mCategoryManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCategoryManager = CategoryManager.getInstance(getActivity(), LanguageHelper.sIsChinese);
        mGroupData = mCategoryManager.getCategoryList();
        mChildData = mCategoryManager.getSubcategoryList();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(LanguageHelper.getId(LanguageHelper.KEY_CATEGORY_PICKER_TITLE,
                getActivity()));
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
                R.layout.layout_expandable_list_group, new String[] { LanguageHelper.getString(
                        LanguageHelper.KEY_CATEGORY_GROUP, getActivity()) },
                new int[] { R.id.expandable_list_group_text }, mChildData,
                R.layout.layout_expandable_list_child, new String[] {
                        LanguageHelper.getString(LanguageHelper.KEY_CATEGORY_GROUP, getActivity()),
                        LanguageHelper.getString(LanguageHelper.KEY_SUBCATEGORY_GROUP,
                                getActivity()) }, new int[] { R.id.expandable_list_group_text,
                        R.id.expandable_list_child_text });
        mListView.setAdapter(mAdapter);

        mListView.setOnGroupClickListener(new OnGroupClickListener()
        {

            @SuppressWarnings("unchecked")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                    long id)
            {
                if (groupPosition == (mGroupData.size() - 1))
                {
                    v.setBackgroundColor(getResources().getColor(R.color.LightGrey));
                    HashMap<String, String> hs_c = (HashMap<String, String>) parent
                            .getExpandableListAdapter().getGroup(groupPosition);
                    String categoryID = hs_c.get(CategoryManager.KEY_CATEGORY_ID);
                    String category = hs_c.get(LanguageHelper.getString(
                            LanguageHelper.KEY_CATEGORY_GROUP, getActivity()));
                    if (categoryID == null || category == null)
                    {
                        LogpieLog.e(TAG, "category data is null.");
                        sendResult(Activity.RESULT_CANCELED, categoryID, category, null, null);
                    }
                    else
                    {
                        LogpieLog.d(TAG, "get category: " + category);
                        sendResult(Activity.RESULT_OK, categoryID, category, null, null);
                    }
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }

        });

        mListView.setOnChildClickListener(new OnChildClickListener()
        {
            @SuppressWarnings("unchecked")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id)
            {
                v.setBackgroundColor(getResources().getColor(R.color.LightGrey));
                HashMap<String, String> hs_c = (HashMap<String, String>) parent
                        .getExpandableListAdapter().getGroup(groupPosition);
                HashMap<String, String> hs_s = (HashMap<String, String>) parent
                        .getExpandableListAdapter().getChild(groupPosition, childPosition);

                String categoryID = hs_c.get(CategoryManager.KEY_CATEGORY_ID);
                String category = hs_c.get(LanguageHelper.getString(
                        LanguageHelper.KEY_CATEGORY_GROUP, getActivity()));
                String subcategoryID = hs_s.get(CategoryManager.KEY_SUBCATEGORY_ID);
                String subcategory = hs_s.get(LanguageHelper.getString(
                        LanguageHelper.KEY_SUBCATEGORY_GROUP, getActivity()));
                if (categoryID == null || category == null || subcategoryID == null
                        || subcategory == null)
                {
                    LogpieLog.e(TAG, "category/subcategory data is null.");
                    sendResult(Activity.RESULT_CANCELED, categoryID, category, subcategoryID,
                            subcategory);
                }
                else
                {
                    LogpieLog.d(TAG, "get category: " + category + " subcategory: " + subcategory);
                    sendResult(Activity.RESULT_OK, categoryID, category, subcategoryID, subcategory);
                }
                getDialog().dismiss();
                return true;
            }
        });

        builder.setView(v);

        return builder.create();
    }

    private void sendResult(int resultCode, String categoryID, String category,
            String subcategoryID, String subcategory)
    {
        if (getTargetFragment() == null)
        {
            LogpieLog.d(TAG, "target fragment is null.");
            return;
        }

        Intent i = new Intent();
        i.putExtra(KEY_CATEGORY_ID, categoryID);
        i.putExtra(KEY_SUBCATEGORY_ID, subcategoryID);
        i.putExtra(KEY_CATEGORY_STRING, category);
        i.putExtra(KEY_SUBCATEGORY_STRING, subcategory);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}

package com.funenc.eticket;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView, View container) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeightDp = 0;

        totalHeightDp = 88 * listAdapter.getCount();

        ViewGroup.LayoutParams params = container.getLayoutParams();

        Resources r = Resources.getSystem();

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48 + totalHeightDp + 8, r.getDisplayMetrics());
        params.height =  (listView.getDividerHeight() * (listAdapter.getCount() - 1))+ (int)px;

        container.setLayoutParams(params);
        container.requestLayout();
    }
}

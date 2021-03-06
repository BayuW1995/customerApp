package id.net.gmedia.gmedialiveconnection.utils;

import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

public class ScrollToTopListview {
	public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
		View child = getChildAtPosition(view, position);
		// There's no need to scroll if child is already at top or view is already scrolled to its end
		if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
			return;
		}

		view.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(final AbsListView view, final int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					view.setOnScrollListener(null);

					// Fix for scrolling bug
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							view.setSelection(position);
						}
					});
				}
			}

			@Override
			public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
								 final int totalItemCount) {
			}
		});

		// Perform scrolling to position
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				view.smoothScrollToPositionFromTop(position, 0);
			}
		});
	}

	public static View getChildAtPosition(final AdapterView view, final int position) {
		final int index = position - view.getFirstVisiblePosition();
		if ((index >= 0) && (index < view.getChildCount())) {
			return view.getChildAt(index);
		} else {
			return null;
		}
	}
}

package encalient.es.scorecenter.Common;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by EnCalientes on 11/12/2015.
 */
public class PagerSwipeRefreshLayout extends SwipeRefreshLayout {

    public View target;

    public PagerSwipeRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public boolean canChildScrollUp() {
        return target.canScrollVertically(-1) | super.canChildScrollUp();
    }
}

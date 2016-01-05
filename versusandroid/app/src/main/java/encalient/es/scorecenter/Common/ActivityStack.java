package encalient.es.scorecenter.Common;

import android.content.Intent;

import java.util.Stack;

/**
 * Created by EnCalientes on 11/5/2015.
 */
//Esta activity es para regresar a la ultima activity al presionar "back" en la toolbar.
public class ActivityStack {
   public static Stack<Intent> stack = new Stack<>();
   public static Integer selectedTabIndexMainActivity = 0;
   public static Integer selectedTabIndexLeaguesActivity = 0;
}

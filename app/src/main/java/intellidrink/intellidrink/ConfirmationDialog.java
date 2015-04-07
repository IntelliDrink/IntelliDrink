package intellidrink.intellidrink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class ConfirmationDialog implements DialogInterface.OnKeyListener{


        private final Context mContext;

        public ConfirmationDialog(final Context context) {
            mContext = context;
        }

        public void showErrorDialog(final String title, final String message) {
            AlertDialog aDialog = new AlertDialog.Builder(mContext).setMessage(message).setTitle(title)
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,
                                            final int which) {
                            //Prevent to finish activity, if user clicks about.
                            if (!title.equalsIgnoreCase("About") && !title.equalsIgnoreCase("Directory Error") && !title.equalsIgnoreCase("View")) {
                                ((Activity) mContext).finish();
                            }

                        }
                    }).create();
            aDialog.setOnKeyListener(this);
            aDialog.show();
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                //disable the back button
            }
            return true;
        }
}

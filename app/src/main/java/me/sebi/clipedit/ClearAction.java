package me.sebi.clipedit;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class ClearAction extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
        if (sdk < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText("");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", "");
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_clipboardCleared), Toast.LENGTH_SHORT).show();

        finish();
    }
}

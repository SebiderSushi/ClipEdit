package me.sebi.clipedit;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private final int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
    private boolean getOnOpen, setOnClose, closeOnClear;
    private SharedPreferences prefs;
    private android.text.ClipboardManager clipboard_text;
    private android.content.ClipboardManager clipboard_content;
    private EditText editText;
    private Button button_clearAndExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (sdk < 11)
            clipboard_text = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        else
            clipboard_content = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        editText = findViewById(R.id.editText);
        button_clearAndExit = findViewById(R.id.button_clearAndExit);
        loadPrefs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrefs();
        boolean wasEmpty = editText.getText().length() == 0;
        if (getOnOpen) getClipboard(null);
        if (wasEmpty) editText.setSelection(editText.getText().length());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (setOnClose) setClipboard(null);
    }

    public void setClipboard(View view) {
        String text = editText.getText().toString();
        if (sdk < 11) {
            clipboard_text.setText(text);
        } else {
            ClipData clip = ClipData.newPlainText("", text);
            clipboard_content.setPrimaryClip(clip);
        }
    }

    public void getClipboard(View view) {
        CharSequence text = "";
        if (sdk < 11) {
            text = clipboard_text.getText();
        } else {
            ClipData clip = clipboard_content.getPrimaryClip();
            if (clip != null) text = clip.getItemAt(0).coerceToText(this);
        }
        if (view == null) {
            if (!editText.getText().toString().equals(text))
                editText.setText(text);
        } else
            editText.append(text);
    }

    public void clearEditText(View view) {
        editText.setText("");
        if (closeOnClear) {
            setClipboard(null);
            finish();
        }
    }

    public void clearAndExit(View view) {
        editText.setText("");
        setClipboard(null);
        finish();
    }

    public void loadPrefs() {
        getOnOpen = prefs.getBoolean("getOnOpen", true);
        setOnClose = prefs.getBoolean("setOnClose", true);
        closeOnClear = prefs.getBoolean("closeOnClear", false);
        boolean clearAndExit = prefs.getBoolean("clearAndExit", true);
        boolean centerText = prefs.getBoolean("centerText", true);

        if (clearAndExit) button_clearAndExit.setVisibility(View.VISIBLE);
        else button_clearAndExit.setVisibility(View.GONE);
        if (centerText) editText.setGravity(Gravity.CENTER);
        else editText.setGravity(Gravity.START);
    }
}

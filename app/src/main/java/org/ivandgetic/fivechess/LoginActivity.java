package org.ivandgetic.fivechess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ivandgetic.fivechess.adapters.UserAdapter;
import org.ivandgetic.fivechess.settings.SettingsActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText nameEditText, passwordEditText;
    public static Button attemptLoginButton;
    public static LoginActivity loginActivity;

    public static LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public LoginActivity() {
        loginActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (MyService.socket == null) {
            stopService(new Intent(this, MyService.class));
            startService(new Intent(this, MyService.class));
        }
        attemptLoginButton = (Button) findViewById(R.id.sign_in_button);
        nameEditText = (EditText) findViewById(R.id.name);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && nameEditText.getText().length() > 0) {
                    attemptLoginButton.setEnabled(true);
                } else {
                    attemptLoginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_exit) {
            stopService(new Intent(this, MyService.class));
            System.exit(0);
            return true;
        } else if (id == R.id.action_reconnect) {
            UserAdapter.userList.clear();
            try {
                if (MyService.dataOutputStream != null) {
                    MyService.dataOutputStream.close();
                }
                if (MyService.dataInputStream != null) {
                    MyService.dataInputStream.close();
                }
                if (MyService.socket != null) {
                    MyService.socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopService(new Intent(this, MyService.class));
            startService(new Intent(this, MyService.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void attemptLogin(View view) throws IOException {
        String name = nameEditText.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("name", name).apply();
        String password = passwordEditText.getText().toString();
        if (MyService.dataOutputStream != null) {
            MyService.dataOutputStream.writeUTF("Login:" + name + ":" + password);
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

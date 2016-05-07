package org.ivandgetic.fivechess;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, passwordEditText,passwordAgainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEditText = (EditText) findViewById(R.id.name);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordAgainEditText = (EditText) findViewById(R.id.password_again);
    }

    public void register(View view) throws IOException {
        if (nameEditText.length()==0){
            Toast.makeText(this,"用户名为空",Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println(passwordEditText.getText());
        System.out.println(passwordAgainEditText.getText());
        if (!passwordEditText.getText().toString().equals(passwordAgainEditText.getText().toString())){
            Toast.makeText(this,"输入的密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        if (MyService.dataOutputStream==null){
            Toast.makeText(this,"没有网络连接",Toast.LENGTH_SHORT).show();
            return;
        }
        String name = nameEditText.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("name", name).apply();
        String password = passwordEditText.getText().toString();
        if (MyService.dataOutputStream != null) {
            MyService.dataOutputStream.writeUTF("Register:" + name + ":" + password);
        }
    }
}

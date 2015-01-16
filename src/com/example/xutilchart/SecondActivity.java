package com.example.xutilchart;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/1/16.
 */
public class SecondActivity extends Activity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();
            }
        });
    }

    private void initView() {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view, null);
        Button btn = (Button) view.findViewById(R.id.login);
        final EditText usernameEdit = (EditText) view.findViewById(R.id.username);
        final EditText passwordEdit = (EditText) view.findViewById(R.id.password);
        AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Toast.makeText(SecondActivity.this, "username: " + username + " , password: " + password, Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}

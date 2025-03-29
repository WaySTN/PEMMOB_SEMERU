package com.example.logincrud;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPWActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnReset;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_activity);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);
        dbManager = new DatabaseManager(this);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ResetPWActivity.this, "Masukkan email Anda!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbManager.isUserExists(email)) {
                    Intent intent = new Intent(ResetPWActivity.this, NewPasswordActivity.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPWActivity.this, "Email tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Metode untuk menangani klik tombol "Kembali ke Login"
    public void BackLoginClick(View view) {
        Intent intent = new Intent(ResetPWActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Menutup aktivitas ResetPWActivity
    }
}

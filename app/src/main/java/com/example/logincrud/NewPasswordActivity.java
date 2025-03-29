package com.example.logincrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText etNewPassword, etConfirmPassword;
    private Button btnUpdatePassword;
    private DatabaseManager dbManager;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpw_activity);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        dbManager = new DatabaseManager(this);

        email = getIntent().getStringExtra("EMAIL");

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(NewPasswordActivity.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(NewPasswordActivity.this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean updateSuccess = dbManager.updatePassword(email, newPassword);
                if (updateSuccess) {
                    Toast.makeText(NewPasswordActivity.this, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(NewPasswordActivity.this, "Gagal memperbarui password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Metode untuk menangani klik tombol "Kembali ke Login"
    public void BackLoginClick(View view) {
        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Menutup aktivitas ResetPWActivity
    }
}

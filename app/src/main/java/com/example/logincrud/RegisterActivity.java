package com.example.logincrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPhone, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvLogin;
    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Inisialisasi UI
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Inisialisasi database manager
        dbManager = new DatabaseManager(this);

        if (dbManager == null) {
            Log.e("REGISTER_ERROR", "DatabaseManager gagal diinisialisasi!");
        } else {
            Log.d("REGISTER", "DatabaseManager berhasil diinisialisasi.");
        }

        // Event klik tombol register
        btnRegister.setOnClickListener(view -> registerUser());

        // Event klik teks login
        tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        Log.d("REGISTER", "Data Input: " + username + ", " + email + ", " + phone + ", " + password);

        // Validasi input
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            Log.e("REGISTER_ERROR", "Ada kolom kosong!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
            Log.e("REGISTER_ERROR", "Password dan konfirmasi tidak cocok.");
            return;
        }

        // Cek apakah email sudah ada
        if (dbManager.isUserExists(email)) {
            Toast.makeText(this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
            Log.e("REGISTER_ERROR", "Email sudah ada di database: " + email);
            return;
        }

        // Simpan ke database
        boolean isInserted = dbManager.insertUser(username, email, phone, password);
        Log.d("REGISTER", "Insert User Result: " + isInserted);

        if (isInserted) {
            Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
            Log.d("REGISTER", "User berhasil didaftarkan: " + username);

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registrasi gagal! Coba lagi.", Toast.LENGTH_SHORT).show();
            Log.e("REGISTER_ERROR", "Gagal menyimpan data user.");
        }
    }
}

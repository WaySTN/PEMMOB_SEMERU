package com.example.logincrud;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtNama, edtEmail, edtNominal, edtPesan;
    Spinner spinnerMetode;
    Button btnBaru, btnSimpan, btnUbah, btnHapus, btnLogout;
    ListView listDonasi;
    DatabaseManager2 dbManagerr;
    ArrayAdapter<String> adapter;
    ArrayList<String> listData;
    int selectedId = -1; // ID data yang dipilih untuk update/hapus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen UI
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtNominal = findViewById(R.id.edtNominal);
        edtPesan = findViewById(R.id.edtPesan);
        spinnerMetode = findViewById(R.id.spinnerMetode);
        btnBaru = findViewById(R.id.btnBaru);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnUbah = findViewById(R.id.btnUbah);
        btnHapus = findViewById(R.id.btnHapus);
        btnLogout = findViewById(R.id.btnLogout);
        listDonasi = findViewById(R.id.listDonasi);

        // Inisialisasi database
        dbManagerr = new DatabaseManager2(this);

        // Set up spinner metode pembayaran
        ArrayAdapter<CharSequence> metodeAdapter = ArrayAdapter.createFromResource(
                this, R.array.metode_pembayaran, android.R.layout.simple_spinner_item);
        metodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetode.setAdapter(metodeAdapter);

        // Load data donasi
        loadData();

        // Tambah data
        btnSimpan.setOnClickListener(view -> {
            String nama = edtNama.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String nominalStr = edtNominal.getText().toString().trim();
            String metode = spinnerMetode.getSelectedItem().toString();
            String pesan = edtPesan.getText().toString().trim();

            // Cek apakah ada input yang kosong
            if (nama.isEmpty() || email.isEmpty() || nominalStr.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            int nominal;
            try {
                nominal = Integer.parseInt(nominalStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal harus berupa angka!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validasi minimal Rp 10.000
            if (nominal < 10000) {
                Toast.makeText(this, "Minimal donasi adalah Rp 10.000!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jika semua valid, simpan data
            dbManagerr.insertData(nama, email, nominalStr, metode, pesan);
            Toast.makeText(this, "Data disimpan", Toast.LENGTH_SHORT).show();
            loadData();
            clearFields();
        });

        // Pilih data dari ListView
        listDonasi.setOnItemClickListener((adapterView, view, i, l) -> {
            Cursor cursor = dbManagerr.getAllData();
            if (cursor != null && cursor.moveToPosition(i)) {
                selectedId = cursor.getInt(0);
                edtNama.setText(cursor.getString(1));
                edtEmail.setText(cursor.getString(2));
                edtNominal.setText(cursor.getString(3));
                spinnerMetode.setSelection(metodeAdapter.getPosition(cursor.getString(4)));
                edtPesan.setText(cursor.getString(5));
                cursor.close(); // Tutup cursor untuk mencegah memory leak
            }
        });

        // Update data
        btnUbah.setOnClickListener(view -> {
            if (selectedId != -1) {
                dbManagerr.updateData(selectedId, edtNama.getText().toString(), edtEmail.getText().toString(),
                        edtNominal.getText().toString(), spinnerMetode.getSelectedItem().toString(), edtPesan.getText().toString());
                Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show();
                loadData();
                clearFields();
            } else {
                Toast.makeText(this, "Pilih data yang ingin diubah!", Toast.LENGTH_SHORT).show();
            }
        });

        // Hapus data
        btnHapus.setOnClickListener(view -> {
            if (selectedId != -1) {
                dbManagerr.deleteData(selectedId);
                Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show();
                loadData();
                clearFields();
            } else {
                Toast.makeText(this, "Pilih data yang ingin dihapus!", Toast.LENGTH_SHORT).show();
            }
        });

        // Reset input
        btnBaru.setOnClickListener(view -> clearFields());

        // Tombol Logout
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadData() {
        listData = new ArrayList<>();
        Cursor cursor = dbManagerr.getAllData();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nama = cursor.getString(1);
                String nominal = cursor.getString(3);
                String pesan = cursor.getString(5);

                if (pesan == null || pesan.isEmpty()) {
                    pesan = "(Tidak ada pesan)";
                }

                listData.add(nama + " - Rp" + nominal + "\nPesan: " + pesan);
            }
            cursor.close(); // Pastikan cursor ditutup untuk menghindari memory leak
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listDonasi.setAdapter(adapter);
    }

    private void clearFields() {
        edtNama.setText("");
        edtEmail.setText("");
        edtNominal.setText("");
        edtPesan.setText("");
        spinnerMetode.setSelection(0);
        selectedId = -1;
    }
}

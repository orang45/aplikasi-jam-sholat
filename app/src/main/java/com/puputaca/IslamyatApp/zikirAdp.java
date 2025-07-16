package com.puputaca.IslamyatApp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class zikirAdp extends AppCompatActivity {

    TextView azkaarText; // Deklarasi TextView untuk menampilkan teks zikir


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikir_adp);
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String azkaarname = getIntent().getStringExtra("name"); // Mendapatkan nama zikir dari Intent
        String azkaartext = getIntent().getStringExtra("text"); // Mendapatkan teks zikir dari Intent
        setTitle(azkaarname); // Mengatur judul aktivitas ke nama zikir
        azkaarText = findViewById(R.id.azkaaradp); // Menghubungkan variabel dengan TextView di layout
        azkaarText.setText(azkaartext); // Mengatur teks zikir ke TextView
        // Memuat ukuran teks yang disimpan dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("text0", MODE_PRIVATE);
        int returned_size = preferences.getInt("text1", 30); // Mengambil ukuran teks, default 30
        azkaarText.setTextSize(returned_size); // Mengatur ukuran teks TextView

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doa_menu, menu); // Menginflasi menu adiaa_menu ke dalam Menu
        return true; // Menunjukkan bahwa menu telah ditangani
    }

    // Metode yang dipanggil saat opsi "ubah ukuran teks" dipilih dari menu
    public void changetxtsize(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(zikirAdp.this);
        alertDialog.setTitle("Pilih ukuran font yang sesuai"); // Mengatur judul dialog dalam Bahasa Indonesia
        String[] items = {"Kecil - Small", "Sedang - Medium", "Besar - Large", "Sangat Besar - X-Large"}; // Pilihan ukuran font
        int checkedItem = -1; // Item yang dipilih secara default (tidak ada yang dipilih)

        // Menambahkan item pilihan tunggal ke dialog
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Menangani klik pada item pilihan
                switch (which) {
                    case 0: // Pilihan "Kecil"
                        SharedPreferences.Editor editor1 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor1.putInt("text1", 25); // Menyimpan ukuran teks 25
                        editor1.apply(); // Menerapkan perubahan
                        azkaarText.setTextSize(25); // Mengatur ukuran teks TextView
                        break;
                    case 1: // Pilihan "Sedang"
                        SharedPreferences.Editor editor2 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor2.putInt("text1", 30); // Menyimpan ukuran teks 30
                        editor2.apply();
                        azkaarText.setTextSize(30);
                        break;
                    case 2: // Pilihan "Besar"
                        SharedPreferences.Editor editor3 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor3.putInt("text1", 35); // Menyimpan ukuran teks 35
                        editor3.apply();
                        azkaarText.setTextSize(35);
                        break;
                    case 3: // Pilihan "Sangat Besar"
                        SharedPreferences.Editor editor4 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor4.putInt("text1", 45); // Menyimpan ukuran teks 45
                        editor4.apply();
                        azkaarText.setTextSize(45);
                        break;
                }
            }
        });
        alertDialog.setPositiveButton("Oke", null); // Menambahkan tombol positif "Oke"
        AlertDialog alert = alertDialog.create(); // Membuat AlertDialog
        alert.setCanceledOnTouchOutside(false); // Mencegah dialog ditutup saat disentuh di luar
        alert.show(); // Menampilkan dialog
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani pemilihan item dari menu opsi
        switch (item.getItemId()) {
            // Menanggapi tombol Up/Home (tombol kembali di action bar)
            case android.R.id.home:
                onBackPressed(); // Memanggil metode onBackPressed untuk kembali ke aktivitas sebelumnya
                return true;
            default:
                recreate(); // Membuat ulang aktivitas jika item lain dipilih

        }
        return super.onOptionsItemSelected(item); // Memanggil implementasi superclass
    }
}

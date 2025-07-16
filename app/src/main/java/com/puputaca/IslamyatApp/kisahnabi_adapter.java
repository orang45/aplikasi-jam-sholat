package com.puputaca.IslamyatApp;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class kisahnabi_adapter extends AppCompatActivity {
    TextView anbiaaadp; // Deklarasi TextView untuk menampilkan teks cerita kisahnabi
    ScrollView svanb; // Deklarasi ScrollView untuk menggulir teks cerita kisahnabi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisahnabi_adapter);
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        anbiaaadp = findViewById(R.id.anbiaaadp); // Menghubungkan variabel dengan TextView di layout
        svanb = findViewById(R.id.svanb); // Menghubungkan variabel dengan ScrollView di layout
        String anbiaaname = getIntent().getStringExtra("names"); // Mendapatkan nama kisahnabi dari Intent
        String anbiaast = getIntent().getStringExtra("stories"); // Mendapatkan teks cerita kisahnabi dari Intent
        setTitle(anbiaaname); // Mengatur judul aktivitas ke nama kisahnabi
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Mengaktifkan tombol kembali di action bar
        anbiaaadp.setText(anbiaast); // Mengatur teks cerita kisahnabi ke TextView
        // Memuat ukuran teks yang disimpan dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("text0", MODE_PRIVATE);
        int returned_size = preferences.getInt("text1", 30); // Mengambil ukuran teks, default 30
        anbiaaadp.setTextSize(returned_size); // Mengatur ukuran teks TextView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kisahnabi_menu, menu); // Menginflasi menu anbiaa_menu ke dalam Menu
        final ObjectAnimator[] animator1 = new ObjectAnimator[1]; // Array untuk menyimpan ObjectAnimator
        MenuItem itemswitch = menu.findItem(R.id.autoscroll); // Mencari item menu autoscroll
        itemswitch.setActionView(R.layout.use_switch); // Mengatur tampilan kustom untuk item menu (sebuah switch)
        final Switch sw = (Switch) menu.findItem(R.id.autoscroll).getActionView().findViewById(R.id.switch2); // Mendapatkan switch dari tampilan kustom

        // Menambahkan listener untuk perubahan status switch
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Jika switch diaktifkan, mulai animasi autoscroll
                    animator1[0] = ObjectAnimator.ofInt(svanb, "scrollY", anbiaaadp.getLayout().getHeight());
                    animator1[0].setInterpolator(new LinearInterpolator()); // Interpolator linier untuk gulir yang mulus
                    animator1[0].setDuration(100000); // Durasi animasi (100 detik)
                    animator1[0].start(); // Memulai animasi
                    Toast.makeText(kisahnabi_adapter.this, "Mode gulir otomatis ke bawah diaktifkan", Toast.LENGTH_SHORT).show(); // Pesan toast dalam Bahasa Indonesia
                } else {
                    // Jika switch dinonaktifkan, batalkan animasi autoscroll
                    if (animator1[0] != null) { // Pastikan animator tidak null sebelum membatalkan
                        animator1[0].cancel();
                    }
                    Toast.makeText(kisahnabi_adapter.this, "Mode gulir otomatis ke bawah dinonaktifkan", Toast.LENGTH_SHORT).show(); // Pesan toast dalam Bahasa Indonesia
                }
            }
        });
        return true; // Menunjukkan bahwa menu telah ditangani
    }

    // Metode yang dipanggil saat opsi "ubah ukuran teks" dipilih dari menu
    public void changetxtsizeanb(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(kisahnabi_adapter.this);
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
                        anbiaaadp.setTextSize(25); // Mengatur ukuran teks TextView
                        break;
                    case 1: // Pilihan "Sedang"
                        SharedPreferences.Editor editor2 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor2.putInt("text1", 30); // Menyimpan ukuran teks 30
                        editor2.apply();
                        anbiaaadp.setTextSize(30);
                        break;
                    case 2: // Pilihan "Besar"
                        SharedPreferences.Editor editor3 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor3.putInt("text1", 35); // Menyimpan ukuran teks 35
                        editor3.apply();
                        anbiaaadp.setTextSize(35);
                        break;
                    case 3: // Pilihan "Sangat Besar"
                        SharedPreferences.Editor editor4 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor4.putInt("text1", 45); // Menyimpan ukuran teks 45
                        editor4.apply();
                        anbiaaadp.setTextSize(45);
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

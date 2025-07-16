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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class quraan_adpter extends AppCompatActivity {
    ScrollView sv; // Deklarasi ScrollView untuk menggulir ayat
    TextView ayatadp; // Deklarasi TextView untuk menampilkan ayat
    LinearLayout layoutquraan; // Deklarasi LinearLayout (mungkin wadah untuk ayat)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quraan_adpter);
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        layoutquraan = findViewById(R.id.layoutquraan); // Menghubungkan variabel dengan LinearLayout di layout
        String soranamear = getIntent().getStringExtra("names"); // Mendapatkan nama Surah Arab dari Intent
        String soramorm = getIntent().getStringExtra("soraamorm"); // Mendapatkan informasi Makkiyah/Madaniyah dari Intent
        String soranameen = getIntent().getStringExtra("namesen"); // Mendapatkan nama Surah Inggris dari Intent
        // Mengatur judul aktivitas dengan nama Surah Arab, tipe, dan nama Surah Inggris
        setTitle(soranamear + "(" + soramorm + ") - " + soranameen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Mengaktifkan tombol kembali di action bar
        ayatadp = findViewById(R.id.ayatadp); // Menghubungkan variabel dengan TextView di layout
        String soraayat = getIntent().getStringExtra("soraayat"); // Mendapatkan ayat Surah dari Intent
        ayatadp.setText(soraayat); // Mengatur teks ayat ke TextView
        sv = findViewById(R.id.sv); // Menghubungkan variabel dengan ScrollView di layout
        // Memuat ukuran teks yang disimpan dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("text0", MODE_PRIVATE);
        int returned_size = preferences.getInt("text1", 30); // Mengambil ukuran teks, default 30
        ayatadp.setTextSize(returned_size); // Mengatur ukuran teks TextView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quraan_menu, menu); // Menginflasi menu quraan_menu ke dalam Menu
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
                    animator1[0] = ObjectAnimator.ofInt(sv, "scrollY", ayatadp.getLayout().getHeight());
                    animator1[0].setInterpolator(new LinearInterpolator()); // Interpolator linier untuk gulir yang mulus
                    animator1[0].setDuration(1000000); // Durasi animasi (1 juta milidetik = 1000 detik)
                    animator1[0].start(); // Memulai animasi
                    Toast.makeText(quraan_adpter.this, "Mode gulir otomatis ke bawah diaktifkan", Toast.LENGTH_SHORT).show(); // Pesan toast
                } else {
                    // Jika switch dinonaktifkan, batalkan animasi autoscroll
                    if (animator1[0] != null) { // Pastikan animator tidak null sebelum membatalkan
                        animator1[0].cancel();
                    }
                    Toast.makeText(quraan_adpter.this, "Mode gulir otomatis ke bawah dinonaktifkan", Toast.LENGTH_SHORT).show(); // Pesan toast
                }
            }
        });

        return true; // Menunjukkan bahwa menu telah ditangani
    }

    // Metode yang dipanggil saat opsi "ubah ukuran teks" dipilih dari menu
    public void changetxtsize(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(quraan_adpter.this);
        alertDialog.setTitle("Pilih ukuran font yang sesuai"); // Mengatur judul dialog
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
                        ayatadp.setTextSize(25); // Mengatur ukuran teks TextView
                        break;
                    case 1: // Pilihan "Sedang"
                        SharedPreferences.Editor editor2 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor2.putInt("text1", 30); // Menyimpan ukuran teks 30
                        editor2.apply();
                        ayatadp.setTextSize(30);
                        break;
                    case 2: // Pilihan "Besar"
                        SharedPreferences.Editor editor3 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor3.putInt("text1", 35); // Menyimpan ukuran teks 35
                        editor3.apply();
                        ayatadp.setTextSize(35);
                        break;
                    case 3: // Pilihan "Sangat Besar"
                        SharedPreferences.Editor editor4 = getSharedPreferences("text0", MODE_PRIVATE).edit();
                        editor4.putInt("text1", 45); // Menyimpan ukuran teks 45
                        editor4.apply();
                        ayatadp.setTextSize(45);
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

package com.puputaca.IslamyatApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class tasbih extends AppCompatActivity {

    public static final String SHARED_PREF = "shared";
    public static final String TEXT = "text";
    public int scounter;
    public String text;
    public AlphaAnimation buttonClick = new AlphaAnimation(0.8F, 0.8F);
    TextView counter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout clickplus1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);
        // Mengatur judul aktivitas
        setTitle("Tasbih Digital");
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Mengubah warna bilah navigasi untuk Android Lollipop dan yang lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));
        }
        // Menginisialisasi tampilan
        counter = findViewById(R.id.counter);
        clickplus1 = findViewById(R.id.clickplus1);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);

        // Menambahkan OnClickListener ke area yang dapat diklik untuk menambah hitungan
        clickplus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengambil nilai hitungan saat ini dan menambahkannya
                scounter = Integer.parseInt(counter.getText().toString());
                scounter = scounter + 1;
                // Mengatur teks penghitung ke nilai baru
                counter.setText(Integer.toString(scounter));
                // Menyimpan nilai hitungan ke SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT, counter.getText().toString());
                editor.apply();
                // Menampilkan pesan toast jika hitungan mencapai 999
                if (scounter == 999) {
                    Toast.makeText(tasbih.this,
                            "Tasbih telah mencapai batas maksimum. Semoga Allah memberkahi Anda. Mohon atur ulang tasbih untuk melanjutkan.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Menambahkan OnRefreshListener ke SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Menampilkan dialog konfirmasi untuk mengatur ulang tasbih
                new AlertDialog.Builder(tasbih.this)
                        .setTitle("Atur Ulang")
                        .setMessage("Apakah Anda ingin mengatur ulang tasbih ke nol?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Mengatur ulang penghitung ke "0"
                                counter.setText("0");
                                scounter = Integer.parseInt(counter.getText().toString());
                                // Menyimpan nilai "0" ke SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(TEXT, counter.getText().toString());
                                editor.apply();
                                // Menghentikan indikator penyegaran
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        })
                        .setNegativeButton("Tidak", null) // Tombol "Tidak" tidak melakukan apa-apa
                        .show();
                // Menghentikan indikator penyegaran (dalam kasus ini jika pengguna memilih "Tidak")
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Memuat nilai hitungan yang disimpan saat aktivitas dibuat
        update();
    }

    // Metode untuk memuat nilai hitungan yang disimpan dari SharedPreferences
    private void update() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "0"); // Mengambil nilai, default ke "0" jika tidak ada
        counter.setText(text); // Mengatur teks penghitung
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani pemilihan item menu
        switch (item.getItemId()) {
            // Menanggapi tombol Up/Home di action bar
            case android.R.id.home:
                onBackPressed(); // Kembali ke aktivitas sebelumnya
                return true;
            default:
                recreate(); // Membuat ulang aktivitas
        }
        return super.onOptionsItemSelected(item);
    }
}
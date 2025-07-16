package com.puputaca.IslamyatApp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;

public class stories extends AppCompatActivity {


    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Kisah-kisah"); // Judul asli "Stories - قَصَص وَمُعْجِزَات"
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani pemilihan item menu
        switch (item.getItemId()) {
            // Menanggapi tombol Up/Home di bilah tindakan
            case android.R.id.home:
                onBackPressed(); // Kembali ke aktivitas sebelumnya
                return true;
            default:
                recreate(); // Membuat ulang aktivitas
        }
        return super.onOptionsItemSelected(item);
    }


    // Metode untuk memulai aktivitas kisahnabi saat tombol diklik
    public void anbiaa_stories(View view) {
        view.startAnimation(buttonClick); // Memulai animasi klik tombol
        Intent intent = new Intent(stories.this, kisahnabi.class); // Membuat intent baru untuk kisahnabi
        startActivity(intent); // Memulai aktivitas kisahnabi
    }
}
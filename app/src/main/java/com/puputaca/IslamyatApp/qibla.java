package com.puputaca.IslamyatApp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Kelas qibla yang mengimplementasikan SensorEventListener untuk mendeteksi perubahan sensor
public class qibla extends AppCompatActivity implements SensorEventListener {

    private static Sensor sensor; // Objek Sensor
    private static SensorManager sensorManager; // Objek SensorManager
    ImageView ic_compass; // ImageView untuk menampilkan kompas
    Button qi; // Tombol (mungkin untuk refresh atau tindakan lain)
    private float currentDegree; // Derajat rotasi kompas saat ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Arah Kiblat");
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inisialisasi animasi klik tombol
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        ic_compass = findViewById(R.id.ic_compass); // Menghubungkan ImageView kompas dengan ID di layout

        // Mendapatkan SensorManager dari layanan sistem
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Mendapatkan sensor orientasi default
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // Memeriksa apakah sensor orientasi tersedia
        if (sensor != null) {
            // Mendaftarkan listener untuk sensor orientasi dengan kecepatan tercepat
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            // Menampilkan pesan toast jika sensor tidak didukung
            Toast.makeText(getApplicationContext(), "Tidak Didukung", Toast.LENGTH_SHORT).show(); // Pesan asli "Not Support"
        }

        qi = findViewById(R.id.butqi); // Menghubungkan tombol qi dengan ID di layout
        // Menambahkan OnClickListener ke tombol qi
        qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick); // Memulai animasi klik tombol
                // Melakukan refresh aktivitas dengan transisi nol
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    // Metode yang dipanggil ketika nilai sensor berubah
    public void onSensorChanged(SensorEvent event) {
        // Mengambil derajat rotasi dari nilai sensor (azimuth)
        int degree = Math.round(event.values[0]);

        // Membuat animasi rotasi untuk kompas
        // currentDegree adalah posisi awal, -degree adalah posisi akhir (minus karena rotasi gambar berlawanan)
        RotateAnimation animation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000); // Durasi animasi dalam milidetik (1 detik)
        animation.setFillAfter(true); // Mempertahankan rotasi setelah animasi selesai
        ic_compass.setAnimation(animation); // Menerapkan animasi ke ImageView kompas
        currentDegree = -degree; // Memperbarui currentDegree untuk rotasi berikutnya
    }

    @Override
    // Metode yang dipanggil ketika akurasi sensor berubah (tidak digunakan dalam implementasi ini)
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Logika untuk menangani perubahan akurasi sensor jika diperlukan
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

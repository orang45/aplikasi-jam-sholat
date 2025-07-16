// Zikir
package com.puputaca.IslamyatApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class zikir extends AppCompatActivity {

    DatabaseHelper mDBHelper; // Objek helper untuk mengelola database
    ListView azkaarNameList; // ListView untuk menampilkan daftar nama zikir
    private SQLiteDatabase mDb; // Objek database SQLite

    @SuppressLint("Range") // Menekan peringatan lint tentang penggunaan getColumnIndex tanpa pengecekan kolom
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikir);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Zikir"); // Judul asli "أَذْكَار - Citation"
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        azkaarNameList = findViewById(R.id.azkaanamelist); // Menghubungkan ListView dengan ID di layout

        // Bagian inisialisasi dan pengelolaan database
        mDBHelper = new DatabaseHelper(this); // Menginisialisasi DatabaseHelper
        try {
            // Memperbarui database dari aset jika diperlukan
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            // Melemparkan Error jika database tidak dapat diperbarui
            throw new Error("Tidak dapat memperbarui Database", mIOException);
        }
        try {
            // Mendapatkan instance database yang dapat ditulis
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            // Melemparkan pengecualian jika ada masalah saat mendapatkan database
            throw mSQLException;
        }
        // Mengambil nama-nama zikir dari database
        ArrayList<String> azkaarnames = new ArrayList<>();
        // Melakukan kueri semua data dari tabel 'azkar_labels'
        Cursor resar = mDBHelper.getReadableDatabase().rawQuery("select * from azkar_labels", null);
        resar.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!resar.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            azkaarnames.add(resar.getString(resar.getColumnIndex("name"))); // Tambahkan nama zikir ke ArrayList
            resar.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        resar.close(); // Tutup kursor

        // Mengonversi ArrayList nama zikir ke array String
        String[] azkaarnamesArr = azkaarnames.toArray(new String[0]);
        //////////////
        // Mengambil teks zikir dari database
        ArrayList<String> azkaartext = new ArrayList<>();
        // Melakukan kueri semua data dari tabel 'azkar_labels'
        Cursor resen = mDBHelper.getReadableDatabase().rawQuery("select * from azkar_labels", null);
        resen.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!resen.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            azkaartext.add(resen.getString(resen.getColumnIndex("text"))); // Tambahkan teks zikir ke ArrayList
            resen.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        resen.close(); // Tutup kursor

        // Mengonversi ArrayList teks zikir ke array String
        String[] azkaartextArr = azkaartext.toArray(new String[0]);

        // Array final untuk digunakan dalam listener
        String[] finalAzkaartextArr = azkaartextArr;
        String[] finalAzkaarnamesArr = azkaarnamesArr;

        // Menambahkan OnItemClickListener ke ListView
        azkaarNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F); // Animasi klik tombol
                view.startAnimation(buttonClick); // Memulai animasi
                Intent intent = new Intent(zikir.this, zikirAdp.class); // Membuat Intent baru untuk zikirAdp
                intent.putExtra("name", finalAzkaarnamesArr[i]); // Mengirim nama zikir
                intent.putExtra("text", finalAzkaartextArr[i]); // Mengirim teks zikir
                startActivity(intent); // Memulai aktivitas
            }
        });
        // Membuat ArrayAdapter untuk ListView, menggunakan custom layout list_view_custom
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view_custom, azkaarnamesArr);
        azkaarNameList.setAdapter(adapter); // Mengatur adapter ke ListView

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

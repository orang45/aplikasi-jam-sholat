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

public class kisahnabi extends AppCompatActivity {

    DatabaseHelper mDBHelper; // Objek helper untuk mengelola database
    ListView anbiaalist; // ListView untuk menampilkan daftar nama kisahnabi
    private SQLiteDatabase mDb; // Objek database SQLite

    @SuppressLint("Range") // Menekan peringatan lint tentang penggunaan getColumnIndex tanpa pengecekan kolom
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisahnabi);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Mukjizat Para Nabi - Prophets Miracles"); // Judul asli "مُعْجِزَاتِ الْأَنْبِيَاءِ - Prophets Miracles"
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anbiaalist = findViewById(R.id.anbiaalist); // Menghubungkan ListView dengan ID di layout

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
        ///////
        // Mengambil nama-nama kisahnabi dari database
        ArrayList<String> anbianame = new ArrayList<>();
        // Melakukan kueri semua data dari tabel 'ANBIAA'
        Cursor res = mDBHelper.getReadableDatabase().rawQuery("select * from ANBIAA", null);
        res.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!res.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            anbianame.add(res.getString(res.getColumnIndex("header"))); // Tambahkan nilai kolom "header" ke ArrayList
            res.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        res.close(); // Tutup kursor

        // Mengonversi ArrayList nama kisahnabi ke array String
        String[] anbianamearr = anbianame.toArray(new String[0]);

        /////////
        // Mengambil cerita-cerita kisahnabi dari database
        ArrayList<String> anbiast = new ArrayList<>();
        // Melakukan kueri semua data dari tabel 'ANBIAA'
        Cursor rest = mDBHelper.getReadableDatabase().rawQuery("select * from ANBIAA", null);
        rest.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!rest.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            anbiast.add(rest.getString(rest.getColumnIndex("title"))); // Tambahkan nilai kolom "title" ke ArrayList
            rest.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        rest.close(); // Tutup kursor

        // Mengonversi ArrayList cerita kisahnabi ke array String
        String[] anbiastarr = anbiast.toArray(new String[0]);
        /////////////
        // Membuat ArrayAdapter untuk ListView, menggunakan custom layout list_view_custom
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view_custom, anbianamearr);
        anbiaalist.setAdapter(adapter); // Mengatur adapter ke ListView

        final String[] finalAnbianamearr = anbianamearr; // Array final untuk nama kisahnabi
        final String[] finalAnbiastarr = anbiastarr; // Array final untuk cerita kisahnabi

        // Menambahkan OnItemClickListener ke ListView
        anbiaalist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F); // Animasi klik tombol
                view.startAnimation(buttonClick); // Memulai animasi
                Intent intent = new Intent(kisahnabi.this, kisahnabi_adapter.class); // Membuat Intent baru untuk kisahnabi_adapter
                intent.putExtra("names", finalAnbianamearr[i]); // Mengirim nama kisahnabi
                intent.putExtra("stories", finalAnbiastarr[i]); // Mengirim cerita kisahnabi
                startActivity(intent); // Memulai aktivitas
            }
        });
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

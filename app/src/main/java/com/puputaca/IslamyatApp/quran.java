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
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class quran extends AppCompatActivity {
    DatabaseHelper mDBHelper; // Objek pembantu untuk mengelola database
    ListView soraname; // ListView untuk menampilkan nama-nama Surah
    // Array yang menunjukkan apakah Surah Makkiyah (مكية) atau Madaniyah (مدنية)
    String[] m_or_m = {"Makkiyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Makkiyah", "Makkiyah", "Madaniyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Madaniyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah", "Madaniyah", "Makkiyah", "Makkiyah", "Makkiyah", "Makkiyah"};
    private SQLiteDatabase mDb; // Objek database SQLite

    /*String soranames[] = {" الْفَاتِحَةَ", " الْبَقَرَةَ", " آلَ عُمْرَانُ", " النِّسَاءَ", " الْمَائِدَةَ", " الْأَنْعَامَ", " الْأَعْرَافَ", " الْأَنْفَالَ",
            " التَّوْبَةَ", " يُونِسَ", " هُودَ", " يوسف", " الرَّعْدَ", " ابراهيم", " الْحَجَرَ", " النَّحْلَ", " الْإِسْرَاءَ", " الْكَهْفَ", " مَرْيَمَ", " طه", " الْأَنْبِيَاءَ",
            " الْحَجَّ", " الْمُؤْمِنُونَ", " النُّورَ", " الْفَرْقَانِ", " الشُّعرَاءَ", " النَّمْلَ", " الْقَصَصَ", " الْعَنْكَبُوتَ", " الرُّومَ", " لُقْمَانَ", " السَّجْدَةَ", " الْأحْزَابَ", " سبإِ", " فَاُطْرُ", " يس", " الصَّافَّاتَ", " ص", " الزَّمْرَ",
            " غَافِرَ", " فَصَلَتْ", " الشُّورَى", " الزُّخْرُفَ", " الدُّخَانَ", " الْجَاثِيَةَ", " الْأَحْقَافَ", " مُحَمَّدَ", " الْفَتْحَ", " الْحَجَرَاتِ", " ق", " الذَّارِيَاتِ", " الطَّوْرَ", " النَّجْمَ", " الْقَمَرَ", " الرَّحْمَنَ", " الْوَاقِعَةَ", " الْحَديدَ", " الْمُجَادَلَةَ", " الْحَشْرَ", " الْمُمْتَحَنَةَ", " الصَّفَّ", " الْجَمْعَةَ", " الْمُنَافِقُونَ", " التَّغَابُنَ", " الطَّلَاَقَ",
            " التَّحْرِيمَ", " الْمَلِكَ", " الْقَلَمَ", " الْحَاقَّةَ", " الْمَعَارِجَ", " نُوحِ", " الْجِنَّ", " الْمُزَّمِّلَ", " الْمُدَّثِّرَ", " الْقِيَامَةَ", " الانسان", " الْمُرْسَلَاتِ", " النبإ", " النَّازِعَاتِ", " عَبَسَ",
            " التَّكْويرَ", " الإنفطار", " الْمطففِينَ", " الإنشقاق", " الْبُروجَ", " الطَّارِقَ", " الْأعْلَى", " الْغَاشِّيَّةَ", " الْفَجْرَ", " الْبَلَدَ", " الشَّمْسَ", " اللَّيْلَ",
            " الضُّحَى", " الشَّرْحَ", " التّينَ", " الْعَلَقَ", " الْقَدْرَ", " الْبَيِّنَةَ", " الزَّلْزَلَةَ", " الْعَادِيَاتِ", " الْقَارِعَةَ", " التَّكَاثُرَ", " الْعَصْرَ", " الْهَمْزَةَ",
            " الْفِيلَ", " قُرَيْشَ", " الْمَاعُونَ", " الْكَوْثَرَ", " الْكَافِرُونَ", " النَّصْرَ", " الْمَسَدَ", " الْإِخْلَاَصَ", " الْفَلْقَ", " النَّاسَ"
    };*/
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Al-Qur'an Karim - The Holy Quran");
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        soraname = findViewById(R.id.sora_name); // Menghubungkan ListView dengan ID di layout

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
        ////////////
        // Mengambil nama-nama Surah dalam bahasa Arab dari database
        ArrayList<String> soraarrnamearlist = new ArrayList<>();
        Cursor resar = mDBHelper.getReadableDatabase().rawQuery("select * from SORA_NAME", null);
        resar.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!resar.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            soraarrnamearlist.add(resar.getString(resar.getColumnIndex("sora_name_ar"))); // Tambahkan nama Surah Arab ke ArrayList
            resar.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        resar.close(); // Tutup kursor

        // Mengonversi ArrayList nama Surah Arab ke array String
        String[] soraarnamearr = soraarrnamearlist.toArray(new String[0]);
        //////////////
        // Mengambil nama-nama Surah dalam bahasa Inggris dari database
        ArrayList<String> soraennamelist = new ArrayList<>();
        Cursor resen = mDBHelper.getReadableDatabase().rawQuery("select * from SORA_NAME", null);
        resen.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!resen.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            soraennamelist.add(resen.getString(resen.getColumnIndex("sora_name_en"))); // Tambahkan nama Surah Inggris ke ArrayList
            resen.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        resen.close(); // Tutup kursor

        // Mengonversi ArrayList nama Surah Inggris ke array String
        String[] soraennamearr = soraennamelist.toArray(new String[0]);
        //////////////
        // Mengambil ayat-ayat Surah dari database
        ArrayList<String> soraarrlist = new ArrayList<>();
        Cursor res = mDBHelper.getReadableDatabase().rawQuery("select * from SORA_AYAT", null);
        res.moveToFirst(); // Pindahkan kursor ke baris pertama
        while (!res.isAfterLast()) { // Iterasi selama kursor belum mencapai akhir
            soraarrlist.add(res.getString(res.getColumnIndex("sora_ayat"))); // Tambahkan ayat Surah ke ArrayList
            res.moveToNext(); // Pindahkan kursor ke baris berikutnya
        }
        res.close(); // Tutup kursor

        // Mengonversi ArrayList ayat Surah ke array String
        String[] soraarr = soraarrlist.toArray(new String[0]);
        ////////

        final String[] finalSoraennamearr = soraennamearr; // Array final untuk nama Surah Inggris
        final String[] finalSoraarr = soraarr; // Array final untuk ayat Surah
        final String[] finalSoraarnamearr = soraarnamearr; // Array final untuk nama Surah Arab

        // Menambahkan OnItemClickListener ke ListView
        soraname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F); // Animasi klik tombol
                view.startAnimation(buttonClick); // Memulai animasi
                Intent intent = new Intent(quran.this, quraan_adpter.class); // Membuat Intent baru untuk quraan_adpter
                intent.putExtra("names", finalSoraarnamearr[i]); // Mengirim nama Surah Arab
                intent.putExtra("namesen", finalSoraennamearr[i]); // Mengirim nama Surah Inggris
                intent.putExtra("soraayat", finalSoraarr[i]); // Mengirim ayat Surah
                intent.putExtra("soraamorm", m_or_m[i]); // Mengirim informasi Makkiyah/Madaniyah
                startActivity(intent); // Memulai aktivitas
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Memuat ulang data nama Surah saat aktivitas dilanjutkan
        ArrayList<Surah> soras = mDBHelper.getSoraName();
        final quran_info quranInfo = new quran_info(this, R.layout.list_view_quraan, soras);
        soraname.setAdapter(quranInfo); // Mengatur adapter untuk ListView
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

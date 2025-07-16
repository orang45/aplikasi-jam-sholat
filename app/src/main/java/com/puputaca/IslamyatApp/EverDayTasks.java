package com.puputaca.IslamyatApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EverDayTasks extends AppCompatActivity {

    private ListView mListView; // ListView untuk menampilkan daftar tugas
    private List<Comment> mTweets; // List untuk menyimpan objek tugas (dinamakan 'Comment' di sini)
    private Comment mComment; // Objek tugas tunggal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ever_day_tasks);
        // Mengatur judul aktivitas ke Bahasa Indonesia
        setTitle("Tugas Harian"); // Judul asli "الْمَهَامّ اليَوْمِيَّة - Daily Tasks"
        // Mengatur warna bilah navigasi jika versi Android adalah Lollipop atau lebih baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));}
        // Mengaktifkan tombol kembali di action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = findViewById(R.id.list); // Menghubungkan ListView dengan ID di layout

        mTweets = generateData(); // Memuat data tugas yang ada
        refreshList(); // Memperbarui tampilan daftar

        mListView.setLongClickable(true); // Membuat item ListView bisa diklik lama

        // Listener untuk klik lama pada item daftar (untuk menghapus tugas)
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                deleteOne(pos); // Memanggil metode untuk menghapus satu tugas
                return true; // Mengembalikan true untuk mengonsumsi event klik lama
            }
        });

        // Listener untuk klik biasa pada item daftar (untuk memodifikasi tugas)
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modifyOne(position); // Memanggil metode untuk memodifikasi satu tugas
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu); // Menginflasi menu todo_menu ke dalam Menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani pemilihan item menu
        switch (item.getItemId()) {
            case R.id.deleteall:
                clearDialog(); // Menampilkan dialog untuk menghapus semua tugas
                return true;
            case R.id.addtodo:
                addDialog(); // Menampilkan dialog untuk menambahkan tugas baru
                return true;
            case android.R.id.home:
                onBackPressed(); // Kembali ke aktivitas sebelumnya
                return true;
            default:
                recreate(); // Membuat ulang aktivitas jika item lain dipilih

        }
        return super.onOptionsItemSelected(item); // Memanggil implementasi superclass
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan satu tugas
    private void deleteOne(int pos) {
        final int position = pos;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Selesaikan Tugas"); // Judul dialog dalam Bahasa Indonesia
        alert.setMessage("Apakah Anda sudah menyelesaikan tugas ini dengan sukses?"); // Pesan dialog dalam Bahasa Indonesia

        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteOnePos(position); // Memanggil metode untuk menghapus tugas berdasarkan posisi
                refreshList(); // Memperbarui tampilan daftar
            }
        });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                // Dibatalkan.
            }
        });
        alert.show(); // Menampilkan dialog
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan semua tugas
    private void clearDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hapus Semua Tugas"); // Judul dialog dalam Bahasa Indonesia
        alert.setMessage("Apakah Anda ingin menghapus semua tugas?"); // Pesan dialog dalam Bahasa Indonesia

        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteAll(); // Memanggil metode untuk menghapus semua tugas
                refreshList(); // Memperbarui tampilan daftar
            }
        });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                // Dibatalkan.
            }
        });
        alert.show(); // Menampilkan dialog
    }

    // Metode untuk menampilkan dialog penambahan tugas baru
    private void addDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Tugas Baru"); // Judul dialog dalam Bahasa Indonesia
        alert.setMessage("Tambahkan nama dan deskripsi tugas:"); // Pesan dialog dalam Bahasa Indonesia

        // Membuat EditText untuk nama tugas
        final EditText name = new EditText(this);
        name.setHint("Nama Tugas"); // Petunjuk dalam Bahasa Indonesia

        // Membuat EditText untuk deskripsi tugas
        final EditText text = new EditText(this);
        text.setHint("Deskripsi Tugas"); // Petunjuk dalam Bahasa Indonesia

        // Membuat Checkbox untuk menandai tugas penting
        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText("Tugas Penting"); // Teks checkbox dalam Bahasa Indonesia

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);

        alert.setView(layout); // Mengatur layout kustom ke dialog

        alert.setPositiveButton("Tambah", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                String important;
                if (importantCheck.isChecked()) {
                    important = "y"; // Jika dicentang, atur 'y' (ya)
                } else {
                    important = "n"; // Jika tidak dicentang, atur 'n' (tidak)
                }
                // Jika nama tugas tidak kosong, tambahkan tugas
                if (name.getText().toString().trim().length() > 0) {
                    mComment = new Comment(name.getText().toString(), text.getText().toString(), important);
                    AddItem(mComment); // Memanggil metode untuk menambahkan item
                    refreshList(); // Memperbarui tampilan daftar
                }
            }
        });

        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                // Dibatalkan.
            }
        });
        alert.show(); // Menampilkan dialog
    }

    // Metode untuk menampilkan dialog modifikasi tugas
    private void modifyOne(final int position) {
        mComment = mTweets.get(position); // Mendapatkan objek tugas yang akan dimodifikasi

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Tugas"); // Judul dialog dalam Bahasa Indonesia
        alert.setMessage("Edit nama atau deskripsi tugas:"); // Pesan dialog dalam Bahasa Indonesia

        // Membuat EditText untuk nama tugas, dengan teks yang sudah ada
        final EditText name = new EditText(this);
        name.setText(mComment.getPseudo());

        // Membuat EditText untuk deskripsi tugas, dengan teks yang sudah ada
        final EditText text = new EditText(this);
        text.setText(mComment.getText());

        // Membuat Checkbox untuk menandai tugas penting, dengan status yang sudah ada
        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText("Tugas Penting"); // Teks checkbox dalam Bahasa Indonesia

        if (mComment.getImportant().equals("y")) {
            importantCheck.setChecked(true);
        }

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);

        alert.setView(layout); // Mengatur layout kustom ke dialog

        alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                String important;
                if (importantCheck.isChecked()) {
                    important = "y"; // Jika dicentang, atur 'y' (ya)
                } else {
                    important = "n"; // Jika tidak dicentang, atur 'n' (tidak)
                }
                // Jika nama tugas tidak kosong, modifikasi tugas
                if (name.getText().toString().trim().length() > 0) {
                    mComment = new Comment(name.getText().toString(), text.getText().toString(), important);
                    ModifyItem(position, mComment); // Memanggil metode untuk memodifikasi item
                    refreshList(); // Memperbarui tampilan daftar
                }
            }
        });

        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() { // Teks tombol dalam Bahasa Indonesia
            public void onClick(DialogInterface dialog, int whichButton) {
                // Dibatalkan.
            }
        });
        alert.show(); // Menampilkan dialog
    }

    // Metode untuk memperbarui ListView
    private void refreshList() {
        RowAdapter adapter = new RowAdapter(EverDayTasks.this, mTweets);
        mListView.setAdapter(adapter);
    }

    // Metode untuk memuat data awal dari SharedPreferences
    private List<Comment> generateData() {
        mTweets = new ArrayList<>();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData", null); // Mengambil data JSON dari SharedPreferences

        if (myData != null) {
            try {
                JSONArray jsonArray = new JSONArray(myData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String data = jsonArray.getString(i);
                    String[] splitData = data.split("\\."); // Memisahkan string berdasarkan '.'

                    mTweets.add(new Comment(splitData[0], splitData[1], splitData[2])); // Menambahkan tugas ke daftar
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mTweets;
    }

    // Metode untuk memodifikasi item dalam SharedPreferences
    private void ModifyItem(int position, Comment e) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData", null);

        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(myData);
            jsonArray.remove(position); // Menghapus item lama
            jsonArray.put(e.getPseudo() + "." + e.getText() + "." + e.getImportant()); // Menambahkan item yang dimodifikasi
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        mTweets.remove(position); // Menghapus dari daftar di memori
        mTweets.add(e); // Menambahkan ke daftar di memori

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null); // Menyimpan kembali ke SharedPreferences
        editor.apply();
    }

    // Metode untuk menambahkan item baru ke SharedPreferences
    private void AddItem(Comment e) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData", null);

        JSONArray jsonArray = null;
        if (myData == null) { // Jika belum ada data
            jsonArray = new JSONArray();
            jsonArray.put(e.getPseudo() + "." + e.getText() + "." + e.getImportant());
            mTweets.add(e);
        } else {
            try {
                jsonArray = new JSONArray(myData);
                jsonArray.put(e.getPseudo() + "." + e.getText() + "." + e.getImportant());
                mTweets.add(e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null);
        editor.apply();
    }

    // Metode untuk menghapus satu item berdasarkan posisi dari SharedPreferences
    private void deleteOnePos(int pos) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData", null);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(myData);
            jsonArray.remove(pos); // Menghapus item dari JSONArray
            mTweets.remove(pos); // Menghapus dari daftar di memori
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null); // Menyimpan kembali ke SharedPreferences
        editor.apply();
    }

    // Metode untuk menghapus semua item dari SharedPreferences
    private void deleteAll() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        JSONArray jsonArray = new JSONArray(); // Membuat JSONArray kosong
        mTweets = new ArrayList<>(); // Mengosongkan daftar di memori

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray.toString()); // Menyimpan JSONArray kosong ke SharedPreferences
        editor.apply();
    }
}

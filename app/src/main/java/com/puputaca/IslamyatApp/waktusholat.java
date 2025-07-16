package com.puputaca.IslamyatApp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone; // Import TimeZone

public class waktusholat extends AppCompatActivity {

    private static final String TAG = "WaktuSholatActivity";
    TextView cityname, fagr, shrouq, dohr, asr, magreb, eshaa;
    String url;
    String mIshaa, mFajr, mShrouq, mDhur, mAsr, mMaghrieb;
    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Waktu Sholat"); // Judul dalam Bahasa Indonesia
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));
        }
        setContentView(R.layout.activity_waktu_sholat);
        cityname = findViewById(R.id.citynameid2);
        fagr = findViewById(R.id.fagrid);
        shrouq = findViewById(R.id.shrouqid);
        dohr = findViewById(R.id.dohrid);
        asr = findViewById(R.id.asrid);
        magreb = findViewById(R.id.magrebid);
        eshaa = findViewById(R.id.eshaaid);
        cityname.setText(Mainpage.cityNameGetter);

        url = "https://muslimsalat.com/" + Mainpage.cityNameGetter + ".json?key=e2246567131ccd3ede8061c32bdb1951";
        final ProgressDialog pDialog = new ProgressDialog(waktusholat.this);
        pDialog.setMessage("Sedang menentukan waktu sholat...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            // Ambil waktu dari JSON (biasanya dalam format 12 jam AM/PM)
                            mFajr = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            mShrouq = response.getJSONArray("items").getJSONObject(0).get("shurooq").toString();
                            mDhur = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            mAsr = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            mMaghrieb = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            mIshaa = response.getJSONArray("items").getJSONObject(0).get("isha").toString();

                            // Format ulang waktu ke 24 jam (HH:mm) untuk tampilan
                            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH); // Format dari API
                            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", new Locale("id", "ID")); // Format 24 jam Indonesia

                            // Untuk memastikan zona waktu yang benar (WIB) jika diperlukan, tapi API harusnya sudah lokal
                            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta")); // Set TimeZone ke WIB

                            fagr.setText(outputFormat.format(inputFormat.parse(mFajr)));
                            shrouq.setText(outputFormat.format(inputFormat.parse(mShrouq)));
                            dohr.setText(outputFormat.format(inputFormat.parse(mDhur)));
                            asr.setText(outputFormat.format(inputFormat.parse(mAsr)));
                            magreb.setText(outputFormat.format(inputFormat.parse(mMaghrieb)));
                            eshaa.setText(outputFormat.format(inputFormat.parse(mIshaa)));

                            Toast.makeText(waktusholat.this, "Waktu sholat berhasil diatur berdasarkan lokasi Anda", Toast.LENGTH_SHORT).show();

                            // --- Jadwalkan Notifikasi (menggunakan waktu yang sudah diparse, bukan string tampilan) ---
                            // Gunakan kembali inputFormat untuk parse string waktu ke Date, karena schedulePrayerAlarm membutuhkannya
                            schedulePrayerAlarm("Subuh", mFajr, 1);
                            schedulePrayerAlarm("Dzuhur", mDhur, 2);
                            schedulePrayerAlarm("Ashar", mAsr, 3);
                            schedulePrayerAlarm("Maghrib", mMaghrieb, 4);
                            schedulePrayerAlarm("Isya", mIshaa, 5);

                        } catch (ParseException | JSONException e) { // Tangani kedua jenis exception
                            Toast.makeText(waktusholat.this, "Gagal mengatur waktu sholat berdasarkan lokasi Anda", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(waktusholat.this, "Kesalahan dalam mengatur waktu sholat, mohon periksa koneksi internet Anda", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Menjadwalkan alarm untuk waktu sholat tertentu.
     * @param prayerName Nama sholat (misal: "Subuh", "Dzuhur").
     * @param prayerTime Waktu sholat dalam format string (misal: "04:30 AM").
     * @param requestCode Kode permintaan unik untuk PendingIntent.
     */
    private void schedulePrayerAlarm(String prayerName, String prayerTime, int requestCode) {
        try {
            // Kita masih menggunakan SimpleDateFormat("hh:mm a", Locale.ENGLISH) untuk mem-parsing
            // waktu yang datang dari API (yang formatnya 12 jam AM/PM).
            // TimeZone disetel ke Asia/Jakarta untuk memastikan perhitungan waktu yang tepat untuk alarm.
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta")); // Penting: Set TimeZone untuk parsing

            Date prayerDate = sdf.parse(prayerTime);

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")); // Gunakan TimeZone WIB untuk Calendar
            calendar.setTime(prayerDate);

            // Setel tahun, bulan, dan hari ke tanggal hari ini
            Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")); // Gunakan TimeZone WIB
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));

            // Jika waktu sholat untuk hari ini sudah terlewat, jadwalkan untuk besok
            if (calendar.before(currentCalendar)) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(this, PrayerAlarmReceiver.class);
            intent.putExtra("PRAYER_NAME", prayerName);
            // Kirim waktu sholat dalam format 24 jam untuk notifikasi
            SimpleDateFormat notificationTimeFormat = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));
            notificationTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            intent.putExtra("PRAYER_TIME", notificationTimeFormat.format(calendar.getTime()));


            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                Log.d(TAG, "Alarm dijadwalkan untuk " + prayerName + " pada " + notificationTimeFormat.format(calendar.getTime()));
            }

        } catch (ParseException e) {
            Log.e(TAG, "Error saat parsing waktu sholat: " + prayerTime + " - " + e.getMessage());
            Toast.makeText(this, "Kesalahan dalam format waktu sholat.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sholat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void refresh_menu(MenuItem item) {
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
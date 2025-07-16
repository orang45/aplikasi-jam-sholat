package com.puputaca.IslamyatApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Mainpage extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static String cityNameGetter = "Semarang";
    TextView year, day, month, currentTimeTextView;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    boolean gps_enabled, network_enabled;
    private AddressResultReceiver resultReceiver;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private ProgressDialog locationProgressDialog;

    private Handler clockHandler = new Handler(Looper.getMainLooper());
    private Runnable updateClockRunnable;


    static double gmod(double n, double m) {
        return ((n % m) + m) % m;
    }

    /**
     * Menghitung tanggal Hijriyah berdasarkan tanggal Masehi menggunakan metode perhitungan (seperti Ummu al-Qura/Kuwaiti).
     * Penting: Hasil dari perhitungan ini mungkin berbeda 1-2 hari dari pengumuman resmi pemerintah Indonesia
     * karena pemerintah menggunakan metode rukyatul hilal (pengamatan bulan) dan kriteria Imkanur Rukyah.
     *
     * @param adjust Jika true, akan ada penyesuaian (biasanya +/- 1 hari) berdasarkan parameter `adj` di dalam fungsi.
     * Dalam implementasi ini, `adj` akan menjadi 0 jika `adjust` adalah true.
     * @return Array double berisi informasi tanggal Masehi dan Hijriyah.
     * myRes[5] = tanggal Hijriyah
     * myRes[6] = bulan Hijriyah (0-indexed)
     * myRes[7] = tahun Hijriyah
     */
    static double[] indonesiacalendar(boolean adjust) { // Nama fungsi diubah
        Calendar today = Calendar.getInstance();
        int adj = 0; // Inisialisasi penyesuaian

        // Logika penyesuaian hari (sesuai logika asli Anda)
        // Jika adjust true, adj = 0. Jika adjust false, adj = 1.
        // Anda bisa mengubah logika ini jika ingin 'adjust=true' berarti menggeser tanggal 1 hari.
        if (adjust) {
            adj = 0;
        } else {
            adj = 1;
        }

        long adjustmili = (long) 1000 * 60 * 60 * 24 * adj;
        long todaymili = today.getTimeInMillis() + adjustmili;
        today.setTimeInMillis(todaymili);

        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);

        double m = month + 1;
        double y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        double a = Math.floor(y / 100.);
        double b = 2 - a + Math.floor(a / 4.);

        if (y < 1583)
            b = 0;
        if (y == 1582) {
            if (m > 10)
                b = -10;
            if (m == 10) {
                b = 0;
                if (day > 4)
                    b = -10;
            }
        }

        double jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day
                + b - 1524;

        b = 0;
        if (jd > 2299160) {
            a = Math.floor((jd - 1867216.25) / 36524.25);
            b = 1 + a - Math.floor(a / 4.);
        }
        double bb = jd + b + 1524;
        double cc = Math.floor((bb - 122.1) / 365.25);
        double dd = Math.floor(365.25 * cc);
        double ee = Math.floor((bb - dd) / 30.6001);
        day = (bb - dd) - Math.floor(30.6001 * ee);
        month = ee - 1;
        if (ee > 13) {
            cc += 1;
            month = ee - 13;
        }
        year = cc - 4716;

        double wd = gmod(jd + 1, 7) + 1;

        double iyear = 10631. / 30.;
        double epochastro = 1948084;
        double epochcivil = 1948085;

        double shift1 = 8.01 / 60.;

        double z = jd - epochastro;
        double cyc = Math.floor(z / 10631.);
        z = z - 10631 * cyc;
        double j = Math.floor((z - shift1) / iyear);
        double iy = 30 * cyc + j;
        z = z - Math.floor(j * iyear + shift1);
        double im = Math.floor((z + 28.5001) / 29.5);
        if (im == 13)
            im = 12;
        double id = z - Math.floor(29.5001 * im - 29);

        double[] myRes = new double[8];

        myRes[0] = day; // calculated day (CE)
        myRes[1] = month - 1; // calculated month (CE)
        myRes[2] = year; // calculated year (CE)
        myRes[3] = jd - 1; // julian day number
        myRes[4] = wd - 1; // weekday number
        myRes[5] = id; // islamic date
        myRes[6] = im - 1; // islamic month
        myRes[7] = iy; // islamic year

        return myRes;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color2));
        }
        setTitle("Islamyat");

        currentTimeTextView = findViewById(R.id.current_time); // Inisialisasi TextView jam

        // Setup the clock update
        updateClockRunnable = new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                clockHandler.postDelayed(this, 1000); // Perbarui setiap 1 detik
            }
        };

        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        displayHijriDate(); // Menampilkan tanggal Hijriyah

        resultReceiver = new AddressResultReceiver(new Handler(Looper.getMainLooper()));

        locationProgressDialog = new ProgressDialog(this);
        locationProgressDialog.setMessage("Mendapatkan lokasi Anda...");
        locationProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mulai pembaruan jam saat Activity di-resume
        clockHandler.post(updateClockRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hentikan pembaruan jam saat Activity di-pause
        clockHandler.removeCallbacks(updateClockRunnable);
    }

    // Metode untuk memperbarui tampilan jam digital
    private void updateCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", new Locale("id", "ID"));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta")); // Set zona waktu ke WIB (Waktu Indonesia Barat)
        String currentTime = sdf.format(new Date());
        currentTimeTextView.setText(currentTime + " WIB");
    }


    private void displayHijriDate() {
        String[] iMonthNames = {"Muharram", "Safar", "Rabiul Awal", "Rabiul Akhir", "Jumadil Awal", "Jumadil Akhir", "Rajab", "Syaban", "Ramadan", "Syawal", "Zulkaidah", "Zulhijjah"};
        // Gunakan nama fungsi yang baru: indonesiacalendar
        boolean dayTest = true; // Parameter ini mengontrol penyesuaian hari di indonesiacalendar
        double[] iDate = indonesiacalendar(dayTest); // Panggil fungsi dengan nama baru
        int isday = (int) iDate[5];
        String ismonth = iMonthNames[(int) iDate[6]];
        int isyear = (int) iDate[7];

        year = findViewById(R.id.year);
        day = findViewById(R.id.mday);
        month = findViewById(R.id.month);

        year.setText(isyear + " H");
        month.setText(ismonth);
        day.setText(isday + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void aboutme_menu(MenuItem item) {
        Dialog dialog = new Dialog(Mainpage.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.aboutme);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void sadka_menu(MenuItem item) {
        Dialog dialog = new Dialog(Mainpage.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.sedekah);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void facebookbut(View view) {
        view.startAnimation(buttonClick);
        Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/abdalrahman.khaled.54"));
        startActivity(i1);
    }

    public void whatsappbut(View view) {
        view.startAnimation(buttonClick);
        Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/081802482154"));
        startActivity(i2);
    }

    public void instagrambut(View view) {
        view.startAnimation(buttonClick);
        Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/puputgifa/"));
        startActivity(i3);
    }

    public void linkedinbut(View view) {
        view.startAnimation(buttonClick);
        Intent i4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/abdulrhman-khaled-91a3b821a/"));
        startActivity(i4);
    }

    public void gitHubbut(View view) {
        view.startAnimation(buttonClick);
        Intent i5 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bodykh"));
        startActivity(i5);
    }

    public void button_Salah(View view) {
        view.startAnimation(buttonClick);
        locationProgressDialog.show();
        checkLocationPermissionAndGetPrayerTimes();
    }

    public void button_tasks(View view) {
        view.startAnimation(buttonClick);
        Intent intent = new Intent(Mainpage.this, EverDayTasks.class);
        startActivity(intent);
    }

    public void button_quraan(View v) {
        v.startAnimation(buttonClick);
        Intent intent = new Intent(Mainpage.this, quran.class);
        startActivity(intent);
    }

    public void button_sb7aa(View v) {
        v.startAnimation(buttonClick);
        Intent intent = new Intent(Mainpage.this, tasbih.class);
        startActivity(intent);
    }

    public void button_qipla(View v) {
        v.startAnimation(buttonClick);
        Intent intent = new Intent(Mainpage.this, qibla.class);
        startActivity(intent);
    }

    public void button_stories(View v) {
        v.startAnimation(buttonClick);
        Intent intent = new Intent(Mainpage.this, stories.class);
        startActivity(intent);
    }


    private void checkLocationPermissionAndGetPrayerTimes() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Mainpage.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled && !network_enabled) {
                locationProgressDialog.dismiss();
                showLocationAlertDialog();
            } else {
                getCurrentLocation();
            }
        }
    }

    private void showLocationAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Aktifkan Lokasi");
        dialog.setMessage("Waktu sholat tidak akan ditentukan akurat tanpa akses lokasi. Mohon aktifkan GPS atau Wi-Fi untuk deteksi lokasi.");
        dialog.setPositiveButton("Pengaturan Lokasi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                locationProgressDialog.dismiss();
                Intent intent = new Intent(Mainpage.this, waktusholat.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermissionAndGetPrayerTimes();
            } else {
                locationProgressDialog.dismiss();
                Toast.makeText(this, "Izin lokasi ditolak. Menggunakan lokasi default.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Mainpage.this, waktusholat.class);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create(); // Menggunakan metode create()
        locationRequest.setInterval(10000); // Interval update lokasi
        locationRequest.setFastestInterval(3000); // Interval tercepat
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Menggunakan konstanta prioritas lama

        LocationServices.getFusedLocationProviderClient(Mainpage.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        // Hentikan update lokasi setelah mendapatkan satu hasil
                        LocationServices.getFusedLocationProviderClient(Mainpage.this)
                                .removeLocationUpdates(this);

                        if (locationProgressDialog.isShowing()) {
                            locationProgressDialog.dismiss();
                        }

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            Location location = locationResult.getLocations().get(latestLocationIndex);

                            fetchAddressFromLatLong(location);
                        } else {
                            Toast.makeText(Mainpage.this, "Gagal mendapatkan lokasi. Menggunakan lokasi default.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Mainpage.this, waktusholat.class);
                            startActivity(intent);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                cityNameGetter = resultData.getString(Constants.RESULT_DATA_KEY);
                Toast.makeText(Mainpage.this, "Lokasi terdeteksi: " + cityNameGetter, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Mainpage.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(Mainpage.this, waktusholat.class);
            startActivity(intent);
        }
    }
}
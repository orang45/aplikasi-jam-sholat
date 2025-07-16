package com.puputaca.IslamyatApp;

public class Surah {
    // Variabel untuk menyimpan nama Surah dalam bahasa Arab
    private String arsora;
    // Variabel untuk menyimpan nama Surah dalam bahasa Inggris
    private String ensora;

    // Konstruktor untuk kelas Sora
    public Surah(String arsora, String ensora) {
        this.arsora = arsora; // Menginisialisasi nama Surah Arab
        this.ensora = ensora; // Menginisialisasi nama Surah Inggris
    }

    // Metode getter untuk mendapatkan nama Surah dalam bahasa Arab
    public String getArsora() {
        return arsora;
    }

    // Metode setter untuk mengatur nama Surah dalam bahasa Arab
    public void setArsora(String arsora) {
        this.arsora = arsora;
    }

    // Metode getter untuk mendapatkan nama Surah dalam bahasa Inggris
    public String getEnsora() {
        return ensora;
    }

    // Metode setter untuk mengatur nama Surah dalam bahasa Inggris
    public void setEnsora(String ensora) {
        this.ensora = ensora;
    }
}
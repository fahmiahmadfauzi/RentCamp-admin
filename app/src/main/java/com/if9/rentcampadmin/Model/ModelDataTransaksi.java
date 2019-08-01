package com.if9.rentcampadmin.Model;

public class ModelDataTransaksi {
    String id_transaksi;
    String tgl;
    String id;
    String id_toko;
    String id_alat;
    String nama_alat;
    String harga_alat;
    String status;

    public ModelDataTransaksi() {
    }

    public ModelDataTransaksi(String id_transaksi, String tgl, String id, String id_toko, String id_alat, String nama_alat, String harga_alat, String status) {

    }

    public String getId_alat() {
        return id_alat;
    }

    public void setId_alat(String id_alat) {
        this.id_alat = id_alat;
    }

    public String getNama_alat() {
        return nama_alat;
    }

    public void setNama_alat(String nama_alat) {
        this.nama_alat = nama_alat;
    }


    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_toko() {
        return id_toko;
    }

    public void setId_toko(String id_toko) {
        this.id_toko = id_toko;
    }


    public String getHarga_alat() {
        return harga_alat;
    }

    public void setHarga_alat(String harga_alat) {
        this.harga_alat = harga_alat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

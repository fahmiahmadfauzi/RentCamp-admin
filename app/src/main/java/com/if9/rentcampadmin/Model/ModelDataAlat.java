package com.if9.rentcampadmin.Model;

public class ModelDataAlat {
    String id_alat;
    String nama;
    String harga;
    String foto;


    String stok;

    public ModelDataAlat() {
    }

    public ModelDataAlat(String id_toko, String nama, String alamat, String foto, String stok) {

    }


    public String getId_alat() {
        return id_alat;
    }

    public void setId_alat(String id_alat) {
        this.id_alat = id_alat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }


}

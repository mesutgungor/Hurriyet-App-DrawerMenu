package com.example.mesutgungor.drawer;


 class Haber {
     String haberid;
     String haberresmi;
     String haberbasligi;
     String habertarihi;
     String haberinkategorisi;
     String haberinicerigi;
     String haberUrl;
     String habermetni;

     public Haber(String haberid, String haberresmi, String haberbasligi, String habertarihi, String haberinkategorisi, String haberinicerigi, String haberUrl, String habermetni) {
         this.haberid = haberid;
         this.haberresmi = haberresmi;
         this.haberbasligi = haberbasligi;
         this.habertarihi = habertarihi;
         this.haberinkategorisi = haberinkategorisi;
         this.haberinicerigi = haberinicerigi;
         this.haberUrl = haberUrl;
         this.habermetni = habermetni;

     }

     public String getHaberid() {
         return haberid;
     }

     public void setHaberid(String haberid) {
         this.haberid = haberid;
     }

     public String getHaberresmi() {
         return haberresmi;
     }

     public void setHaberresmi(String haberresmi) {
         this.haberresmi = haberresmi;
     }

     public String getHaberbasligi() {
         return haberbasligi;
     }

     public void setHaberbasligi(String haberbasligi) {
         this.haberbasligi = haberbasligi;
     }

     public String getHabertarihi() {
         return habertarihi;
     }

     public void setHabertarihi(String habertarihi) {
         this.habertarihi = habertarihi;
     }

     public String getHaberinkategorisi() {
         return haberinkategorisi;
     }

     public void setHaberinkategorisi(String haberinkategorisi) {
         this.haberinkategorisi = haberinkategorisi;
     }

     public String getHaberinicerigi() {
         return haberinicerigi;
     }

     public void setHaberinicerigi(String haberinicerigi) {
         this.haberinicerigi = haberinicerigi;
     }

     public String getHaberUrl() {
         return haberUrl;
     }

     public void setHaberUrl(String haberUrl) {
         this.haberUrl = haberUrl;
     }

     public String getHaberMetni() {
         return habermetni;
     }

     public void setHaberMetni(String habermetni) {
         this.habermetni = habermetni;
     }
 }

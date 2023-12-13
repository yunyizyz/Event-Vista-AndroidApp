package com.yunyizapp.ticketmastersearch;

import androidx.room.Entity;

@Entity(tableName = "event")
public class Event {
    private String name;
    private String url;
    private String imageUrl;
    private String venue;
    private String date;
    private String time;
    private String[] artists;
    private String genre;
    private String subgenre;
    private String subgenre1;
    private String subgenre2;
    private String subgenre3;
    private String maxprice;
    private String minprice;
    private String status;
    private String seatmap;
    private String address;
    private String city;
    private String state;
    private String contact;
    private String oh;
    private String gr;
    private String cr;


    public Event(String name, String url, String imageUrl, String date, String time, String venue, String genre, String[] artists, String subgenre,String subgenre1,String subgenre2,String subgenre3, String maxprice, String minprice, String status, String seatmap, String address, String city, String state, String contact, String oh,String gr,String cr) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.genre = genre;
        this.artists = artists;
        this.subgenre = subgenre;
        this.subgenre1 = subgenre1;
        this.subgenre2= subgenre2;
        this.subgenre3 = subgenre3;
        this.maxprice = maxprice;
        this.minprice = minprice;
        this.status = status;
        this.seatmap = seatmap;
        this.address = address;
        this.city = city;
        this.state = state;
        this.contact = contact;
        this.oh = oh;
        this.gr = gr;
        this.cr = cr;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getGenre() {
        return genre;
    }

    public String[] getArtists() {
        return artists;
    }

    public String getSubgenre() {
        return subgenre;
    }

    public String getMaxPrice() {
        return maxprice;
    }

    public String getMinPrice() {
        return minprice;
    }

    public String getStatus() {
        return status;
    }

    public String getSeatmap() {
        return seatmap;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getContact() {
        return contact;
    }

    public String getOh() {
        return oh;
    }
    public String getGr() {
        return gr;
    }
    public String getCr() {
        return cr;
    }
}
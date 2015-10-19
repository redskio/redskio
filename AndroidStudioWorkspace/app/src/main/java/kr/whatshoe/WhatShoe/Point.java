package kr.whatshoe.WhatShoe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class Point implements Parcelable{
    private String title;
    private String date; //was boolean but int for check the order count
    private int price;
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };


    public Point() {

    }

    public Point(String title, String date, int price) {
        this.title = title;
        this.date = date;
        this.price = price;
    }

    public Point(Parcel parcel){
        this.title=parcel.readString();
        this.date = parcel.readString();
        this.price = parcel.readInt();
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice(){
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeInt(price);
    }

}

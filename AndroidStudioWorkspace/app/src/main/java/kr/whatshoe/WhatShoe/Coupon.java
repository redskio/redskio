package kr.whatshoe.WhatShoe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class Coupon implements Parcelable{
    private String desc;
    private String code;
    private int price;
    private String owner;
    private int status;
    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };


    public Coupon() {

    }

    public Coupon(String desc, String code, int price, String owner , int status) {
        this.desc = desc;
        this.code = code;
        this.price = price;
        this.owner = owner;
        this.status = status;
    }

    public Coupon(Parcel parcel){
        this.desc = parcel.readString();
        this.code = parcel.readString();
        this.price = parcel.readInt();
        this.owner = parcel.readString();
        this.status = parcel.readInt();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeString(code);
        dest.writeInt(price);
        dest.writeString(owner);
        dest.writeInt(status);
    }

}

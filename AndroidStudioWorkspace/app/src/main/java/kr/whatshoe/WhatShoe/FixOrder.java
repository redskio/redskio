package kr.whatshoe.WhatShoe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class FixOrder implements Parcelable{
    private String contentText;
    private int isChecked; //was boolean but int for check the order count
    private int price;
    public static final Parcelable.Creator<FixOrder> CREATOR = new Creator<FixOrder>() {
        @Override
        public FixOrder createFromParcel(Parcel in) {
            return new FixOrder(in);
        }

        @Override
        public FixOrder[] newArray(int size) {
            return new FixOrder[size];
        }
    };


    public FixOrder() {

    }

    public FixOrder(String contentText, int isChecked, int price) {
        this.contentText = contentText;
        this.isChecked = isChecked;
        this.price = price;
    }

    public FixOrder(Parcel parcel){
        this.contentText=parcel.readString();
        this.isChecked = parcel.readInt();
        this.price = parcel.readInt();
    }
    public int getIsChecked() {
        return isChecked;
    }
    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public String getContentText() {

        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
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
        dest.writeString(contentText);
        dest.writeInt(isChecked);
        dest.writeInt(price);
    }

}

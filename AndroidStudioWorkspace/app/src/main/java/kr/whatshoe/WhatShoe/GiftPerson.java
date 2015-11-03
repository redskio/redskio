package kr.whatshoe.whatShoe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class GiftPerson implements Parcelable{
    private String name;
    private String phoneNum; //was boolean but int for check the order count
    private int num;
    public static final Creator<GiftPerson> CREATOR = new Creator<GiftPerson>() {
        @Override
        public GiftPerson createFromParcel(Parcel in) {
            return new GiftPerson(in);
        }

        @Override
        public GiftPerson[] newArray(int size) {
            return new GiftPerson[size];
        }
    };


    public GiftPerson() {

    }

    public GiftPerson(String name, String phoneNum, int num) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.num = num;
    }

    public GiftPerson(Parcel parcel){
        this.name =parcel.readString();
        this.phoneNum = parcel.readString();
        this.num = parcel.readInt();
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNum);
        dest.writeInt(num);
    }

}

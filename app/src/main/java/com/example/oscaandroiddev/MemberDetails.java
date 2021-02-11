package com.example.oscaandroiddev;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberDetails implements Parcelable {

    private String fullName;
    private String oscaID;
    private String password;
    private String picture;
    private String sex;
    private String conNumber;
    private String birthDate;
    private String memDate;
    private String address;
    private String nfc;
    private String account;

    public MemberDetails(String fullName, String oscaID, String password, String picture, String sex, String conNumber, String birthDate, String memDate, String address, String nfc, String account) {
        this.fullName = fullName;
        this.oscaID = oscaID;
        this.password = password;
        this.picture = picture;
        this.sex = sex;
        this.conNumber = conNumber;
        this.birthDate = birthDate;
        this.memDate = memDate;
        this.address = address;
        this.nfc = nfc;
        this.account = account;
    }

    protected MemberDetails(Parcel in) {
        fullName = in.readString();
        oscaID = in.readString();
        password = in.readString();
        picture = in.readString();
        sex = in.readString();
        conNumber = in.readString();
        birthDate = in.readString();
        memDate = in.readString();
        address = in.readString();
        nfc = in.readString();
        account = in.readString();
    }

    public static final Creator<MemberDetails> CREATOR = new Creator<MemberDetails>() {
        @Override
        public MemberDetails createFromParcel(Parcel in) {
            return new MemberDetails(in);
        }

        @Override
        public MemberDetails[] newArray(int size) {
            return new MemberDetails[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public String getOscaID() {
        return oscaID;
    }

    public String getPassword() {return password; }

    public String getPicture() {
        return picture;
    }

    public String getSex() {
        return sex;
    }

    public String getConNumber() {
        return conNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getMemDate() {
        return memDate;
    }

    public String getAddress() {
        return address;
    }

    public String getNFC() {
        return nfc;
    }

    public String getAccount() {
        return account;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(oscaID);
        dest.writeString(password);
        dest.writeString(picture);
        dest.writeString(sex);
        dest.writeString(conNumber);
        dest.writeString(birthDate);
        dest.writeString(memDate);
        dest.writeString(address);
        dest.writeString(nfc);
        dest.writeString(account);
    }
}

package net.codealizer.thegradebook.apis.ic.district;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by Pranav on 10/8/16.
 */

public class DistrictInfo implements Parcelable {
    private String districtAppName;
    private String districtBaseURL;
    private String districtName;
    private int districtNumber;
    private String stateCode;

    public String getDistrictAppName()
    {
        return districtAppName;
    }

    public String getDistrictBaseURL()
    {
        return districtBaseURL;
    }

    public String getDistrictName()
    {
        return districtName;
    }

    public int getDistrictNumber()
    {
        return districtNumber;
    }

    public String getDistrictStateCode()
    {
        return stateCode;
    }

    public void setDistrictAppName(String districtAppName) {
        this.districtAppName = districtAppName;
    }

    public void setDistrictBaseURL(String districtBaseURL) {
        this.districtBaseURL = districtBaseURL;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setDistrictNumber(int districtNumber) {
        this.districtNumber = districtNumber;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public DistrictInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, this.getClass());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(districtAppName);
        parcel.writeString(districtBaseURL);
        parcel.writeString(districtName);
        parcel.writeInt(districtNumber);
        parcel.writeString(stateCode);
    }

    public static final Parcelable.Creator<DistrictInfo> CREATOR
            = new Parcelable.Creator<DistrictInfo>() {
        public DistrictInfo createFromParcel(Parcel in) {
            return new DistrictInfo(in);
        }

        public DistrictInfo[] newArray(int size) {
            return new DistrictInfo[size];
        }
    };

    private DistrictInfo(Parcel in) {
        districtAppName = in.readString();
        districtBaseURL = in.readString();
        districtName = in.readString();
        districtNumber = in.readInt();
        stateCode = in.readString();
    }
}

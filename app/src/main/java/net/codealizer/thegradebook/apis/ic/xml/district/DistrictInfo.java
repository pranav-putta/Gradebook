package net.codealizer.thegradebook.apis.ic.xml.district;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Pranav on 10/8/16.
 */

public class DistrictInfo implements Parcelable {
    public static final Parcelable.Creator<DistrictInfo> CREATOR
            = new Parcelable.Creator<DistrictInfo>() {
        public DistrictInfo createFromParcel(Parcel in) {
            return new DistrictInfo(in);
        }

        public DistrictInfo[] newArray(int size) {
            return new DistrictInfo[size];
        }
    };
    private String districtAppName;
    private String districtBaseURL;
    private String districtName;
    private int districtNumber;
    private String stateCode;

    private DistrictInfo(Parcel in) {
        districtAppName = in.readString();
        districtBaseURL = in.readString();
        districtName = in.readString();
        districtNumber = in.readInt();
        stateCode = in.readString();
    }

    public DistrictInfo(String json) {
        JsonObject mJson = new JsonParser().parse(json).getAsJsonObject();

        this.districtAppName = mJson.get("districtAppName").getAsString();
        this.districtBaseURL = mJson.get("districtBaseURL").getAsString();
        this.districtName = mJson.get("districtName").getAsString();
        this.districtNumber = mJson.get("districtNumber").getAsInt();
        this.stateCode = mJson.get("stateCode").getAsString();

    }

    public String getDistrictAppName() {
        return districtAppName;
    }

    public void setDistrictAppName(String districtAppName) {
        this.districtAppName = districtAppName;
    }

    public String getDistrictBaseURL() {
        return districtBaseURL;
    }

    public void setDistrictBaseURL(String districtBaseURL) {
        this.districtBaseURL = districtBaseURL;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getDistrictNumber() {
        return districtNumber;
    }

    public void setDistrictNumber(int districtNumber) {
        this.districtNumber = districtNumber;
    }

    public String getDistrictStateCode() {
        return stateCode;
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
}

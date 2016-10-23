package net.codealizer.thegradebook.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 10/16/16.
 */

public class StateSuggestion implements SearchSuggestion
{

	public static final Creator<StateSuggestion> CREATOR = new Creator<StateSuggestion>()
	{
		@Override
		public StateSuggestion createFromParcel(Parcel parcel)
		{
			return new StateSuggestion(parcel);
		}

		@Override
		public StateSuggestion[] newArray(int i)
		{
			return new StateSuggestion[i];
		}
	};
	public String mDistrictCode;
	String mSchoolName;

	public StateSuggestion(String suggestion)
	{
		this.mSchoolName = suggestion.toLowerCase();
	}

	public StateSuggestion(JsonObject data)
	{
		this.mSchoolName = data.get("district_name").getAsString();
		this.mDistrictCode = data.get("district_code").getAsString();
	}

	public StateSuggestion(Parcel source)
	{
		this.mSchoolName = source.readString();
	}

	public static ArrayList<String> asNameList(List<StateSuggestion> s)
	{
		ArrayList<String> mSuggestions = new ArrayList<>();

		for (StateSuggestion suggestion : s)
		{
			mSuggestions.add(suggestion.getBody());
		}

		return mSuggestions;
	}

	@Override
	public String getBody()
	{
		return WordUtils.capitalize(mSchoolName.toLowerCase());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(mSchoolName);
		parcel.writeString(mDistrictCode);
	}

	@Override
	public String toString()
	{
		return mSchoolName;
	}
}

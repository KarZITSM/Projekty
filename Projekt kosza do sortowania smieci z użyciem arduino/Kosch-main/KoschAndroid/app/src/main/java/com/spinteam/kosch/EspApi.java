package com.spinteam.kosch;

import android.util.Log;

import com.spinteam.kosch.model.Trash;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EspApi {

	public static final long CLOSE_DELAY_MS = 3000;
	public static final int TRASH_MAX_DISTANCE = 25;

	public static String callApi(String uri) throws Exception {
		HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://" + Prefs.get().getString(Constants.PREF_IP_OR_HOSTNAME, "kosch") + "/api" + uri).openConnection();
		return IOUtils.toString(urlConnection.getInputStream(), StandardCharsets.UTF_8);
	}

	public static void openAndCloseTrash(int trashIds) throws Exception {
		EspApi.openTrashes(trashIds);
		Thread.sleep(CLOSE_DELAY_MS);
		EspApi.closeTrashes(trashIds);
	}

	public static ArrayList<Trash> getInfo() throws Exception {
		String response = callApi("/?action=info");
		Log.e("ESPAPI", "serverresponse=" + response);
		JSONObject object = new JSONObject(response);
		JSONArray trashesArr = object.getJSONArray("t");
		ArrayList<Trash> trashes = new ArrayList<>();
		for (int i = 0; i < trashesArr.length(); i++) {
			JSONObject trashObject = trashesArr.getJSONObject(i);
			trashes.add(new Trash(
					trashObject.getInt("i"),
					/*trashObject.getString("name")*/"x",
					trashObject.getInt("o") == 1,
					trashObject.getInt("d")));
		}
		return trashes;
	}

	public static void openTrashes(int trashIds) throws Exception {
		String response = callApi("/?action=open&trashIds=" + trashIds);
	}

	public static void closeTrashes(int trashIds) throws Exception {
		String response = callApi("/?action=close&trashIds=" + trashIds);
	}

}
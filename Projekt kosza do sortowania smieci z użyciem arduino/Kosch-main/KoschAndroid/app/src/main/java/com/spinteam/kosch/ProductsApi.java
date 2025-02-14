package com.spinteam.kosch;

import android.util.Log;

import com.spinteam.kosch.model.Trash;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ProductsApi {

	public static final String BASE_URL = "https://jtrz.pl/kosch";

	public static String callApi(String uri) throws Exception {
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(BASE_URL + uri).openConnection();
		Log.e("PRODUCTSAPI", "url=" + BASE_URL + uri);
		return IOUtils.toString(urlConnection.getInputStream(), StandardCharsets.UTF_8);
	}

	public static Trash getTrashForEan(String eanNumber) throws Exception {
		String response = callApi("/api.php?ean=" + URLEncoder.encode(eanNumber, "UTF-8"));
		Log.e("PRODUCTSAPI", "serverresponse=" + response);
		JSONObject object = new JSONObject(response);
		int id = 0;
		if (object.getBoolean("success"))
			id = object.getInt("trashId");
		return Trash.byId(id);
	}

	public static boolean addTrashIdForEanToDatabase(String eanNumber, int trashId) throws Exception {
		String response = callApi("/api.php?ean=" + URLEncoder.encode(eanNumber, "UTF-8") +"&trashId=" + trashId);
		Log.e("PRODUCTSAPI", "serverresponse=" + response);
		JSONObject object = new JSONObject(response);
		return object.getBoolean("success");
	}

}
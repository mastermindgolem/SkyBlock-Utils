package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.NoteForDecompilers;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import logger.Logger;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUtil {
	@NoteForDecompilers("a session variable does not mean i am trying to rat you. I am simply getting the player's username.")
	private static final String username = Main.mc.getSession().getUsername();
	@NoteForDecompilers("Just getting uuid, why ware you still here .w.")
	private static final String uuid = Main.mc.getSession().getPlayerID();

	public static SSLSocketFactory getAllowAllFactory() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return sc.getSocketFactory();
		} catch (Exception ignored) {
		}
		return null;
	}

	private RequestData printErrorDebug(URL url, String jsonAsText, int status, Exception e) {
		Logger.error("Exception when fetching data... (uc maybe null)", e);
		Logger.error("URL was: {}", url != null ? url.toExternalForm() : "null url");
		Logger.error("Json data: {}", jsonAsText);
		JsonObject errorObject = new JsonObject();
		errorObject.addProperty("success", false);
		errorObject.addProperty("status", status);
		return new RequestData(status, new HashMap<>(), errorObject);
	}

	private HttpsURLConnection getHttpsURLConnection(URL url, String post) throws IOException {
		HttpsURLConnection uc;
		uc = (HttpsURLConnection) url.openConnection();
		uc.setSSLSocketFactory(getAllowAllFactory());
		uc.setReadTimeout(20000);
		uc.setConnectTimeout(20000);
		uc.setRequestMethod(post);

		try {
			uc.addRequestProperty("UUID", uuid);
		} catch (NullPointerException ignored) {return null;}
		uc.addRequestProperty("IGN", username);
		uc.addRequestProperty("User-Agent", "golemmod");
		uc.setRequestProperty("Content-Type", "application/json; utf-8");
		uc.setRequestProperty("Accept", "application/json");
		return uc;
	}

	public RequestData sendGetRequest(String urlString) {
		URL url = null;
		JsonElement jsonData;
		HttpsURLConnection uc;
		String jsonAsText = "";
		int status = -1;
		try {
			url = new URL(urlString);
			uc = getHttpsURLConnection(url, "GET");
			if (uc == null) return null;
			status = uc.getResponseCode();
			InputStream inputStream;
			if (status != 200) {
				inputStream = uc.getErrorStream();
			} else {
				inputStream = uc.getInputStream();
			}
			jsonAsText = IOUtils.toString(inputStream);
			jsonData = new JsonParser().parse(jsonAsText);
			return new RequestData(status, uc.getHeaderFields(), jsonData);
		} catch (IOException | JsonSyntaxException | JsonIOException e) {
			return printErrorDebug(url, jsonAsText, status, e);
		}
	}

	public RequestData sendPostRequest(String urlString, JsonObject postData) {
		URL url = null;
		JsonElement jsonData;
		HttpsURLConnection uc;
		String jsonAsText = "";
		int status = -1;
		try {
			url = new URL(urlString);
			uc = getHttpsURLConnection(url, "POST");
			uc.setDoOutput(true);
			try (OutputStream os = uc.getOutputStream()) {
				byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			status = uc.getResponseCode();
			InputStream inputStream;
			if (status != 200) {
				inputStream = uc.getErrorStream();
			} else {
				inputStream = uc.getInputStream();
			}
			jsonAsText = IOUtils.toString(inputStream);
			jsonData = new JsonParser().parse(jsonAsText);
			return new RequestData(status, uc.getHeaderFields(), jsonData);
		} catch (IOException | JsonSyntaxException | JsonIOException e) {
			return printErrorDebug(url, jsonAsText, status, e);
		}
	}
}
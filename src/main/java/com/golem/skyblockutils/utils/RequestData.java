package com.golem.skyblockutils.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class RequestData {
	private final Map<String, List<String>> headers;
	private final JsonElement json;
	private final int status;
	public RequestData(int status, Map<String, List<String>> headers, JsonElement jsonData) {
		this.status = status;
		this.headers = headers;
		this.json = jsonData;
	}

	public JsonObject getJsonAsObject() {
		return json.getAsJsonObject();
	}


}
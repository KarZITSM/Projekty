package com.spinteam.kosch.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class SegregationRule {

	private final String id;
	private final String name;
	private final SegregationRuleType type;
	private final List<String> synonyms;
	private final String additionalInfo;

	public SegregationRule(String id, String name, SegregationRuleType type, List<String> synonyms, String additionalInfo) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.synonyms = synonyms;
		this.additionalInfo = additionalInfo;
	}

	public SegregationRule(JSONObject object) throws Exception {
		this.id = object.getString("_id");
		this.name = object.getString("name");
		this.type = SegregationRuleType.valueOf(object.getString("type"));
		this.synonyms = Arrays.asList(object.getString("synonyms").split(","));
		this.additionalInfo = object.getString("additional_info");
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SegregationRuleType getType() {
		return type;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	@NonNull
	@Override
	public String toString() {
		return name;
	}

}
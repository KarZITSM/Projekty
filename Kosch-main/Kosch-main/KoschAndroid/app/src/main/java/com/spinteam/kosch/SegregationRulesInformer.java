package com.spinteam.kosch;

import android.content.Context;

import com.spinteam.kosch.model.SegregationRule;
import com.spinteam.kosch.model.SegregationRuleType;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SegregationRulesInformer {

	private final Context context;
	private final List<SegregationRule> segregationRules = new ArrayList<>();

	public SegregationRulesInformer(Context context) {
		this.context = context;
		loadRules();
	}

	public void loadRules() {
		segregationRules.clear();
		try {
			String json = Utils.loadAssetTextFile(context, "recycle_data.json");
			JSONArray rulesArray = new JSONArray(json);
			for (int i = 0; i < rulesArray.length(); i++) {
				SegregationRule segregationRule = new SegregationRule(rulesArray.getJSONObject(i));
				segregationRules.add(segregationRule);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<SegregationRule> findRulesByQuery(String query) {
		query = query.toLowerCase();
		Set<SegregationRule> rulesByName = new HashSet<>();
		Set<SegregationRule> rulesBySynonyms = new HashSet<>();
		outer: for (SegregationRule segregationRule : segregationRules) {
			if (segregationRule.getName().toLowerCase().equals(query))
				return Collections.singletonList(segregationRule);
			for (String synonym : segregationRule.getSynonyms()) {
				if (synonym.toLowerCase().trim().equals(query)) {
					return Collections.singletonList(segregationRule);
				}
				if (synonym.toLowerCase().contains(query)) {
					rulesBySynonyms.add(segregationRule);
					continue outer;
				}
			}
			if (segregationRule.getName().toLowerCase().contains(query))
				rulesByName.add(segregationRule);
		}
		ArrayList<SegregationRule> segregationRules = new ArrayList<>();
		segregationRules.addAll(rulesByName);
		segregationRules.addAll(rulesBySynonyms);
		return segregationRules;
	}

}
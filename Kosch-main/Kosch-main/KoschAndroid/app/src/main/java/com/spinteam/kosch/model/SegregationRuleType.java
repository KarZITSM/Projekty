package com.spinteam.kosch.model;

public enum SegregationRuleType {
	LEKI(null, "Zanieś do apteki, która zbiera przeterminowane leki"),
	METALE_I_TWORZYWA(Trash.PLASTIC, ""),
	GABARYTY(null, "Zanieś do jednego z Punktów Selektywnego Zbierania Odpadów Komunalnych."),
	ZMIESZANE(Trash.MIXED, ""),
	ELEKTROSMIECI(null, "Zanieś do jednego z punktów zbiórki elektroodpadów lub Punktów Selektywnego Zbierania Odpadów Komunalnych."),
	BATERIE(null, "Wyrzuć do specjanego pojemnika na zużyte baterie w sklepie, urzędzie lub oddać do jednego z punktów Punktów Selektywnego Zbierania Odpadów Komunalnych."),
	BIO(null, "Wyrzuć do pojemnika na odpady biodegradowalne"),
	PAPIER(Trash.PAPER, ""),
	SZKLO(Trash.GLASS, ""),
	INNE(null, "Zanieś do jednego z Punktów Selektywnego Zbierania Odpadów Komunalnych.");

	private final Trash trash;
	private final String otherInfo;

	SegregationRuleType(Trash trash, String otherInfo) {
		this.trash = trash;
		this.otherInfo = otherInfo;
	}

	public Trash getTrash() {
		return trash;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

}
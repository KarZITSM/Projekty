package com.spinteam.kosch.model;

import com.spinteam.kosch.EspApi;

import java.util.HashMap;
import java.util.Map;

public class Trash {

	public static final Trash PAPER = new Trash(1, "Papier", false, 0);
	public static final Trash PLASTIC = new Trash(2, "Metale i tworzywa sztuczne", false, 0);
	public static final Trash MIXED = new Trash(4, "Zmieszane", false, 0);
	public static final Trash GLASS = new Trash(8, "Szkło", false, 0);
	public static final Trash[] TRASHES = {PAPER, PLASTIC, MIXED, GLASS};
	public static final String[] TRASH_NAMES = {PAPER.getName(), PLASTIC.getName(), MIXED.getName(), GLASS.getName()};
	public static final Map<Integer, Trash> trashMap = new HashMap<>();

	static {
		trashMap.put(PAPER.getId(), PAPER);
		trashMap.put(PLASTIC.getId(), PLASTIC);
		trashMap.put(MIXED.getId(), MIXED);
		trashMap.put(GLASS.getId(), GLASS);
	}

	private final int id;
	private final String name;
	private boolean opened;
	private int distance;
	private long lastOpen;

	public Trash(int id, String name, boolean opened, int distance) {
		this.id = id;
		this.name = name;
		this.opened = opened;
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isOpened() {
		return opened;
	}

	public int getDistance() {
		return distance;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int calculateGarbageFillPercent() {
		return Math.max(0, 100 - (distance * 100 / EspApi.TRASH_MAX_DISTANCE));
	}

	public static Trash byId(int id) {
		return trashMap.get(id);
	}

	public void setLastOpen(long lastOpen) {
		this.lastOpen = lastOpen;
	}

	public long getLastOpen() {
		return lastOpen;
	}

	public boolean isAllowedToOpen() {
		return System.currentTimeMillis() > lastOpen + 2000;
	}
}
package com.minesweeper.client;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.minesweeper.shared.EntryProxy;


public class EntryReceiver extends Receiver<List<EntryProxy>> {
	private ArrayList<EntryProxy> entries = null;

	@Override
	public void onFailure(ServerFailure error) {
		error.getMessage();
	}

	@Override
	public void onSuccess(List<EntryProxy> response) {
		entries= new ArrayList<EntryProxy>(response);
		MinesweeperWidget.addEntries(entries);
	}

	public List<EntryProxy> getResult() {
		return entries;
	}

}
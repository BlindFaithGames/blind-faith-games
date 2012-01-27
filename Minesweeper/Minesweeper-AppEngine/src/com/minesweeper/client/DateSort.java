package com.minesweeper.client;

import java.util.Comparator;

import com.minesweeper.shared.LogProxy;

public class DateSort implements Comparator<LogProxy>{

	@Override
	public int compare(LogProxy l1, LogProxy l2) {
        return l1.getDueDate().compareTo(l2.getDueDate());
	}	
}

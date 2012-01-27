/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.minesweeper.server;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.minesweeper.annotation.ServiceMethod;
import com.minesweeper.shared.LogChange;

/**
 * It manages the datastore
 *
 */
public class EntryService {

	@SuppressWarnings("unused")
	private static final Logger entry = Logger.getLogger(EntryService.class.getName());
	
    public EntryService() {
  	}

	static EntryDataStore db = new EntryDataStore();
	
	@ServiceMethod
	public static Long createEntry(Entry entry) {
		return db.update(entry);
	}

	@ServiceMethod
	public static Entry readEntry(Long id) {
		return db.find(id);
	}
	
	@ServiceMethod
	public static List<Entry> readEntries(List<Long> idList) {
		List<Entry> result = new ArrayList<Entry>();
		Long id;
		Iterator<Long> it = idList.iterator();
		while(it.hasNext()){
			id = it.next();
			result.add(db.find(id));
		}
		return result;
	}
	
	@ServiceMethod
	public static List<Entry> readEntriesByEvent(List<Long> idList, String event) {
		List<Entry> result = readEntries(idList);
		Entry e;
		Iterator<Entry> it = result.iterator();
		while(it.hasNext()){
			e = it.next();
			if(!e.getType().equals(event)){
				result.remove(e);
			}
		}
		return result;
	}

	@ServiceMethod
	public static Long updateEntry(Entry entry) {
		db.update(entry);
		LogDataStore.sendC2DMUpdate(LogChange.UPDATE + LogChange.SEPARATOR + entry.getId());
		return entry.getId();
	}

	@ServiceMethod
	public static void deleteEntry(Entry entry) {
		db.delete(entry.getId());
	}

	@ServiceMethod
	public static List<Entry> queryEntries() {
		return db.findAll();
	}
}

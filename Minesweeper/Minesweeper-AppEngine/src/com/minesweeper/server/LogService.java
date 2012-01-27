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


import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.minesweeper.annotation.ServiceMethod;
import com.minesweeper.shared.LogChange;

/**
 * It manages the datastore
 *
 */
public class LogService {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(LogService.class.getName());
	
    public LogService() {
  	}

	static LogDataStore db = new LogDataStore();
	
	@ServiceMethod
	public static Log createLog(Log log) {
		return db.update(log);
	}

	@ServiceMethod
	public static Log readLog(Long id) {
		return db.find(id);
	}
	
	@ServiceMethod
	public static List<Log> readLogs(String tag) {
		List<Log> result = db.findAll();
		Log l;
		Iterator<Log> it = result.iterator();
		while(it.hasNext()){
			l = it.next();
			if(!l.getTag().equals(tag)){
				result.remove(l);
			}
		}
		return result;
	}

	@ServiceMethod
	public static Log updateLog(Log log) {
		log.setEmailAddress(LogDataStore.getUserEmail());
		log = db.update(log);
		LogDataStore.sendC2DMUpdate(LogChange.UPDATE + LogChange.SEPARATOR + log.getId());
		return log;
	}

	@ServiceMethod
	public static void deleteLog(Log log) {
		db.delete(log.getId());
	}

	@ServiceMethod
	public static List<Log> queryLogs() {
		return db.findAll();
	}
}

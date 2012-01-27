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
package com.minesweeper.client;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import com.minesweeper.shared.EntryProxy;
import com.minesweeper.shared.LogProxy;
import com.minesweeper.shared.MessageProxy;
import com.minesweeper.shared.RegistrationInfoProxy;

public interface MyRequestFactory extends RequestFactory {
	
	@ServiceName("com.minesweeper.server.LogService")
	public interface LogRequest extends RequestContext {

		Request<LogProxy> createLog(LogProxy log);

		Request<LogProxy> readLog(Long id);
		
		Request<List<LogProxy>> readLogs(String tag);

		Request<LogProxy> updateLog(LogProxy log);

		Request<Void> deleteLog(LogProxy log);

		Request<List<LogProxy>> queryLogs();

	}
	
	@ServiceName("com.minesweeper.server.EntryService")
	public interface EntryRequest extends RequestContext {
		Request<Long> createEntry(EntryProxy entry);
		
		Request<EntryProxy> readEntry(Long id);
		
		Request<List<EntryProxy>> readEntries(List<Long> idList);
		
		Request<List<EntryProxy>> readEntriesByEvent(List<Long> idList, String event);

		Request<Long> updateEntry(EntryProxy entry);

		Request<Void> deleteEntry(EntryProxy entry);

		Request<List<EntryProxy>> queryEntries();
		
	}
	
	@ServiceName("com.minesweeper.server.RegistrationInfo")
	public interface RegistrationInfoRequest extends RequestContext {
		/**
		 * Register a device for C2DM messages.
		 */
		InstanceRequest<RegistrationInfoProxy, Void> register();

		/**
		 * Unregister a device for C2DM messages.
		 */
		InstanceRequest<RegistrationInfoProxy, Void> unregister();
	}

	@ServiceName("com.minesweeper.server.Message")
	public interface MessageRequest extends RequestContext {
		/**
		 * Send a message to a device using C2DM.
		 */
		InstanceRequest<MessageProxy, String> send();
	}

	LogRequest logRequest();
	
	EntryRequest entryRequest();

	RegistrationInfoRequest registrationInfoRequest();

	MessageRequest messageRequest();
}

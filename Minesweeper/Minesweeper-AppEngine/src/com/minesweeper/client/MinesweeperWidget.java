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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.minesweeper.client.MyRequestFactory.EntryRequest;
import com.minesweeper.client.MyRequestFactory.LogRequest;
import com.minesweeper.shared.EntryProxy;
import com.minesweeper.shared.LogProxy;

public class MinesweeperWidget extends Composite{
	private static final int REFRESH_INTERVAL = 10000; // ms
	private final static int MAX_LOG = 10;
	private int indexOfForms;
	private int indexOfLogs; /*
							 * Indicates the difference between next button
							 * press and previous button press, in order to
							 * calculate the index of the current log showed.
							 */

	private ListBox listBox = new ListBox();
	private ArrayList<String> games = new ArrayList<String>();
	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel logsPanel = new VerticalPanel(), formsPanel = new VerticalPanel();
	private HorizontalPanel buttonPanel[] = new HorizontalPanel[2];
	private static FlexTable logsTable = new FlexTable(), formsTable = new FlexTable();
	private Button next[] = new Button[2];
	private Button previous[] = new Button[2];
	private static Button showSettings[] = new Button[MAX_LOG];
	private static TextArea[] touches = new TextArea[MAX_LOG];
	private static TextArea[] mines = new TextArea[MAX_LOG];
	private ArrayList<List<String>> forms = new ArrayList<List<String>>();
	
	// Pop up
	private static PopUpContentPanel[] popupSettings = new PopUpContentPanel[MAX_LOG];
	private static PopUpContentPanel[] popupForms = new PopUpContentPanel[MAX_LOG];

	private static int row;
	private int i;
	private int j;
	
	final MyRequestFactory requestFactory = GWT.create(MyRequestFactory.class);
	private LogRequest logRequest;
	private EntryRequest entryRequest;

	private ArrayList<LogProxy> logs = new ArrayList<LogProxy>();

//	private static final int STATUS_DELAY = 4000;
//	private static final String STATUS_ERROR = "status error";
//	private static final String STATUS_NONE = "status none";
//	private static final String STATUS_SUCCESS = "status success";

	interface MinesweeperUiBinder extends UiBinder<Widget, MinesweeperWidget> {
	}

	private static MinesweeperUiBinder uiBinder = GWT
			.create(MinesweeperUiBinder.class);

//	@UiField
//	DivElement status;

	/**
	 * Timer to clear the UI.
	 */
//	Timer timer = new Timer() {
//		@Override
//		public void run() {
//			status.setInnerText("");
//			status.setClassName(STATUS_NONE);
//		}
//	};
//
//	private void setStatus(String message, boolean error) {
//		status.setInnerText(message);
//		if (error) {
//			status.setClassName(STATUS_ERROR);
//		} else {
//			if (message.length() == 0) {
//				status.setClassName(STATUS_NONE);
//			} else {
//				status.setClassName(STATUS_SUCCESS);
//			}
//		}
//
//		timer.schedule(STATUS_DELAY);
//	}

	public MinesweeperWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		final EventBus eventBus = new SimpleEventBus();

		requestFactory.initialize(eventBus);

		// Fill the list
		fillList();

		for (int i = 0; i < buttonPanel.length; i++){
			previous[i] = new Button("Previous");
			buttonPanel[i] = new HorizontalPanel();
			buttonPanel[i].add(previous[i]);
			next[i] = new Button("Next");
			buttonPanel[i].add(next[i]);
			buttonPanel[i].addStyleName("buttonPanel");
			
			next[i].setEnabled(false);
			next[i].addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showNext(((Button)event.getSource()).getTitle());
				}
			});
			previous[i].setEnabled(false);
			previous[i].addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showPrevious(((Button)event.getSource()).getTitle());
				}
			});
		}
		
		next[0].setTitle("Logs");
		next[1].setTitle("Forms");
		
		previous[0].setTitle("Logs");
		previous[1].setTitle("Forms");

		FlowPanel p = new FlowPanel();
		p.add(listBox);
		p.setStyleName("buttonPanel");

		logsPanel.add(logsTable);
		logsPanel.add(buttonPanel[0]);
		tabPanel.add(logsPanel, "Logs");

		formsPanel.add(formsTable);
		formsPanel.add(buttonPanel[1]);
		tabPanel.add(formsPanel, "Forms");

		tabPanel.selectTab(0);
		tabPanel.setPixelSize(Window.getClientWidth(), Window.getClientHeight());

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(p); mainPanel.add(tabPanel);
		
		ScrollPanel scroll = new ScrollPanel(mainPanel);
		scroll.setPixelSize(Window.getClientWidth(), Window.getClientHeight());
		
		// Associate the main panel with the HTML host page
		RootPanel.get("accGames").add(scroll);

		// List box with accessible games
		listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				updateTable(listBox.getValue(listBox.getSelectedIndex()));
				if (indexOfLogs * MAX_LOG + MAX_LOG > logs.size())
					next[0].setEnabled(false);
				else
					next[0].setEnabled(true);
				if (indexOfForms * MAX_LOG + MAX_LOG > forms.size())
					next[1].setEnabled(false);
				else
					next[1].setEnabled(true);
			}
		});



		// Setup timer to refresh list automatically.
		Timer refreshTimer = new Timer() {

			@Override
			public void run() {
				fillList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}

	protected void updateTable(String value) {
		logRequest = requestFactory.logRequest();

		logRequest.readLogs(value).fire(new Receiver<List<LogProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
//				setStatus(error.getMessage(), true);
			}

			@Override
			public void onSuccess(List<LogProxy> response) {
				logs.clear();
				logs.addAll(response);
				Collections.sort(logs, new DateSort());

				updateLogsTable();
				updateFormsTable();
				if (indexOfLogs * MAX_LOG + MAX_LOG > logs.size())
					next[0].setEnabled(false);
				else
					next[0].setEnabled(true);
				if (indexOfForms * MAX_LOG + MAX_LOG > forms.size())
					next[1].setEnabled(false);
				else
					next[1].setEnabled(true);
			}
			
		});
	}

	protected void showPrevious(String panel) {
		if (panel.equals("Logs")){		
			indexOfLogs--;
			if (indexOfLogs == 0)
				previous[0].setEnabled(false);
			if (indexOfLogs * MAX_LOG + MAX_LOG > logs.size())
				next[0].setEnabled(false);
			else
				next[0].setEnabled(true);
			updateLogsTable();
		}
		else if (panel.equals("Forms")){
			indexOfForms--;
			if (indexOfForms == 0)
				previous[1].setEnabled(false);
			if (indexOfForms * MAX_LOG + MAX_LOG > forms.size())
				next[1].setEnabled(false);
			else
				next[1].setEnabled(true);
			updateFormsTable();
		}
	}

	protected void showNext(String panel) {
		if (panel.equals("Logs")){
			indexOfLogs++;
			previous[0].setEnabled(true);
			if (indexOfLogs * MAX_LOG + MAX_LOG > logs.size())
				next[0].setEnabled(false);
			updateLogsTable();
		}
		else if (panel.equals("Forms")){
			indexOfForms++;
			previous[1].setEnabled(true);
			if (indexOfForms * MAX_LOG + MAX_LOG > forms.size())
				next[1].setEnabled(false);
			updateFormsTable();
		}
	}

	private void updateLogsTable() {
		logsTable.removeAllRows();

		// Create table for entries
		logsTable.setText(0, 0, "Index");
		logsTable.setText(0, 1, "User");
		logsTable.setText(0, 2, "Date");
		logsTable.setText(0, 3, "Settings");
		logsTable.setText(0, 4, "Touches");
		logsTable.setText(0, 5, "Board");
		logsTable.setText(0, 6, "Instructions");
		logsTable.setText(0, 7, "Exit");
		logsTable.setText(0, 8, "Device");


		// Add styles to elements in the stock list table.
		logsTable.setCellPadding(6);

		// Add styles to elements in the stock list table.
		logsTable.getRowFormatter().addStyleName(0, "logListHeader");
		logsTable.addStyleName("watchList");

		EntryReceiver receiver = new EntryReceiver();

		//----------- Set pop ups --------------
		for (int i = 0; i < showSettings.length; i++){
			showSettings[i] = new Button("Show");
			showSettings[i].setTitle("" + i);
			showSettings[i].addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Button aux = (Button)event.getSource();
					int n = Integer.parseInt(aux.getTitle());
					popupSettings[n].center();
				}
			});
		}
		
		row = logsTable.getRowCount();
		i = indexOfLogs * MAX_LOG;
		while (i < logs.size() && i < indexOfLogs * MAX_LOG + MAX_LOG) {
			logsTable.setText(i % MAX_LOG + 1, 0, Integer.toString(i + 1));
			logsTable.setText(i % MAX_LOG + 1, 1, logs.get(i).getEmailAddress());			
			logsTable.setText(i % MAX_LOG + 1, 2, logs.get(i).getDueDate().toString());
			// Settings
			popupSettings[i % MAX_LOG] = new PopUpContentPanel();
			logsTable.setWidget(i % MAX_LOG + 1, 3, showSettings[i % MAX_LOG]);
			
			// Obtener entries
			entryRequest = requestFactory.entryRequest();
			entryRequest.readEntries(logs.get(i).getLogEntries()).fire(receiver);
			i++;
		}

	}


	static void addEntries(ArrayList<EntryProxy> e) {
		touches[row - 1] = new TextArea();
		mines[row - 1] = new TextArea();
		touches[row - 1].setReadOnly(true);
		mines[row - 1].setReadOnly(true);
		// Initialize
		logsTable.setText(row, 6, "No");
		logsTable.setText(row, 7, "No");
		for (EntryProxy entry : e) {
			// Si son pulsaciones
			if (entry.getType().equals("KeyEvent")	|| entry.getType().equals("TapEvent") ||
					entry.getType().equals("DoubleTapEvent") || entry.getType().equals("TripleTapEvent") || 
					entry.getType().equals("KeyEvent") )
				touches[row - 1].setText(touches[row - 1].getText() + entry.getComment() + "\n");
			else if (entry.getType().equals("OnCreate")) {
				mines[row - 1].setText(entry.getComment());
				// Sets settings
				popupSettings[row - 1].setSettings(entry.getConfigurationSettings());
			}
			else if (entry.getType().equals("Instructions")){
				logsTable.setText(row, 6, "Yes");
			}
			else if (entry.getType().equals("Exit")){
				logsTable.setText(row, 7, "Yes");
				showSettings[row - 1].setEnabled(false);
			}
			else if (entry.getType().equals("Device")){
				logsTable.setText(row, 8, entry.getComment());
			}
		}
		logsTable.setWidget(row, 4, touches[row - 1]);
		logsTable.setWidget(row, 5, mines[row - 1]);
		row++;
	}
	
	private void updateFormsTable(){
		formsTable.removeAllRows();
		
		formsTable.setText(0, 0, "Index");
		formsTable.setText(0, 1, "User");
		formsTable.setText(0, 2, "Date");
		formsTable.setText(0, 3, "Form");
		
		formsTable.setCellPadding(6);
		
		
		formsTable.getRowFormatter().addStyleName(0, "logListHeader");
		formsTable.addStyleName("watchList");
		
		Button showForms[] = new Button[MAX_LOG];
		
		for (int i = 0; i < showForms.length; i++){
			showForms[i] = new Button("Show");
			showForms[i].setTitle("" + i);
			showForms[i].addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Button aux = (Button)event.getSource();
					int n = Integer.parseInt(aux.getTitle());
					popupForms[n].center();
				}
			});
		}

		
		j = indexOfForms * MAX_LOG;
		int numRows = j;
		while (j < logs.size() && numRows < forms.size() && numRows < indexOfForms * MAX_LOG + MAX_LOG) {
			if (logs.get(j).getFormAnswers().size() != 0){
				formsTable.setText(numRows % MAX_LOG + 1, 0, Integer.toString(numRows + 1));
				formsTable.setText(numRows % MAX_LOG + 1, 1, logs.get(j).getEmailAddress());
				formsTable.setText(numRows % MAX_LOG + 1, 2, logs.get(j).getDueDate().toString());
				
				popupForms[numRows % MAX_LOG] = new PopUpContentPanel();
				popupForms[numRows % MAX_LOG].setForm(logs.get(j).getFormAnswers());
	
				formsTable.setWidget(numRows % MAX_LOG + 1, 3, showForms[numRows % MAX_LOG]);
				
				numRows++;
			}

			j++;
		}
	}

	private void fillList() {
		logRequest = requestFactory.logRequest();

		logRequest.queryLogs().fire(new Receiver<List<LogProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
//				setStatus(error.getMessage(), true);
			}

			@Override
			public void onSuccess(List<LogProxy> response) {
				if (logs.size() != response.size()) {
					int i = logs.size();
					while (i < response.size()) {
						logs.add(response.get(i));
						if (logs.get(i).getFormAnswers().size() != 0){
							forms.add(logs.get(i).getFormAnswers());
						}
						
						i++;
					}

					Iterator<LogProxy> it = response.iterator();
					LogProxy l;

					while (it.hasNext()) {
						l = it.next();
						if (!games.contains(l.getTag())) {
							games.add(l.getTag());
							listBox.addItem(l.getTag());
						}

					}
					Collections.sort(logs, new DateSort());
					updateLogsTable();
					updateFormsTable();
					if (indexOfLogs * MAX_LOG + MAX_LOG > logs.size())
						next[0].setEnabled(false);
					else
						next[0].setEnabled(true);
					if (indexOfForms * MAX_LOG + MAX_LOG > forms.size())
						next[1].setEnabled(false);
					else
						next[1].setEnabled(true);
				}
			}
		});
	}

}

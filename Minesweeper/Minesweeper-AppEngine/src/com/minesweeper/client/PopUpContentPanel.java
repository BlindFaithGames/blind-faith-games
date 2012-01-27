package com.minesweeper.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopUpContentPanel extends VerticalPanel{
	private PopupPanel popup = new PopupPanel();
	private Button close = new Button("Close");
	private SimplePanel holder = new SimplePanel();
	
	public PopUpContentPanel(){
		popup = new PopupPanel(false);
		popup.setTitle("Settings configuration");
		holder.add(close);
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
	}
	
	public void setSettings(String text){
		String aux[] = text.split("/");
		String result = "";
		for (int i = 0; i < aux.length; i++){
			result += aux[i] + "<br>";
		}
		HTML message = new HTML(result);
		this.add(message);
		this.add(holder);
		popup.setWidget(this);
	}
	
	public void center(){
		popup.center();
	}

	public void setForm(List<String> formAnswers) {
		String result = "";
		for (int i = 0; i < formAnswers.size(); i++){
			result += Integer.toString(i + 1) + "- " + formAnswers.get(i) + "<br>";
		}
		HTML message = new HTML(result);
		this.add(message);
		this.add(holder);
		popup.setWidget(this);
	}
}

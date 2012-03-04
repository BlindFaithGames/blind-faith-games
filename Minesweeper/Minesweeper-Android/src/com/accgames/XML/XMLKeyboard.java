package com.accgames.XML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Clase que representa una Teclado XML.
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 */

public class XMLKeyboard {

	private HashMap<Integer, String> keyList; /* Mapa tecla - acción */
	private HashMap<Integer, String> keyButton; /* Mapa tecla - botón */
	private int num; // Número de acciones definidas para el teclado
	// Para llevar el número de errores que se produjeron
	private int numErrors;


	public XMLKeyboard(){
		keyList = new HashMap<Integer, String>();
		keyButton = new HashMap<Integer, String>();
		/* Botones configurables */
		keyButton.put(7, "0");
		keyButton.put(8, "1");
		keyButton.put(9, "2");
		keyButton.put(10, "3");
		keyButton.put(11, "4");
		keyButton.put(12, "5");
		keyButton.put(13, "6");
		keyButton.put(14, "7");
		keyButton.put(15, "8");
		keyButton.put(16, "9");

		keyButton.put(29, "A");
		keyButton.put(57, "ALT LEFT");
		keyButton.put(58, "ALT RIGHT");
		keyButton.put(75, "Apostrophe");
		keyButton.put(187, "App Swicth");
		keyButton.put(77, "@");
		keyButton.put(30, "B");
		keyButton.put(4, "BACK");
		keyButton.put(73, "\\");
		keyButton.put(121, "Pause");
		keyButton.put(188, "#1");
		keyButton.put(197, "#10");
		keyButton.put(198, "#11");
		keyButton.put(199, "#12");
		keyButton.put(200, "#13");
		keyButton.put(201, "#14");
		keyButton.put(202, "#15");
		keyButton.put(203, "#16");
		keyButton.put(189, "#2");
		keyButton.put(190, "#3");
		keyButton.put(191, "#4");
		keyButton.put(192, "#5");
		keyButton.put(193, "#6");
		keyButton.put(194, "#7");
		keyButton.put(195, "#8");
		keyButton.put(196, "#9");
		keyButton.put(96, "A Button");
		keyButton.put(97, "B Button");
		keyButton.put(98, "C Button");
		keyButton.put(102, "L1 Button");
		keyButton.put(104, "L2 Button");
		keyButton.put(110, "Mode Button");
		keyButton.put(103, "R1 Button");
		keyButton.put(105, "R2 Button");
		keyButton.put(109, "Select Button");
		keyButton.put(108, "Start Button");
		keyButton.put(106, "Thumb Left Button");
		keyButton.put(107, "Thumb Right Button");
		keyButton.put(99, "X Button");
		keyButton.put(100, "Y Button");
		keyButton.put(101, "Z Button");
		
		keyButton.put(31, "C");
		keyButton.put(5, "Call Key");
		keyButton.put(27, "Camera");
		keyButton.put(115, "Caps Lock");
		keyButton.put(175, "Captions");
		keyButton.put(167, "Channel Down");
		keyButton.put(166, "Channel Up");
		keyButton.put(28, "Clear");
		keyButton.put(55, ",");
		keyButton.put(113, "Left CTRL");
		keyButton.put(114, "Right CTRL");
		keyButton.put(32, "D");
		keyButton.put(67, "DEL");
		keyButton.put(23, "DPAD Center");
		keyButton.put(20, "DPAD Down");
		keyButton.put(21, "DPAD Left");
		keyButton.put(22, "DPAD Right");
		keyButton.put(19, "DPAD Up");
		keyButton.put(173, "DVR");
		keyButton.put(33, "E");
		keyButton.put(6, "End Call");
		keyButton.put(66, "Enter");
		keyButton.put(65, "Envelope");
		keyButton.put(70, "=");
		keyButton.put(111, "Escape");
		keyButton.put(64, "Explorer");
		keyButton.put(34, "F");
		
		keyButton.put(131, "F1");
		keyButton.put(140, "F10");
		keyButton.put(141, "F11");
		keyButton.put(142, "F12");
		keyButton.put(132, "F2");
		keyButton.put(133, "F3");
		keyButton.put(134, "F4");
		keyButton.put(135, "F5");
		keyButton.put(136, "F6");
		keyButton.put(137, "F7");
		keyButton.put(138, "F8");
		keyButton.put(139, "F9");
		
		keyButton.put(80, "Camera focus");
		keyButton.put(125, "Forward");
		keyButton.put(112, "Forward DEL");
		keyButton.put(119, "Function Modifier");
		keyButton.put(35, "G");
		keyButton.put(68, "`");
		keyButton.put(172, "Guide");
		keyButton.put(36, "H");
		keyButton.put(79, "Headset Hook");
		keyButton.put(3, "Home");
		keyButton.put(37, "I");
		keyButton.put(165, "Info");
		keyButton.put(124, "Insert");
		keyButton.put(38, "J");
		keyButton.put(39, "K");
		keyButton.put(40, "L");
		keyButton.put(204, "Language Switch");
		keyButton.put(71, "[");
		keyButton.put(41, "M");
		keyButton.put(205, "Manner Mode");
		keyButton.put(128, "Close Media");
		keyButton.put(129, "Eject Media");
		keyButton.put(90, "Fast Forward");
		keyButton.put(87, "Play Next Media");
		keyButton.put(127, "Pause media");
		keyButton.put(126, "Play Media");
		keyButton.put(85, "Play/Pause media");
		keyButton.put(88, "Play Previous Media");
		keyButton.put(130, "Record Media");
		keyButton.put(89, "Rewind Media");
		keyButton.put(86, "Stop Media");
		keyButton.put(82, "Menu");
		keyButton.put(117, "Left Meta modifier");
		keyButton.put(118, "Right Meta modifier");
		keyButton.put(69, "-");
		keyButton.put(123, "End Movement");
		keyButton.put(122, "Home Movement");
		keyButton.put(91, "Mute");
		keyButton.put(42, "N");
		keyButton.put(83, "Notification");
		keyButton.put(78, "Number modifier");
		keyButton.put(144, "Numeric Keypad 0");
		keyButton.put(145, "Numeric Keypad 1");
		keyButton.put(146, "Numeric Keypad 2");
		keyButton.put(147, "Numeric Keypad 3");
		keyButton.put(148, "Numeric Keypad 4");
		keyButton.put(149, "Numeric Keypad 5");
		keyButton.put(150, "Numeric Keypad 6");
		keyButton.put(151, "Numeric Keypad 7");
		keyButton.put(152, "Numeric Keypad 8");
		keyButton.put(153, "Numeric Keypad 9");
		keyButton.put(157, "+");
		keyButton.put(159, ",");
		keyButton.put(154, "/");
		keyButton.put(158, ".");
		keyButton.put(160, "Numeric Keypad Enter");
		keyButton.put(161, "Numeric Keypad =");
		keyButton.put(162, "Numeric Keypad (");
		keyButton.put(155, "Numeric Keypad *");
		keyButton.put(163, "Numeric Keypad )");
		keyButton.put(156, "Numeric Keypad -");
		keyButton.put(156, "Num Lock");
		keyButton.put(43, "O");
		keyButton.put(44, "P");
		keyButton.put(93, "Page Down");
		keyButton.put(92, "Page Up");
		keyButton.put(56, ".");
		keyButton.put(94, "Picture Symbols");
		keyButton.put(81, "+");
		keyButton.put(18, "#");
		keyButton.put(26, "Power");
		keyButton.put(186, "Blue Programmable");
		keyButton.put(184, "Green Programmable");
		keyButton.put(183, "Red Programmable");
		keyButton.put(185, "Yellow Programmable");
		keyButton.put(45, "Q");
		keyButton.put(46, "R");
		keyButton.put(72, "]");
		keyButton.put(47, "S");
		keyButton.put(116, "Scroll Lock");
		keyButton.put(84, "Search");
		keyButton.put(74, ";");
		keyButton.put(176, "Settings");
		keyButton.put(59, "Left Shift");
		keyButton.put(60, "Right Shift");
		keyButton.put(76, "/");
		keyButton.put(1, "Soft Left");
		keyButton.put(2, "Soft Right");
		keyButton.put(62, "Space");
		keyButton.put(17, "*");
		keyButton.put(48, "T");
		keyButton.put(61, "Tab");
		keyButton.put(49, "U");
		keyButton.put(0, "Unkwown");
		keyButton.put(50, "V");
		keyButton.put(25, "Volume Down");
		keyButton.put(164, "Volume Mute");
		keyButton.put(24, "Volume Up");
		keyButton.put(51, "W");
		keyButton.put(171, "Window");
		keyButton.put(52, "X");
		keyButton.put(53, "Y");
		keyButton.put(54, "Z");
		keyButton.put(168, "Zoom In");
		keyButton.put(169, "Zoom Out");		

		numErrors = 0;
		num = 0;
	}

// SETTERS
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public void setKeyButton(HashMap<Integer, String> keyButton) {
		this.keyButton = keyButton;
	}

	// GETTERS
	public HashMap<Integer, String> getKeyList() {
		return keyList;
	}
	
	public int getNum(){return num;}
	public int getNumErrors() {return numErrors;}

	public String getAction(int i) {
		return keyList.get(i);
	}
	
	public String toString(int key){
		return keyButton.get(key);
	}
	
	
// OTHERS

	public void riseNumberOfErrors(int unit){
		numErrors += unit;
	}

	public void addObject(Integer k, String v) {
		keyList.put(k, v);
	}
	
	public Integer getKeyByButton(String button){
		Iterator it = keyButton.entrySet().iterator();
		Map.Entry e = null;
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = button.equals(e.getValue());
		}
		if (found) return (Integer) e.getKey();
		else return null;
	}
	
	public String searchButtonByAction(String action){
		Iterator it = keyList.entrySet().iterator();
		Map.Entry e = null;
		String s1 = "";
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = action.equals(e.getValue());
		}
		if (found)
			s1 = keyButton.get(e.getKey());
		return s1;
	}
	
	public Integer getKeyByAction(String action){
		Iterator it = keyList.entrySet().iterator();
		Map.Entry e = null;
		boolean found = false;
		// Para cada fila del teclado
		while (!found && it.hasNext()) {
			e = (Map.Entry) it.next();
			found = action.equals(e.getValue());
		}
		if (found) return (Integer) e.getKey();
		else return null;
	}
	
	/**
	 * Check if a button given by parameter is available
	 * @param key
	 * @return
	 */
	public void addButtonAction(int key, String action){
		// Eliminar tecla asignada a esa acción
		Integer k = getKeyByAction(action);
		if (k != null){
			keyList.remove(k);
		}
		keyList.put(key, action);
	}

}

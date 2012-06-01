package es.eucm.blindfaithgames.tss.game;

import java.util.List;


public class Scene {

	private NPC currentNPC;
	private List<NPC> npcs;
	private int id;
	private SceneType type;
	private List<Integer> nextScenes;		/* Scene ids of the next possible scenes */
	private String introMsg, description;
	
	private List<Integer> transitionCondition;
	private List<Integer> endCondition;
	private boolean firstTime;
	
	
	public Scene(List<NPC> npcs, int id, SceneType type, String introMsg, String description,
					List<Integer> nextScenes,List<Integer> transitionCondition, List<Integer> endCondition){
		this.npcs = npcs;
		this.id = id;
		this.type = type;
		this.introMsg = introMsg;
		this.description = description;
		this.nextScenes = nextScenes;
		this.transitionCondition = transitionCondition;
		this.endCondition = endCondition;
		firstTime = true;
	}

	public int getID() {
		return id;
	}
	
	public String getintroMsg() {
		if(firstTime){
			firstTime = false;
			return introMsg;
		}else
			return null;
	}
	
	public List<Integer> getNextScenes() {
		return nextScenes;
	}

	public List<NPC> getNPCS() {
		return npcs;
	}
	
	public List<Integer> getTransitionCondition() {
		return transitionCondition;
	}
	
	public List<Integer> getEndCondition() {
		return endCondition;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean changeNPC(int selectedNPC) {
		currentNPC = npcs.get(selectedNPC);
		currentNPC.reset();
		return currentNPC.getTransition();
		//npcs.remove(selectedNPC);
	}
	
	public boolean equals(Object o){
		Scene sc = (Scene) o;
		return this.id == sc.id;
	}

	public boolean updateDialog(Text text) {
		String speech = currentNPC.nextDialog();
		if(speech != null)
			text.concatText(speech);
		return speech != null;
	}

	public boolean isInNextScene(int selectedScene) {
		boolean found = false;
		for(Integer sc:nextScenes){
			found = (sc == selectedScene);
		}
		return found;
	}

	public String getNPCSOptions() {
		String options = "";
		int counter = 0;
		for(NPC npc:npcs){
			options += counter + "- " + npc.getName() + Text.SEPARATOR;
		}
		return options;
	}

	public int showNPCOptions(Text text) {
		String options = "";
		int counter = 0;
		for(NPC npc:npcs){
				options += counter + " - " + npc.getName() + Text.SEPARATOR;
				counter++;
		}
		text.setText(options);
		return counter;
	}

	public String getCurrentDialog() {
		String result = null;
		if(currentNPC!= null){
			result = currentNPC.getDialog();
		}
		return result;
	}
}

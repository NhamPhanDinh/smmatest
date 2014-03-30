package jp.ne.smma.aboutsmma.DTO;

/**
 * Item calendar
 */
public class ItemCalendar {
	private String id;
	private String event_id;
	// if no chose, don't draw event 27/3/2014
	private boolean isChosen;
	private String companyName;
	private String eventName;
	private String startDay;
	private String endDay;
	private int iconUrl;
	private String colorCode;

	/**
	 * Contructor no variable
	 */
	public ItemCalendar() {

	}

	/**
	 * Contructor variable
	 * 
	 * @param id
	 *            - Id
	 * 
	 * @param companyName
	 *            - name company
	 * @param eventName
	 *            - event name
	 * @param startDay
	 *            - start event
	 * @param endDay
	 *            - end day event
	 * @param iconUrl
	 *            - icon image index
	 * @param colorCode
	 *            - color mode
	 */
	public ItemCalendar(String id,String eventId, String companyName, String eventName,
			String startDay, String endDay, int iconUrl, String colorCode) {
		this.id = id;
		this.event_id = eventId;
		this.isChosen = isChosen;
		this.companyName = companyName;
		this.eventName = eventName;
		this.startDay = startDay;
		this.endDay = endDay;
		this.iconUrl = iconUrl;
		this.colorCode = colorCode;

	}

	/**
	 * Contructor variable
	 * 
	 * @param companyName
	 *            - name company
	 * @param eventName
	 *            - event name
	 * @param startDay
	 *            - start event
	 * @param endDay
	 *            - end day event
	 * @param iconUrl
	 *            - icon image index
	 * @param colorCode
	 *            - color mode
	 */
	public ItemCalendar(String companyName, String eventName, String startDay,
			String endDay, int iconUrl, String colorCode) {
		this.companyName = companyName;
		this.eventName = eventName;
		this.startDay = startDay;
		this.endDay = endDay;
		this.iconUrl = iconUrl;
		this.colorCode = colorCode;

	}
	
	public String getEventId() {
		return event_id;
	}

	public void setEventId(String id) {
		this.event_id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public int getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(int iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public boolean isChosen() {
		return isChosen;
	}

	public void setChosen(boolean isChosen) {
		this.isChosen = isChosen;
	}

}

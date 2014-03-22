package jp.ne.smma.aboutsmma.DTO;

/**
 * Notification Item
 */
public class NotificationItem {
	private int id;
	private int id_event;
	private String name;

	private String startday;
	private String endday;
	private String chooseday;

	private String note;
	private String value;
	private String check;
	private String status;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            - id
	 * @param name
	 *            - name
	 * @param note
	 *            - date
	 * @param value
	 *            - value
	 * @param status
	 *            - 0: nothing, 1: 1 month, 2: 3 month, 3: other
	 */
	public NotificationItem(int id, int idevent, String name, String startday,
			String endday, String chooseday, String note, String value,
			String check, String status) {
		this.id = id;
		this.id_event = idevent;
		this.name = name;
		this.startday = startday;
		this.endday = endday;
		this.chooseday = chooseday;
		this.note = note;
		this.value = value;
		this.check = check;
		this.status = status;
	}

	/**
	 * Constructor no value
	 */
	public NotificationItem() {

	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_event() {
		return id_event;
	}

	public void setId_event(int id_event) {
		this.id_event = id_event;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartday() {
		return startday;
	}

	public void setStartday(String startday) {
		this.startday = startday;
	}

	public String getEndday() {
		return endday;
	}

	public void setEndday(String endday) {
		this.endday = endday;
	}

	public String getChooseday() {
		return chooseday;
	}

	public void setChooseday(String chooseday) {
		this.chooseday = chooseday;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

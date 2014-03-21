package jp.ne.smma.aboutsmma.DTO;

/**
 * Notification Item
 */
public class NotificationItem {
	private int id;
	private String name;
	private String date;
	private String value;
	private String status;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            - id
	 * @param name
	 *            - name
	 * @param date
	 *            - date
	 * @param value
	 *            - value
	 * @param status
	 *            - 0: nothing, 1: 1 month, 2: 3 month, 3: other
	 */
	public NotificationItem(int id, String name, String date, String value,
			String status) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.value = value;
		this.status = status;
	}
	/**
	 * Constructor no value
	 */
	public NotificationItem(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

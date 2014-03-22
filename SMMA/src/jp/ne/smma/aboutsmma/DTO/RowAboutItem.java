package jp.ne.smma.aboutsmma.DTO;
/**
 * Row about item
 */
public class RowAboutItem {
	private String colorCode;
	private String pathImageItem;
	private String strContent;
	private String placeId;
	private double latitude;
	private double longitude;
	/**
	 * Constructor
	 * 
	 * @param colorCode
	 *            - code color bacground same: #ffff
	 * @param imageItem
	 *            - image item backgroud
	 * @param strContent
	 *            - content list item
	 */
	public RowAboutItem(String colorCode, String imagePathItem, String strContent, String id, double latitude, double longitude) {
		this.colorCode = colorCode;
		this.pathImageItem = imagePathItem;
		this.strContent = strContent;
		this.placeId = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getPathImageItem() {
		return pathImageItem;
	}

	public void setPathImageItem(String imageItem) {
		this.pathImageItem = imageItem;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	
	public String getPlaceId(){
		return placeId;
	}
	
	public void setPlaceId(String id){
		this.placeId = id;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public void setLongitude(){
		this.longitude = longitude;
	}

}

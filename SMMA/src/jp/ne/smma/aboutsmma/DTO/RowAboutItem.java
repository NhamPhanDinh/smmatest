package jp.ne.smma.aboutsmma.DTO;
/**
 * Row about item
 */
public class RowAboutItem {
	private String colorCode;
	private int imageItem;
	private String strContent;

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
	public RowAboutItem(String colorCode, int imageItem, String strContent) {
		this.colorCode = colorCode;
		this.imageItem = imageItem;
		this.strContent = strContent;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public int getImageItem() {
		return imageItem;
	}

	public void setImageItem(int imageItem) {
		this.imageItem = imageItem;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

}

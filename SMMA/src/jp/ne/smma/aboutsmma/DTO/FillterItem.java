package jp.ne.smma.aboutsmma.DTO;

public class FillterItem {
	private String strContent=null;
	private boolean selected=false;
	/**
	 * Constructor
	 */
	public FillterItem(){
		
	}
	/**
	 * Constructor have variable
	 */
	public FillterItem(String content, boolean bCheck){
		this.strContent=content;
		this.selected=bCheck;
	}
	public String getStrContent() {
		return strContent;
	}
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}

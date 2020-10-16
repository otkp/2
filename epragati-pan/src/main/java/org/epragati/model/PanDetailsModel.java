package org.epragati.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class PanDetailsModel {
	
	private int status;
	private String panNumber;
	private String isValid;
	private String firstName;
	private String lastName;
	private String middleName;
	private String salutation;
	private String panUpdatedDateAtITDEnd;
	//New ePragathi PAN response values
	private String pan;
	private String panStatus ;
	private String panTitle;
	private String lastUpdateDate;
	private String filter1;
	private String filter2;
	private String filter3;
	private String returnCode;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getPanUpdatedDateAtITDEnd() {
		return panUpdatedDateAtITDEnd;
	}
	public void setPanUpdatedDateAtITDEnd(String panUpdatedDateAtITDEnd) {
		this.panUpdatedDateAtITDEnd = panUpdatedDateAtITDEnd;
	}
	
	
	/**
	 * @return the pan
	 */
	public String getPan() {
		return pan;
	}
	/**
	 * @param pan the pan to set
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}
	/**
	 * @return the panStatus
	 */
	public String getPanStatus() {
		return panStatus;
	}
	/**
	 * @param panStatus the panStatus to set
	 */
	public void setPanStatus(String panStatus) {
		this.panStatus = panStatus;
	}
	/**
	 * @return the panTitle
	 */
	public String getPanTitle() {
		return panTitle;
	}
	/**
	 * @param panTitle the panTitle to set
	 */
	public void setPanTitle(String panTitle) {
		this.panTitle = panTitle;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * @return the filter1
	 */
	public String getFilter1() {
		return filter1;
	}
	/**
	 * @param filter1 the filter1 to set
	 */
	public void setFilter1(String filter1) {
		this.filter1 = filter1;
	}
	/**
	 * @return the filter2
	 */
	public String getFilter2() {
		return filter2;
	}
	/**
	 * @param filter2 the filter2 to set
	 */
	public void setFilter2(String filter2) {
		this.filter2 = filter2;
	}
	/**
	 * @return the filter3
	 */
	public String getFilter3() {
		return filter3;
	}
	/**
	 * @param filter3 the filter3 to set
	 */
	public void setFilter3(String filter3) {
		this.filter3 = filter3;
	}
	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PanDetailsModel [status=" + status + ", " + (panNumber != null ? "panNumber=" + panNumber + ", " : "")
				+ (isValid != null ? "isValid=" + isValid + ", " : "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (middleName != null ? "middleName=" + middleName + ", " : "")
				+ (salutation != null ? "salutation=" + salutation + ", " : "")
				+ (panUpdatedDateAtITDEnd != null ? "panUpdatedDateAtITDEnd=" + panUpdatedDateAtITDEnd + ", " : "")
				+ (pan != null ? "pan=" + pan + ", " : "") + (panStatus != null ? "panStatus=" + panStatus + ", " : "")
				+ (panTitle != null ? "panTitle=" + panTitle + ", " : "")
				+ (lastUpdateDate != null ? "lastUpdateDate=" + lastUpdateDate + ", " : "")
				+ (filter1 != null ? "filter1=" + filter1 + ", " : "")
				+ (filter2 != null ? "filter2=" + filter2 + ", " : "")
				+ (filter3 != null ? "filter3=" + filter3 + ", " : "")
				+ (returnCode != null ? "returnCode=" + returnCode : "") + "]";
	}
	
	
	
	
}

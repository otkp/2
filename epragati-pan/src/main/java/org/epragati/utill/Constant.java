package org.epragati.utill;

public enum Constant {
	Constant;
	public enum Status{
		E("E"),N("N");
		
		private String desc;
		

		private Status(String desc) {
			this.desc = desc;
		}

		/**
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}

		/**
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		
	}

}

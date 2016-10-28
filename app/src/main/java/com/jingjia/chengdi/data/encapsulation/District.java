package com.jingjia.chengdi.data.encapsulation;
/**
 * 用于Gson解析地区数据用，地区数据存放在assets
 */
public class District {
	private String district;
	private String id;

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "District [district=" + district + ", id=" + id + "]";
	}

}

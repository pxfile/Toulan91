package com.toulan.location;

import com.baidu.location.BDLocation;

/**
 * 
 * 说明：定位结果
 * 
 * 作者：fanly
 * 
 * 时间：2016-3-13 下午2:22:49
 * 
 * 版本：verson 1.0
 * 
 */
public class LocationResultBean {
	
	
	//是否定位成功，（定位成功包含地址，经纬度等信息）
	private boolean isSuccess = false;
	//结果码
	private int errorCode;
	//错误信息
	private String errorInfo;
	
	
	//定位时间
	private String time;
	//详细地址信息
	private String address;
	//城市
	private String city;
	//城市编码
	private String cityCode;
	//省份
	private String province;
	//街道信息
	private String street;
	//街道号码
	private String streetNumber;
	//国家
	private String country;
	//国家编码
	private String countryCode;
	//区县信息
	private String district;
	//维度
	private double latitude;
	//经度
	private double longitude;
	
	
	public LocationResultBean(){
		
	}
	
	public LocationResultBean(BDLocation location){
		convert(location);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	} 
	
	
	
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int code) {
		this.errorCode = code;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	private void convert(BDLocation location){
		if (location != null) {
			int code = -1;
			boolean isSuccess = false;
			
			//定位时间
			setTime(location.getTime());
			//详细地址信息
			setAddress(location.getAddrStr());
			//城市
			setCity(location.getCity());
			//城市编码
			setCityCode(location.getCityCode());
			//省份
			setProvince(location.getProvince());
			//街道信息
			setStreet(location.getStreet());
			//街道号码
			setStreetNumber(location.getStreetNumber());
			//国家
			setCountry(location.getCountry());
			//国家编码
			setCountryCode(location.getCountryCode());
			//区县信息
			setDistrict(location.getDistrict());
			//维度
			setLatitude(location.getLatitude());
			//经度
			setLongitude(location.getLongitude());
			code = location.getLocType();
			//错误码
			setErrorCode(code);
			//错误信息
			switch (code) {
			case BDLocation.TypeOffLineLocation://离线定位结果
				isSuccess = true;
				break;
			case BDLocation.TypeNetWorkLocation://网络定位成功
				isSuccess = true;
				break;
			case BDLocation.TypeGpsLocation://GPS定位成功
				isSuccess = true;
				break;
			default:
				isSuccess = false;
				break;
			}
			setSuccess(isSuccess);
		}else {
			setSuccess(false);
		}
	}

}

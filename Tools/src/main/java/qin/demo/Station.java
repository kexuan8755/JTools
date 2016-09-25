/**
 * 功能描述：
 * 代码设计：覃书伟
 * 工   号：0087
 * 版   权：深圳深青联科技有限公司
 * 时   间：2015-7-9下午2:43:52
 */
package qin.demo;

import java.text.DecimalFormat;

import qin.tool.annotation.Column;
import qin.tool.annotation.Column.ColumnType;
import qin.tool.annotation.Table;
import qin.tool.utils.StringUtils;


@Table
public class Station {
	@Column(type=ColumnType.EQUALS)
	private int band;
	@Column(type=ColumnType.EQUALS)
	int area;
	//电台id
	@Column
	private int id;
	@Column(type=ColumnType.EQUALS)
	private String name;
	@Column(type=ColumnType.EQUALS)
	private int freq;
	@Column(type=ColumnType.EQUALS)
	private String city = "unknown";
	@Column(type=ColumnType.EQUALS)
	private int online = 0; // 0 - 本地 1 - 在线

	public Station() {
		
	}
	
	public Station(int band, int id, String name, int freq, int area) {
		this();
		this.band = band;
		this.id = id;
		this.name = name;
		this.freq = freq;
		this.area = area;
	}
	
	/**
	 * @param band the band to set
	 */
	public void setBand(int band) {
		this.band = band;
	}
	
	/**
	 * @return the band
	 */
	public int getBand() {
		return band;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param freq the freq to set
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	/**
	 * @return the freq
	 */
	public int getFreq() {
		return freq;
	}
	
	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}
	
	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setOnline(int online) {
		this.online = online;
	}
	
	public int getOnline() {
		return online;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Station) {
			Station other = (Station) o;
			return band == other.band 
					&& area == other.area 
					&& freq == other.freq
					&& StringUtils.equals(city, other.city)
					&& online == other.online;
		}
		return super.equals(o);
	}
	
	
	
	public String getFreqStr() {
		DecimalFormat format = null;
		int freqFormat = 1;
		/*if(band >= FinalRadio.BAND_AM_INDEX_BEGIN && band < FinalRadio.BAND_AM_INDEX_END) {
			format = new DecimalFormat("#.##");
		} else if(band >= FinalRadio.BAND_FM_INDEX_BEGIN && band <= FinalRadio.BAND_FM_INDEX_END) {
			format = new DecimalFormat("#.00");
			freqFormat = 100;
		}*/
		final double val = freq / (float) freqFormat;
		return format == null ? null : format.format(val);
	}
	
	@Override
	public String toString() {
		return "area = " + area + /*" city = " + city + */" band = " + band + " id = " + id + " name = " + name + " freq = " + freq;
	}
}

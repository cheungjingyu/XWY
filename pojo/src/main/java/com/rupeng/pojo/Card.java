package com.rupeng.pojo;

import java.io.Serializable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//学习卡
public class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;//逻辑id
	private String name;//学习卡名称
	@JsonFormat(pattern="yyyy-MM-dd")
	
	private Date createTime;//学习卡创建时间
	private String description;//描述
	private String courseware;//学习卡课件下载地址
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCourseware() {
		return courseware;
	}
	public void setCourseware(String courseware) {
		this.courseware = courseware;
	}
	
}

package com.rupeng.pojo;

import java.io.Serializable;
import java.util.Date;
//用户表
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;//逻辑id
	private String name;//姓名
	private Boolean isMale;//是否是男性
	private String email;//邮箱
	private String phone;//手机号
	private String password;//密码
	private String passwordSalt;//密码盐
	private String school;//毕业或在读学校
	private Date createTime;//注册时间
	private Boolean isEmailVerified;//邮箱是否验证
	private Boolean isPhoneVerified;//手机号是否验证
	private Boolean isTeacher;//是否是教师
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
		User other = (User) obj;
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
	public Boolean getIsMale() {
		return isMale;
	}
	public void setIsMale(Boolean isMale) {
		this.isMale = isMale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}
	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	public Boolean getIsPhoneVerified() {
		return isPhoneVerified;
	}
	public void setIsPhoneVerified(Boolean isPhoneVerified) {
		this.isPhoneVerified = isPhoneVerified;
	}
	public Boolean getIsTeacher() {
		return isTeacher;
	}
	public void setIsTeacher(Boolean isTeacher) {
		this.isTeacher = isTeacher;
	}
	
}

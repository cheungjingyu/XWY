package com.rupeng.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
//问题答案表
public class QuestionAnswer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;//逻辑id
	private Long userId;//回答者id
	private String username;//回答者姓名
	private Long questionId;//问题id
	private Long parentId;//父回答id
	private String content;//回答内容
	private Date createTime;//回答时间
	private Boolean isAdopted;//是否被采纳
	
	//辅助，用于输出顶层答案与直接子答案的关系
	private List<QuestionAnswer> childQuestionAnswerList;
	
	
	public List<QuestionAnswer> getChildQuestionAnswerList() {
		return childQuestionAnswerList;
	}
	public void setChildQuestionAnswerList(List<QuestionAnswer> childQuestionAnswerList) {
		this.childQuestionAnswerList = childQuestionAnswerList;
	}
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
		QuestionAnswer other = (QuestionAnswer) obj;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsAdopted() {
		return isAdopted;
	}
	public void setIsAdopted(Boolean isAdopted) {
		this.isAdopted = isAdopted;
	}
	
}

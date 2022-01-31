package com.citius.models;

import java.util.List;

public class UserGroup {

	private Long userRoleId;

	private String userRole;

	// private List<UserModel> user;

	public String getUserRole() {
		return userRole;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

//	public List<UserModel> getUser() {
//		return user;
//	}
//
//	public void setUser(List<UserModel> user) {
//		this.user = user;
//	}

}

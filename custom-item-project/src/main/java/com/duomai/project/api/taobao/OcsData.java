package com.duomai.project.api.taobao;

import java.io.Serializable;

public class OcsData implements Serializable {
	
	private static final long serialVersionUID = -6577639761086855801L;
	
	private Integer value;

	public OcsData() {
	}

	public OcsData(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}

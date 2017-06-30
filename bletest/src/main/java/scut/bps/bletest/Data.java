package scut.bps.bletest;

import java.io.Serializable;

public class Data implements Serializable{
	private int size;
	private String[] s;
	
	public Data (String string) {
		s = string.split("55AA09");
		size = s.length;
	}

	public int getSize() {
		return size;
	}

	public String[] getS() {
		return s;
	}
	
	public String getS(int i) {
		return s[i];
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setS(String[] s) {
		this.s = s;
	}

	
	
}

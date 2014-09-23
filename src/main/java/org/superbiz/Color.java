package org.superbiz;

import org.jongo.marshall.jackson.oid.ObjectId;

public class Color {

	@ObjectId
	private String _id;
	
	private String name;
	private int r;
	private int g;
	private int b;
	
	public Color() {
		super();
	}
	public Color(String name, int r, int g, int b) {
		super();
		this.name = name;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public void setId(String id) {
		this._id = id;
	}
	public String getId() {
		return _id;
	}
	
	@Override
	public String toString() {
		return "Color [name=" + name + ", r=" + r + ", g=" + g + ", b=" + b
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + r;
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
		Color other = (Color) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (r != other.r)
			return false;
		return true;
	}
	
}

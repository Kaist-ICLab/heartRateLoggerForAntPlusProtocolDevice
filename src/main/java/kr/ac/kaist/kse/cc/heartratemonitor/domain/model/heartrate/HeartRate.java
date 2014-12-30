package kr.ac.kaist.kse.cc.heartratemonitor.domain.model.heartrate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HeartRate {
	private String name;
	private int value;
	private long time;

	public HeartRate(String name, int value, long time) {
		this.name = name;
		this.value = value;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public long getTime() {
		return time;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(getTime())
				.toHashCode();
	}

	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass())
			return false;
		if (o == this)
			return true;
		HeartRate other = (HeartRate) o;
		return new EqualsBuilder().append(getName(), other.getName())
				.append(getTime(), other.getTime()).isEquals();
	}
}

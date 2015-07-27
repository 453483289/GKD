package com.gkd.instrument.callgraph;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parameter")
public class Parameter {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "parameterId", unique = true, nullable = false)
	private Integer parameterId;

	@ManyToOne
	private JmpData jmpData;

	public String name;
	public String type;
	public int size;
	public Long physicalAddress;

	public String parameterOffset;

	public Long value;

	public Parameter() {
	}

	public Parameter(JmpData jmpData, String name, String type, int size, Long physicalAddress, String parameterOffset, Long value) {
		this.jmpData = jmpData;
		this.name = name;
		this.type = type;
		this.size = size;
		this.physicalAddress = physicalAddress;
		this.parameterOffset = parameterOffset;
		this.value = value;
	}

	public String toString() {
		return name + "(" + type + ")";
	}

}

package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Ladestation extends DaiSmartGrid {

	public Ladestation() {
		super();
	}

	@Override
	void normalize() {
	}

	public interface LadestationMixIn{

		@JsonSetter("I_r")
		void setCurrent_I1();
	}
}

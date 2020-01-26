package com.microsmartgrid.database.model.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PV extends Readings {

	protected PV() {
		super();
	}

	public PV(@JsonProperty("reactive_power_total") Float reactive_total,
			  @JsonProperty("active_power_total") Float active_total,
			  @JsonProperty("reactive_energy_A_minus") Float reactive_A_minus,
			  @JsonProperty("reactive_energy_A_plus") Float reactive_A_plus) {
		this();
		super.setReactive_power_Q_total(reactive_total);
		super.setActive_power_P_total(active_total);
		// TODO: figure out where this discrepancy is coming from
		super.setReactive_energy_R_minus(reactive_A_minus);
		super.setReactive_energy_R_minus(reactive_A_plus);
	}
}

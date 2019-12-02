package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Main extends DaiSmartGrid {

	protected Main() {
		super();
	}

	public Main(@JsonProperty("reactive_power_total") float reactive_total,
				@JsonProperty("active_power_total") float active_total,
				@JsonProperty("reactive_energy_A_minus") float reactive_A_minus,
				@JsonProperty("reactive_energy_A_plus") float reactive_A_plus,
				@JsonProperty("frequency_Mgrid") float frequency) {
		this();
		super.setReactive_power_Q_total(reactive_total);
		super.setActive_power_P_total(active_total);
		// TODO: figure out where this discrepancy is coming from
		super.setReactive_energy_R_minus(reactive_A_minus);
		super.setReactive_energy_R_minus(reactive_A_plus);
		super.setFrequency_grid(frequency);
	}
}

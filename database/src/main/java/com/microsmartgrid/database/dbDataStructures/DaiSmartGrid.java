package com.microsmartgrid.database.dbDataStructures;

import com.google.gson.Gson;

import java.time.LocalDateTime;

public abstract class DaiSmartGrid implements AbstractDevice {
	//interface implementation
	private LocalDateTime timestamp = null;
	private String device_name = null;
	private Gson meta_information = null;

	//inherent values
	private float
		current_I_avg,
		current_I1,
		current_I2,
		current_I3,
		voltage_U_avg,
		voltage_U1,
		voltage_U2,
		voltage_U3,
		active_energy_A_plus,
		active_energy_A_minus,
		active_power_P1,
		active_power_P2,
		active_power_P3,
		reactive_energy_R_plus,
		reactive_energy_R_minus,
		reactive_power_Q_total,
		reactive_power_Q1,
		reactive_power_Q2,
		reactive_power_Q3,
		apparent_power_S_total,
		apparent_power_S1,
		apparent_power_S2,
		apparent_power_S3,
		frequency_grid;

	protected DaiSmartGrid() {
		//TODO: take JSON and parse into values
	}

	/**
	 * Translates input into DaiSmartGrid-values
	 */
	abstract void normalize();
}

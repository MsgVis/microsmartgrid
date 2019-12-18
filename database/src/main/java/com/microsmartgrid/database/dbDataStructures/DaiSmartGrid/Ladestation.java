package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ladestation extends Readings {
	protected Ladestation() {
		super();
	}

	public Ladestation(@JsonProperty("I_avg") Float I_avg,
					   @JsonProperty("I_r") Float I_r,
					   @JsonProperty("I_s") Float I_s,
					   @JsonProperty("I_t") Float I_t,
					   @JsonProperty("U_avg") Float U_avg,
					   @JsonProperty("U_r") Float U_r,
					   @JsonProperty("U_s") Float U_s,
					   @JsonProperty("U_t") Float U_t,
					   @JsonProperty("P_total") Float P_total,
					   @JsonProperty("P_r") Float P_r,
					   @JsonProperty("P_s") Float P_s,
					   @JsonProperty("P_t") Float P_t,
					   @JsonProperty("Q_total") Float Q_total,
					   @JsonProperty("Q_r") Float Q_r,
					   @JsonProperty("Q_s") Float Q_s,
					   @JsonProperty("Q_t") Float Q_t,
					   @JsonProperty("S_total") Float S_total,
					   @JsonProperty("S_r") Float S_r,
					   @JsonProperty("S_s") Float S_s,
					   @JsonProperty("S_t") Float S_t,
					   @JsonProperty("A_plus") Float A_plus,
					   @JsonProperty("A_minus") Float A_minus,
					   @JsonProperty("R_plus") Float R_plus,
					   @JsonProperty("R_minus") Float R_minus,
					   @JsonProperty("f") Float f) {
		this();
		super.setCurrent_I_avg(I_avg);
		super.setCurrent_I1(I_r);
		super.setCurrent_I2(I_s);
		super.setCurrent_I3(I_t);
		super.setVoltage_U_avg(U_avg);
		super.setVoltage_U1(U_r);
		super.setVoltage_U2(U_s);
		super.setVoltage_U3(U_t);
		super.setActive_power_P_total(P_total);
		super.setActive_power_P1(P_r);
		super.setActive_power_P2(P_s);
		super.setActive_power_P3(P_t);
		super.setReactive_power_Q_total(Q_total);
		super.setReactive_power_Q1(Q_r);
		super.setReactive_power_Q2(Q_s);
		super.setReactive_power_Q3(Q_t);
		super.setApparent_power_S_total(S_total);
		super.setApparent_power_S1(S_r);
		super.setApparent_power_S2(S_s);
		super.setApparent_power_S3(S_t);
		super.setActive_energy_A_plus(A_plus);
		super.setActive_energy_A_minus(A_minus);
		super.setReactive_energy_R_plus(R_plus);
		super.setReactive_energy_R_minus(R_minus);
		super.setFrequency_grid(f);
	}
}

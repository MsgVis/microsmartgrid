package com.microsmartgrid.database.dbDataStructures.DaiSmartGrid;

import com.microsmartgrid.database.dbDataStructures.Device;

public abstract class DaiSmartGrid extends Device {

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
		active_power_P_total,
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
		super();
	}

	public float getCurrent_I_avg() {
		return this.current_I_avg;
	}

	public void setCurrent_I_avg(float current_I_avg) {
		this.current_I_avg = current_I_avg;
	}

	public float getCurrent_I1() {
		return this.current_I1;
	}

	public void setCurrent_I1(float current_I1) {
		this.current_I1 = current_I1;
	}

	public float getCurrent_I2() {
		return this.current_I2;
	}

	public void setCurrent_I2(float current_I2) {
		this.current_I2 = current_I2;
	}

	public float getCurrent_I3() {
		return this.current_I3;
	}

	public void setCurrent_I3(float current_I3) {
		this.current_I3 = current_I3;
	}

	public float getVoltage_U_avg() {
		return this.voltage_U_avg;
	}

	public void setVoltage_U_avg(float voltage_U_avg) {
		this.voltage_U_avg = voltage_U_avg;
	}

	public float getVoltage_U1() {
		return this.voltage_U1;
	}

	public void setVoltage_U1(float voltage_U1) {
		this.voltage_U1 = voltage_U1;
	}

	public float getVoltage_U2() {
		return this.voltage_U2;
	}

	public void setVoltage_U2(float voltage_U2) {
		this.voltage_U2 = voltage_U2;
	}

	public float getVoltage_U3() {
		return this.voltage_U3;
	}

	public void setVoltage_U3(float voltage_U3) {
		this.voltage_U3 = voltage_U3;
	}

	public float getActive_energy_A_plus() {
		return this.active_energy_A_plus;
	}

	public void setActive_energy_A_plus(float active_energy_A_plus) {
		this.active_energy_A_plus = active_energy_A_plus;
	}

	public float getActive_energy_A_minus() {
		return this.active_energy_A_minus;
	}

	public void setActive_energy_A_minus(float active_energy_A_minus) {
		this.active_energy_A_minus = active_energy_A_minus;
	}

	public float getActive_power_P_total() {
		return this.active_power_P_total;
	}

	public void setActive_power_P_total(float active_power_P_total) {
		this.active_power_P_total = active_power_P_total;
	}

	public float getActive_power_P1() {
		return this.active_power_P1;
	}

	public void setActive_power_P1(float active_power_P1) {
		this.active_power_P1 = active_power_P1;
	}

	public float getActive_power_P2() {
		return this.active_power_P2;
	}

	public void setActive_power_P2(float active_power_P2) {
		this.active_power_P2 = active_power_P2;
	}

	public float getActive_power_P3() {
		return this.active_power_P3;
	}

	public void setActive_power_P3(float active_power_P3) {
		this.active_power_P3 = active_power_P3;
	}

	public float getReactive_energy_R_plus() {
		return this.reactive_energy_R_plus;
	}

	public void setReactive_energy_R_plus(float reactive_energy_R_plus) {
		this.reactive_energy_R_plus = reactive_energy_R_plus;
	}

	public float getReactive_energy_R_minus() {
		return this.reactive_energy_R_minus;
	}

	public void setReactive_energy_R_minus(float reactive_energy_R_minus) {
		this.reactive_energy_R_minus = reactive_energy_R_minus;
	}

	public float getReactive_power_Q_total() {
		return this.reactive_power_Q_total;
	}

	public void setReactive_power_Q_total(float reactive_power_Q_total) {
		this.reactive_power_Q_total = reactive_power_Q_total;
	}

	public float getReactive_power_Q1() {
		return this.reactive_power_Q1;
	}

	public void setReactive_power_Q1(float reactive_power_Q1) {
		this.reactive_power_Q1 = reactive_power_Q1;
	}

	public float getReactive_power_Q2() {
		return this.reactive_power_Q2;
	}

	public void setReactive_power_Q2(float reactive_power_Q2) {
		this.reactive_power_Q2 = reactive_power_Q2;
	}

	public float getReactive_power_Q3() {
		return this.reactive_power_Q3;
	}

	public void setReactive_power_Q3(float reactive_power_Q3) {
		this.reactive_power_Q3 = reactive_power_Q3;
	}

	public float getApparent_power_S_total() {
		return this.apparent_power_S_total;
	}

	public void setApparent_power_S_total(float apparent_power_S_total) {
		this.apparent_power_S_total = apparent_power_S_total;
	}

	public float getApparent_power_S1() {
		return this.apparent_power_S1;
	}

	public void setApparent_power_S1(float apparent_power_S1) {
		this.apparent_power_S1 = apparent_power_S1;
	}

	public float getApparent_power_S2() {
		return this.apparent_power_S2;
	}

	public void setApparent_power_S2(float apparent_power_S2) {
		this.apparent_power_S2 = apparent_power_S2;
	}

	public float getApparent_power_S3() {
		return this.apparent_power_S3;
	}

	public void setApparent_power_S3(float apparent_power_S3) {
		this.apparent_power_S3 = apparent_power_S3;
	}

	public float getFrequency_grid() {
		return this.frequency_grid;
	}

	public void setFrequency_grid(float frequency_grid) {
		this.frequency_grid = frequency_grid;
	}
}

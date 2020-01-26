package com.microsmartgrid.database.model.DaiSmartGrid;

import com.microsmartgrid.database.model.AbstractDevice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Standardized Implementation of all meters. Saved to a table in the database.
 */
@Entity(name = "readings")
@Table(name = "readings")
public abstract class Readings extends AbstractDevice {

	@Column(name = "i_avg")
	private Float current_I_avg;
	@Column(name = "i_r")
	private Float current_I1;
	@Column(name = "i_s")
	private Float current_I2;
	@Column(name = "i_t")
	private Float current_I3;
	@Column(name = "u_avg")
	private Float voltage_U_avg;
	@Column(name = "u_r")
	private Float voltage_U1;
	@Column(name = "u_s")
	private Float voltage_U2;
	@Column(name = "u_t")
	private Float voltage_U3;
	@Column(name = "a_plus")
	private Float active_energy_A_plus;
	@Column(name = "a_minus")
	private Float active_energy_A_minus;
	@Column(name = "p_total")
	private Float active_power_P_total;
	@Column(name = "p_r")
	private Float active_power_P1;
	@Column(name = "p_s")
	private Float active_power_P2;
	@Column(name = "p_t")
	private Float active_power_P3;
	@Column(name = "r_plus")
	private Float reactive_energy_R_plus;
	@Column(name = "r_minus")
	private Float reactive_energy_R_minus;
	@Column(name = "q_total")
	private Float reactive_power_Q_total;
	@Column(name = "q_r")
	private Float reactive_power_Q1;
	@Column(name = "q_s")
	private Float reactive_power_Q2;
	@Column(name = "q_t")
	private Float reactive_power_Q3;
	@Column(name = "s_total")
	private Float apparent_power_S_total;
	@Column(name = "s_r")
	private Float apparent_power_S1;
	@Column(name = "s_s")
	private Float apparent_power_S2;
	@Column(name = "s_t")
	private Float apparent_power_S3;
	@Column(name = "f")
	private Float frequency_grid;

	protected Readings() {
		super();
	}

	public Float getCurrent_I_avg() {
		return this.current_I_avg;
	}

	public void setCurrent_I_avg(Float current_I_avg) {
		this.current_I_avg = current_I_avg;
	}

	public Float getCurrent_I1() {
		return this.current_I1;
	}

	public void setCurrent_I1(Float current_I1) {
		this.current_I1 = current_I1;
	}

	public Float getCurrent_I2() {
		return this.current_I2;
	}

	public void setCurrent_I2(Float current_I2) {
		this.current_I2 = current_I2;
	}

	public Float getCurrent_I3() {
		return this.current_I3;
	}

	public void setCurrent_I3(Float current_I3) {
		this.current_I3 = current_I3;
	}

	public Float getVoltage_U_avg() {
		return this.voltage_U_avg;
	}

	public void setVoltage_U_avg(Float voltage_U_avg) {
		this.voltage_U_avg = voltage_U_avg;
	}

	public Float getVoltage_U1() {
		return this.voltage_U1;
	}

	public void setVoltage_U1(Float voltage_U1) {
		this.voltage_U1 = voltage_U1;
	}

	public Float getVoltage_U2() {
		return this.voltage_U2;
	}

	public void setVoltage_U2(Float voltage_U2) {
		this.voltage_U2 = voltage_U2;
	}

	public Float getVoltage_U3() {
		return this.voltage_U3;
	}

	public void setVoltage_U3(Float voltage_U3) {
		this.voltage_U3 = voltage_U3;
	}

	public Float getActive_energy_A_plus() {
		return this.active_energy_A_plus;
	}

	public void setActive_energy_A_plus(Float active_energy_A_plus) {
		this.active_energy_A_plus = active_energy_A_plus;
	}

	public Float getActive_energy_A_minus() {
		return this.active_energy_A_minus;
	}

	public void setActive_energy_A_minus(Float active_energy_A_minus) {
		this.active_energy_A_minus = active_energy_A_minus;
	}

	public Float getActive_power_P_total() {
		return this.active_power_P_total;
	}

	public void setActive_power_P_total(Float active_power_P_total) {
		this.active_power_P_total = active_power_P_total;
	}

	public Float getActive_power_P1() {
		return this.active_power_P1;
	}

	public void setActive_power_P1(Float active_power_P1) {
		this.active_power_P1 = active_power_P1;
	}

	public Float getActive_power_P2() {
		return this.active_power_P2;
	}

	public void setActive_power_P2(Float active_power_P2) {
		this.active_power_P2 = active_power_P2;
	}

	public Float getActive_power_P3() {
		return this.active_power_P3;
	}

	public void setActive_power_P3(Float active_power_P3) {
		this.active_power_P3 = active_power_P3;
	}

	public Float getReactive_energy_R_plus() {
		return this.reactive_energy_R_plus;
	}

	public void setReactive_energy_R_plus(Float reactive_energy_R_plus) {
		this.reactive_energy_R_plus = reactive_energy_R_plus;
	}

	public Float getReactive_energy_R_minus() {
		return this.reactive_energy_R_minus;
	}

	public void setReactive_energy_R_minus(Float reactive_energy_R_minus) {
		this.reactive_energy_R_minus = reactive_energy_R_minus;
	}

	public Float getReactive_power_Q_total() {
		return this.reactive_power_Q_total;
	}

	public void setReactive_power_Q_total(Float reactive_power_Q_total) {
		this.reactive_power_Q_total = reactive_power_Q_total;
	}

	public Float getReactive_power_Q1() {
		return this.reactive_power_Q1;
	}

	public void setReactive_power_Q1(Float reactive_power_Q1) {
		this.reactive_power_Q1 = reactive_power_Q1;
	}

	public Float getReactive_power_Q2() {
		return this.reactive_power_Q2;
	}

	public void setReactive_power_Q2(Float reactive_power_Q2) {
		this.reactive_power_Q2 = reactive_power_Q2;
	}

	public Float getReactive_power_Q3() {
		return this.reactive_power_Q3;
	}

	public void setReactive_power_Q3(Float reactive_power_Q3) {
		this.reactive_power_Q3 = reactive_power_Q3;
	}

	public Float getApparent_power_S_total() {
		return this.apparent_power_S_total;
	}

	public void setApparent_power_S_total(Float apparent_power_S_total) {
		this.apparent_power_S_total = apparent_power_S_total;
	}

	public Float getApparent_power_S1() {
		return this.apparent_power_S1;
	}

	public void setApparent_power_S1(Float apparent_power_S1) {
		this.apparent_power_S1 = apparent_power_S1;
	}

	public Float getApparent_power_S2() {
		return this.apparent_power_S2;
	}

	public void setApparent_power_S2(Float apparent_power_S2) {
		this.apparent_power_S2 = apparent_power_S2;
	}

	public Float getApparent_power_S3() {
		return this.apparent_power_S3;
	}

	public void setApparent_power_S3(Float apparent_power_S3) {
		this.apparent_power_S3 = apparent_power_S3;
	}

	public Float getFrequency_grid() {
		return this.frequency_grid;
	}

	public void setFrequency_grid(Float frequency_grid) {
		this.frequency_grid = frequency_grid;
	}
}

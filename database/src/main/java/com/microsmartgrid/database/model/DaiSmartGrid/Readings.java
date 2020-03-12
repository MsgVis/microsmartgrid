package com.microsmartgrid.database.model.DaiSmartGrid;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsmartgrid.database.model.AbstractDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Standardized Implementation of all meters.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Readings extends AbstractDevice implements Serializable {

	/**
	 * Use java name for database column name or annotate @Column(name="").
	 * Use @JsonProperty for the desired output json field name (defaults from java name).
	 * Use @JsonAlias for any variations of the same field in input json.
	 */
	@JsonAlias({"f", "frequency_Mgrid", "frequency_Grid"})
	@JsonProperty("Frequency")
	private Float f;

	// current
	@JsonProperty("I_avg")
	private Float i_avg;
	@JsonAlias({"current_I1"})
	@JsonProperty("I_r")
	private Float i_r;
	@JsonAlias({"current_I2"})
	@JsonProperty("I_s")
	private Float i_s;
	@JsonAlias({"current_I3"})
	@JsonProperty("I_t")
	private Float i_t;

	// voltage
	@JsonProperty("U_avg")
	private Float u_avg;
	@JsonAlias({"voltage_U1"})
	@JsonProperty("U_r")
	private Float u_r;
	@JsonAlias({"voltage_U2"})
	@JsonProperty("U_s")
	private Float u_s;
	@JsonAlias({"voltage_U3"})
	@JsonProperty("U_t")
	private Float u_t;

	// active energy
	@JsonAlias({"active_energy_A_plus"})
	@JsonProperty("A_plus")
	private Float a_plus;
	@JsonAlias({"active_energy_A_minus"})
	@JsonProperty("A_minus")
	private Float a_minus;

	// active power
	@JsonAlias({"active_power_total"})
	@JsonProperty("P_total")
	private Float p_total;
	@JsonAlias({"active_power_P1"})
	@JsonProperty("P_r")
	private Float p_r;
	@JsonAlias({"active_power_P2"})
	@JsonProperty("P_s")
	private Float p_s;
	@JsonAlias({"active_power_P3"})
	@JsonProperty("P_t")
	private Float p_t;

	// reactive energy
	@JsonAlias({"reactive_energy_A_plus"})
	@JsonProperty("R_plus")
	private Float r_plus;
	@JsonAlias({"reactive_energy_A_minus"})
	@JsonProperty("R_minus")
	private Float r_minus;

	// reactive power
	@JsonAlias({"reactive_power_total"})
	@JsonProperty("Q_total")
	private Float q_total;
	@JsonAlias({"reactive_power_Q1"})
	@JsonProperty("Q_r")
	private Float q_r;
	@JsonAlias({"reactive_power_Q2"})
	@JsonProperty("Q_s")
	private Float q_s;
	@JsonAlias({"reactive_power_Q3"})
	@JsonProperty("Q_t")
	private Float q_t;

	// apparent power
	@JsonProperty("S_total")
	private Float s_total;
	@JsonProperty("S_r")
	private Float s_r;
	@JsonProperty("S_s")
	private Float s_s;
	@JsonProperty("S_t")
	private Float s_t;

	public Readings() {
		super();
	}
}

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

	// current
	@JsonProperty("I_avg")
	private Float iAvg;
	@JsonAlias({"current_I1"})
	@JsonProperty("I_r")
	private Float iR;
	@JsonAlias({"current_I2"})
	@JsonProperty("I_s")
	private Float iS;
	@JsonAlias({"current_I3"})
	@JsonProperty("I_t")
	private Float iT;

	// voltage
	@JsonProperty("U_avg")
	private Float uAvg;
	@JsonAlias({"voltage_U1"})
	@JsonProperty("U_r")
	private Float uR;
	@JsonAlias({"voltage_U2"})
	@JsonProperty("U_s")
	private Float uS;
	@JsonAlias({"voltage_U3"})
	@JsonProperty("U_t")
	private Float uT;

	// active energy
	@JsonAlias({"active_energy_A_plus"})
	@JsonProperty("A_plus")
	private Float aPlus;
	@JsonAlias({"active_energy_A_minus"})
	@JsonProperty("A_minus")
	private Float aMinus;

	// active power
	@JsonAlias({"active_power_total"})
	@JsonProperty("P_total")
	private Float pTotal;
	@JsonAlias({"active_power_P1"})
	@JsonProperty("P_r")
	private Float pR;
	@JsonAlias({"active_power_P2"})
	@JsonProperty("P_s")
	private Float pS;
	@JsonAlias({"active_power_P3"})
	@JsonProperty("P_t")
	private Float pT;

	// reactive energy
	@JsonAlias({"reactive_energy_A_plus"})
	@JsonProperty("R_plus")
	private Float rPlus;
	@JsonAlias({"reactive_energy_A_minus"})
	@JsonProperty("R_minus")
	private Float rMinus;

	// reactive power
	@JsonAlias({"reactive_power_total"})
	@JsonProperty("Q_total")
	private Float qTotal;
	@JsonAlias({"reactive_power_Q1"})
	@JsonProperty("Q_r")
	private Float qR;
	@JsonAlias({"reactive_power_Q2"})
	@JsonProperty("Q_s")
	private Float qS;
	@JsonAlias({"reactive_power_Q3"})
	@JsonProperty("Q_t")
	private Float qT;

	// apparent power
	@JsonProperty("S_total")
	private Float sTotal;
	@JsonProperty("S_r")
	private Float sR;
	@JsonProperty("S_s")
	private Float sS;
	@JsonProperty("S_t")
	private Float sT;

	@JsonAlias({"frequency_Mgrid", "frequency_Grid"})
	private Float f;

	public Readings() {
		super();
	}
}

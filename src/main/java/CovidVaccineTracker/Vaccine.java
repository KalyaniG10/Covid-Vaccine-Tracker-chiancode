package CovidVaccineTracker;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;

@DataType()
public final class Vaccine {

	@Property()
	private final String beneficiaryName;

	@Property()
	private final String beneficiaryAge;

	@Property()
	private final String beneficiaryGender;

	@Property()
	private final String identityProof;

	@Property()
	private final String referenceId;
	
	@Property()
	private final String vaccineName;
	
	@Property()
	private final String dateOfDose;
	
	@Property()
	private final String doseNumber;

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public String getBeneficiaryAge() {
		return beneficiaryAge;
	}

	public String getBeneficiaryGender() {
		return beneficiaryGender;
	}

	public String getIdentityProof() {
		return identityProof;
	}

	public String getReferenceId() {
		return referenceId;
	}
	
	public String getVaccineName() {
		return vaccineName;
	}
	
	public String getDateOfDose() {
		return dateOfDose;
	}
	
	public String getDoseNumber() {
		return doseNumber;
	}

	public Vaccine(@JsonProperty("beneficiaryName") final String beneficiaryName, @JsonProperty("beneficiaryAge") final String beneficiaryAge,
			@JsonProperty("beneficiaryGender") final String beneficiaryGender, @JsonProperty("identityProof") final String identityProof,
			@JsonProperty("referenceId") final String referenceId, @JsonProperty("vaccineName") final String vaccineName, @JsonProperty("dateOfDose") final String dateOfDose, @JsonProperty("doseNumber") final String doseNumber ) {
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryAge = beneficiaryAge;
		this.beneficiaryGender = beneficiaryGender;
		this.identityProof = identityProof;
		this.referenceId = referenceId;
		this.vaccineName = vaccineName;
		this.dateOfDose = dateOfDose;
		this.doseNumber = doseNumber;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		Vaccine vacc = (Vaccine) obj;

		return Objects.deepEquals(new String[] { getBeneficiaryName(), getBeneficiaryAge(), getBeneficiaryGender(), getIdentityProof(), getReferenceId(),getVaccineName(), getDateOfDose(), getDoseNumber() },
				new String[] { vacc.getBeneficiaryName(), vacc.getBeneficiaryAge(), vacc.getBeneficiaryGender(), vacc.getIdentityProof(), vacc.getReferenceId(), vacc.getVaccineName(), vacc.getDateOfDose(), vacc.getDoseNumber() });
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBeneficiaryName(), getBeneficiaryAge(), getBeneficiaryGender(), getIdentityProof(), getReferenceId(),getVaccineName(), getDateOfDose(), getDoseNumber());
	}

}
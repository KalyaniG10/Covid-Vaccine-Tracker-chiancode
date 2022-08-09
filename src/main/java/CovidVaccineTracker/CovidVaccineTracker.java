package CovidVaccineTracker;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.owlike.genson.Genson;

@Contract(name = "CovidVaccineTracker", info = @Info(title = "VaccineTracker contract", description = "A Cvoid Vaccine Tracker chaincode example", version = "0.0.1-SNAPSHOT"))

@Default
public final class CovidVaccineTracker implements ContractInterface {

	private final Genson genson = new Genson();

	private enum VaccineTrackerErrors {
		VACCINE_NOT_GIVEN, PERSON_NOT_VACCINATED, VACCINE_FIRST_DOSE_ALREADY_GIVEN, PERSON_FULLY_VACCINATED
	}

	/**
	 * Initilize the ledger with one entry
	 *
	 * @param ctx the transaction context
	 */
	@Transaction()
	public void initLedger(final Context ctx) {

		ChaincodeStub stub = ctx.getStub();
		Vaccine vaccine = new Vaccine("Mahesh", "30", "Male", "124788898730", "4878735690", "COVISHIELD", "15April2021", "1");
		String vaccineState = genson.serialize(vaccine);
		stub.putStringState("124788898730", vaccineState);
	}

	/**
	 * Add new vaccines recipient on the ledger.
	 *
	 * @param ctx the transaction context
	 * @param name the name of the recipient
	 * @param age the age of the recipient
	 * @param gender the gender of the recipient
	 * @param identity the identity proof of the recipient
	 * @param vaccineName the name of the vaccine
	 * @param date the date of the vaccine administration
	 * @param vaccineDose the dose number of the vaccine
	 * @return the referenceId
	 */

	@Transaction()
	public Vaccine addVaccineRecipient(final Context ctx, final String name, final String age, 
			final String gender,final String identity, final String refId, final String vaccineName, final String date, 
			final String vaccineDose) {

		ChaincodeStub stub = ctx.getStub();

		String vaccineState = stub.getStringState(identity);

		if (!vaccineState.isEmpty()) {
			Vaccine vacc = genson.deserialize(vaccineState, Vaccine.class);
			if (vacc.getDoseNumber() == "1") {
				String errorMessage = String.format("Person %s already given first vaccine dose", name);
				System.out.println(errorMessage);
				throw new ChaincodeException(errorMessage,
						VaccineTrackerErrors.VACCINE_FIRST_DOSE_ALREADY_GIVEN.toString());
			}

			if (vacc.getDoseNumber() == "2") {
				String errorMessage = String.format("Person %s is alraedy fully vaccinated", name);
				System.out.println(errorMessage);
				throw new ChaincodeException(errorMessage, VaccineTrackerErrors.PERSON_FULLY_VACCINATED.toString());
			}
		}
		Vaccine vaccine = new Vaccine(name, age, gender, identity, refId, vaccineName, date, vaccineDose);
		vaccineState = genson.serialize(vaccine);
		stub.putStringState(identity, vaccineState);
		return vaccine;
	}

	/**
	 * Update second dose status on the ledger for recipient.
	 *
	 * @param ctx      the transaction context
	 * @param date     date of the second dose
	 * @param identity identity of the recipient
	 * @return the reference Id
	 */
	@Transaction()
	public String updateSecondDose(final Context ctx, final String date, final String identity) {
		ChaincodeStub stub = ctx.getStub();

		String vaccineState = stub.getStringState(identity);

		if (vaccineState.isEmpty()) {
			String errorMessage = String.format("First dose is not given to recipient %s yet", identity);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, VaccineTrackerErrors.VACCINE_NOT_GIVEN.toString());
		}

		if (!vaccineState.isEmpty()) {
			Vaccine vacc = genson.deserialize(vaccineState, Vaccine.class);
			if (vacc.getDoseNumber() == "2") {
				String errorMessage = String.format("Person %s is alraedy fully vaccinated", identity);
				System.out.println(errorMessage);
				throw new ChaincodeException(errorMessage, VaccineTrackerErrors.PERSON_FULLY_VACCINATED.toString());
			}
		}

		Vaccine vacc = genson.deserialize(vaccineState, Vaccine.class);

		Vaccine updatedvacc = new Vaccine(vacc.getBeneficiaryName(), vacc.getBeneficiaryAge(),
				vacc.getBeneficiaryGender(), vacc.getIdentityProof(), vacc.getReferenceId(), vacc.getVaccineName(),
				date, "2");

		String newVaccineState = genson.serialize(updatedvacc);

		stub.putStringState(identity, newVaccineState);

		return vacc.getReferenceId();
	}

	/**
	 * Retrieves a vaccination status of the person from the ledger.
	 *
	 * @param ctx the transaction context
	 * @param identity identity of the recipient
	 * @return the status
	 */
	@Transaction()
	public String viewVaccineStatus(final Context ctx, final String identity) {
		ChaincodeStub stub = ctx.getStub();
		String vaccineState = stub.getStringState(identity);
		if (vaccineState.isEmpty()) {
			return "Person is not vaccinated yet";
		}

		Vaccine vacc = genson.deserialize(vaccineState, Vaccine.class);
		return "Person is vaccinated with " + vacc.getDoseNumber() + " dose";
	}
}
package ca.mcgill.ecse321.petshelter.service.extrafeatures;

import com.google.common.base.Joiner;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Class that validates a password to be strong. Taken from Baeldung tutorials.
 * @author louis
 *
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public void initialize(ValidPassword arg0) {
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 60),
				new UppercaseCharacterRule(1), new DigitCharacterRule(1), new SpecialCharacterRule(1)));

		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
				.addConstraintViolation();
		return false;
	}
}
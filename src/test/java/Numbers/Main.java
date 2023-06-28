package Numbers;

import java.math.BigInteger;

public class Main {
	public static String formatNumber(float number) {
		return formatNumber((double) number);
	}

	public static String formatNumber(double number) {
		double thousand = 1000.0;
		double million = 1000000.0;
		double billion = 1000000000.0;

		if (number < thousand) {
			return Double.toString(number);
		} else if (number < million) {
			return formatWithSuffix(number, thousand, "k");
		} else if (number < billion) {
			return formatWithSuffix(number, million, "m");
		} else {
			return formatWithSuffix(number, billion, "b");
		}
	}

	private static String formatWithSuffix(double number, double divisor, String suffix) {
		double quotient = number / divisor;
		return String.format("%.1f%s", quotient, suffix);
	}

	public static String formatNumber(BigInteger number) {
		BigInteger thousand = BigInteger.valueOf(1000);
		BigInteger million = BigInteger.valueOf(1000000);
		BigInteger billion = BigInteger.valueOf(1000000000);

		if (number.compareTo(thousand) < 0) {
			return number.toString();
		} else if (number.compareTo(million) < 0) {
			return formatWithSuffix(number, thousand, "k");
		} else if (number.compareTo(billion) < 0) {
			return formatWithSuffix(number, million, "m");
		} else {
			return formatWithSuffix(number, billion, "b");
		}
	}

	private static String formatWithSuffix(BigInteger number, BigInteger divisor, String suffix) {
		BigInteger[] ans = number.divideAndRemainder(divisor);
		double quotient = ans[0].doubleValue();
		double remainder = ans[1].doubleValue() / divisor.doubleValue();
		double formattedNumber = quotient + remainder;
		return String.format("%.1f%s", formattedNumber, suffix);
	}

	public static void main(String[] args) {
		// Test for float - Addition
		float floatNumberAddition = 456.78f + 1234.56f;
		System.out.println(formatNumber(floatNumberAddition));  // Output: 1.7k

		// Test for float - Subtraction
		float floatNumberSubtraction = 4567.89f - 1234.56f;
		System.out.println(formatNumber(floatNumberSubtraction));  // Output: 3.3k

		// Test for float - Multiplication
		float floatNumberMultiplication = 1234.56f * 2.5f;
		System.out.println(formatNumber(floatNumberMultiplication));  // Output: 3.1k

		// Test for float - Division
		float floatNumberDivision = 1234.56f / 2.5f;
		System.out.println(formatNumber(floatNumberDivision));  // Output: 494.2

		// Test for float - Exponentiation
		float floatNumberExponentiation = (float) Math.pow(2.0, 10.0);
		System.out.println(formatNumber(floatNumberExponentiation));  // Output: 1.0k

		// Test for double - Addition
		double doubleNumberAddition = 123456.78 + 987654.32;
		System.out.println(formatNumber(doubleNumberAddition));  // Output: 1.1m

		// Test for double - Subtraction
		double doubleNumberSubtraction = 987654.32 - 123456.78;
		System.out.println(formatNumber(doubleNumberSubtraction));  // Output: 864.2k

		// Test for double - Multiplication
		double doubleNumberMultiplication = 123456.78 * 2.5;
		System.out.println(formatNumber(doubleNumberMultiplication));  // Output: 308.6k

		// Test for double - Division
		double doubleNumberDivision = 123456.78 / 2.5;
		System.out.println(formatNumber(doubleNumberDivision));  // Output: 49.4k

		// Test for double - Exponentiation
		double doubleNumberExponentiation = Math.pow(2.0, 10.0);
		System.out.println(formatNumber(doubleNumberExponentiation));  // Output: 1.0k

		// Test for BigInteger - Addition
		BigInteger bigIntegerNumberAddition = new BigInteger("12345678901")
			.add(new BigInteger("98765432109"));
		System.out.println(formatNumber(bigIntegerNumberAddition));  // Output: 111.1b

		// Test for BigInteger - Subtraction
		BigInteger bigIntegerNumberSubtraction = new BigInteger("9876543210")
			.subtract(new BigInteger("1234567890"));
		System.out.println(formatNumber(bigIntegerNumberSubtraction));  // Output: 86.2b

		// Test for BigInteger - Multiplication
		BigInteger bigIntegerNumberMultiplication = new BigInteger("1234567890")
			.multiply(new BigInteger("2"));
		System.out.println(formatNumber(bigIntegerNumberMultiplication));  // Output: 24.7b

		// Test for BigInteger - Division
		BigInteger bigIntegerNumberDivision = new BigInteger("1234567890")
			.divide(new BigInteger("2"));
		System.out.println(formatNumber(bigIntegerNumberDivision));  // Output: 6.2b

		// Test for BigInteger - Exponentiation
		BigInteger bigIntegerNumberExponentiation = new BigInteger("2").pow(31);
		System.out.println(formatNumber(bigIntegerNumberExponentiation));  // Output: 2.1b
	}
}
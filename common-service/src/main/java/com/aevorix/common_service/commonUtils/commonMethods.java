package com.aevorix.common_service.commonUtils;

public class commonMethods {
//	public static void main(String[] args) {
//		System.out.println(genOtp(9));
//		
//	}

	public static int genOtp(int length) {
		if (length <= 0 || length > 9) {
			throw new IllegalArgumentException("OTP length must be between 1 and 9");
		}

		java.util.Random random = new java.util.Random();
		int otp;

		while (true) {
			int min = (int) Math.pow(10, length - 1);
			int max = (int) Math.pow(10, length) - 1;

			otp = random.nextInt((max - min) + 1) + min; // ensure length is correct

			String otpStr = String.valueOf(otp);
			char firstChar = otpStr.charAt(0);

			// Check if all digits are the same
			boolean allSame = otpStr.chars().allMatch(c -> c == firstChar);
			if (!allSame) {
				break;
			}
		}

		return otp;
	}

}

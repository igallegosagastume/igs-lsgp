package commons.test;

import java.security.SecureRandom;

public class TestSecureRandom {

	public static void main(String[] args) {
		Long exps = 100000000001L;
		SecureRandom rand = new SecureRandom();
		double sum = 0;
		for (long i=0; i<exps; i++) {
			Integer k = rand.nextInt(256);
			sum = sum + k;
			
			if (i%10000000==0)
				System.out.println(i);
		}
		
		double prom = sum/exps;
		System.out.println("Prom:"+prom);
	}
}

import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;

public class HashiraNoLib {
    public static void main(String[] args) {
        try {
            // Step 1: Read file as plain text
            String content = new String(Files.readAllBytes(Paths.get("input.json")));

            // Step 2: Extract n and k safely
            int n = Integer.parseInt(content.replaceAll("(?s).*\"n\"\\s*:\\s*(\\d+).*", "$1"));
            int k = Integer.parseInt(content.replaceAll("(?s).*\"k\"\\s*:\\s*(\\d+).*", "$1"));

            System.out.println("n = " + n + ", k = " + k);

            // Step 3: Extract roots
            List<BigInteger> roots = new ArrayList<>();
            for (int i = 1; i <= k; i++) {
                // find base
                String basePattern = "\""+i+"\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"?(\\d+)\"?";
                String baseStr = content.replaceAll("(?s).*" + basePattern + ".*", "$1");
                int base = Integer.parseInt(baseStr);

                // find value
                String valuePattern = "\""+i+"\"\\s*:\\s*\\{[^}]*\"value\"\\s*:\\s*\"?([0-9a-zA-Z]+)\"?";
                String valueStr = content.replaceAll("(?s).*" + valuePattern + ".*", "$1");

                BigInteger decoded = new BigInteger(valueStr, base);
                roots.add(decoded);
            }

            System.out.println("Decoded roots (first k): " + roots);

            // Step 4: Construct polynomial coefficients
            List<BigInteger> coeffs = new ArrayList<>();
            coeffs.add(BigInteger.ONE);

            for (BigInteger root : roots) {
                List<BigInteger> newCoeffs = new ArrayList<>();
                newCoeffs.add(coeffs.get(0).negate().multiply(root)); // constant

                for (int i = 1; i <= coeffs.size(); i++) {
                    BigInteger prev = (i < coeffs.size()) ? coeffs.get(i) : BigInteger.ZERO;
                    BigInteger val = coeffs.get(i - 1);
                    newCoeffs.add(val.add(prev.negate().multiply(root)));
                }

                newCoeffs.add(coeffs.get(coeffs.size() - 1)); // top term
                coeffs = newCoeffs;
            }
            BigInteger c = coeffs.get(0);  // constant term is at index 0
            System.out.println("c = " + c);
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


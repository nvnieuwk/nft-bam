package nvnieuwk.nf.test.bam;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<String> castToStringArray(Object input) {
        ArrayList<String> result = new ArrayList<String>();
        if (input instanceof ArrayList<?>) {
            ArrayList<?> inputArray = (ArrayList<?>)input;
            for (int i = 0; i < inputArray.size(); i++) {
                Object item = inputArray.get(i);
                if(item instanceof String) {
                    result.add((String)item);
                }
            }
        }
        return result;
    }
}

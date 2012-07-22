package netscape.javascript;

import java.io.*;

public class JSUtil {

	/* Return the stack trace of an exception or error as a String */
	public static String getStackTrace(Throwable t) {
		ByteArrayOutputStream captureStream;
		PrintWriter p;

		captureStream = new ByteArrayOutputStream();
		p = new PrintWriter(captureStream);

		t.printStackTrace(p);
		p.flush();

		return captureStream.toString();
	}

	/**
	 * This method is used to work around a bug in AIX JDK1.1.6, in which static
	 * initializers are not run when a static field is referenced from native
	 * code. The problem does not manifest itself if the field is accessed from
	 * Java code.
	 */
	private static void workAroundAIXJavaBug() {
		if (java.lang.Void.TYPE == null)
			System.err.println("JDK bug: "
					+ "java.lang.Void.TYPE uninitialized");
	}
}

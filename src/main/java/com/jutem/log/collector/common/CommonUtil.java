package com.jutem.log.collector.common;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class CommonUtil {
	/**
	 * 参考土豆同事的代码
	 */
	public static String ex2Str(Throwable throwable) {
		CharArrayWriter writer = new CharArrayWriter();
		throwable.printStackTrace(new PrintWriter(writer, true));
		return writer.toString();
	}
}

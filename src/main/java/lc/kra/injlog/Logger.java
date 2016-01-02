package lc.kra.injlog;

/**
 * Copyright (c) 2015 Kristian Kraljic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public interface Logger {
	public void log(String format, Object... arguments);
	public void log(String message, Throwable throwable);
	
	public static class ConsoleLogger implements Logger {
		@Override public void log(String format, Object... arguments) {
			System.out.format(format,arguments); }
		@Override public void log(String message, Throwable throwable) {
			System.err.println(message); throwable.printStackTrace(System.err); }
	}
	
	public static class FileLogger implements Logger {
		private static PrintWriter writer;
		
		public FileLogger(File file) throws FileNotFoundException {
			writer = new PrintWriter(file); }
		
		@Override public void log(String format, Object... arguments) {
			writer.format(format, arguments); writer.println(); }
		@Override public void log(String message, Throwable throwable) {
			writer.println(message); throwable.printStackTrace(writer); }
		
		public void close() { writer.close(); }
	}
}
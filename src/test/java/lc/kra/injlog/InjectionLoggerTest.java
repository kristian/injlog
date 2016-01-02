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

import static lc.kra.injlog.InjectionLogger.injectLogger;
import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

public class InjectionLoggerTest {
	@Test public void test() {
		final StringWriter writerA = new StringWriter();
		Logger loggerA = new Logger() {
			private PrintWriter writer = new PrintWriter(writerA);
			@Override public void log(String format, Object... arguments) {
				writer.format(format,arguments); writer.print('\n'); }
			@Override public void log(String message, Throwable throwable) {
				writer.print(message+'\n'); throwable.printStackTrace(writer); }
		};
		
		final StringWriter writerB = new StringWriter();
		Logger loggerB = new Logger() {
			private PrintWriter writer = new PrintWriter(writerB);
			@Override public void log(String format, Object... arguments) {
				writer.format(format,arguments); writer.print('\n'); }
			@Override public void log(String message, Throwable throwable) {
				writer.print(message+'\n'); throwable.printStackTrace(writer); }
		};
		
		Object objectA = new InjectionLogger.Injectable() {
			private final Logger logger = getLogger();
			
			@Override public boolean equals(Object object) {
				boolean equals = super.equals(object);
				logger.log("%s does %sequal %s",this,(equals?"":"not "),object);
				return equals;
			}
			
			@Override public String toString() { return "ObjectA"; }
		};
		
		Object objectB = new InjectionLogger.Injectable() {
			private final Logger logger = getLogger();
			
			@Override public boolean equals(Object object) {
				boolean equals = super.equals(object);
				logger.log("%s does %sequal %s",this,(equals?"":"not "),object);
				return equals;
			}
			
			@Override public String toString() { return "ObjectB"; }
		};
		
		objectA.equals(objectA); objectA.equals(objectB);
		objectB.equals(objectA); objectB.equals(objectB);
		assertEquals("", writerA.toString());
		assertEquals("", writerB.toString());
		
		injectLogger(loggerA, objectA);
		objectA.equals(objectA); objectA.equals(objectB);
		objectB.equals(objectA); objectB.equals(objectB);
		assertEquals("ObjectA does equal ObjectA\nObjectA does not equal ObjectB\n", writerA.toString());
		assertEquals("", writerB.toString());
		
		injectLogger(loggerB, objectB);
		objectA.equals(objectA); objectA.equals(objectB);
		objectB.equals(objectA); objectB.equals(objectB);
		assertEquals("ObjectA does equal ObjectA\nObjectA does not equal ObjectB\nObjectA does equal ObjectA\nObjectA does not equal ObjectB\n", writerA.toString());
		assertEquals("ObjectB does not equal ObjectA\nObjectB does equal ObjectB\n", writerB.toString());
		
		injectLogger(loggerB, objectA);
		objectA.equals(objectA); objectA.equals(objectB);
		objectB.equals(objectA); objectB.equals(objectB);
		assertEquals("ObjectA does equal ObjectA\nObjectA does not equal ObjectB\nObjectA does equal ObjectA\nObjectA does not equal ObjectB\n", writerA.toString());
		assertEquals("ObjectB does not equal ObjectA\nObjectB does equal ObjectB\nObjectA does equal ObjectA\nObjectA does not equal ObjectB\nObjectB does not equal ObjectA\nObjectB does equal ObjectB\n", writerB.toString());
	}
}

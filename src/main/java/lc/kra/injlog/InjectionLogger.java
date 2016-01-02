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

import java.util.Map;
import java.util.WeakHashMap;

public final class InjectionLogger implements Logger {
	private final static Map<Object,Logger> injections = new WeakHashMap<Object,Logger>();
	
	private Logger logger;
	
	public void injectLogger(Logger logger) { this.logger = logger; }
	
	public static <T> T injectLogger(Logger logger, T object) {
		if(logger!=null&&object instanceof InjectionLogger.Injectable)
			((InjectionLogger)((InjectionLogger.Injectable)object).getLogger()).injectLogger(logger);
		return object;
	}
	
	@Override public final void log(String format,Object... arguments) {
		if(this.logger!=null) logger.log(format,arguments);
	}
	@Override public void log(String message,Throwable throwable) {
		if(this.logger!=null) logger.log(message,throwable);
	}
	
	public interface Injectable {
		public default Logger getLogger() {
			Logger logger = injections.getOrDefault(this,new InjectionLogger());
			injections.put(this,logger);
			return logger;
		}
	}
}
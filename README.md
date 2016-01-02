InjLog for Java 8
=================

This project provides a very lightweight injection logger class for Java 8. Any class about to use logging functionality may implement the `InjectionLogger.Injectable` interface. Objects of the class can call `getLogger` to receive a logger instance. The `InjectionLogger.Injectable` interface implements it by default.

Loggers implement a very simple `Logger` interface, providing two methods `log(String format, Object... arguments)` and `log(String message, Throwable throwable)`. Loggers can be injected into objects by calling `InjectionLogger.injectLogger` at any point in time. Any object may have a different logger injected.

There are two default logger implementations provided by the `Logger` class. `ConsoleLogger` logging to the `System.out` and `System.err` print writers and`FileLogger` logging to any `File`. A simple example how to use `InjectionLogger` in action:

```java
import static lc.kra.injlog.InjectionLogger.injectLogger;

public class AnyClass implements InjectionLogger.Injectable {	
	public void anyMethod() {
		final Logger logger = getLogger();
		logger.log("Any method was called.");
	}
	
	public static void main(String[] args) {
		Logger anyLogger = new Logger.ConsoleLogger();
			//new Logger.FileLogger(new File("anyFile.txt"));
		
		AnyClass anyObject = new AnyClass();
		injectLogger(anyLogger, anyObject);
		anyObject.anyMethod();
		
		AnyClass anyOtherObject = injectLogger(anyLogger, new AnyClass());
		anyOtherObject.anyMethod();
	}
}
```

Please find a little background information about the framework [on my blog](http://kra.lc/blog/2016/01/injlog-injection-logger-for-java-8/).

Usage
-----

Feel free to use the class `lc.kra.injlog.InjectionLogger` and the interface `lc.kra.injlog.Logger` by coping the `.java` files to your project.

### Maven Dependency
Alternatively you can include injlog from this GitHub repository by adding this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>lc.kra.injlog</groupId>
  <artifactId>injlog-core</artifactId>
  <version>0.1.0</version>
</dependency>
```

Additionally you will have to add the following repository to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>injlog-mvn-repo</id>
    <url>https://raw.github.com/kristian/injlog/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
```

Build
-----

To build injlog on your machine, checkout the repository, `cd` into it, and call:
```
mvn clean install
```

License
-------

The code is available under the terms of the [MIT License](http://opensource.org/licenses/MIT).
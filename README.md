jerry
=====

`jerry` framework provides much of the common functionality that is used in a standard Java application like checking for application updates, providing template classes for CRUD operations with MongoDB, working with Quartz scheduler, and utility classes to work with HTML, cookies, JSON, XML, reflection, URIs, web and much more.

Features
--------
* Collection of utility classes
* Powerful HTTP framework for handling web requests
* OAuth client and server side filters
* MongoDB abstract class for writing CRUD operations on objects with single line of code
* Quartz scheduling framework
* Basic security framework classes
* Basic batching framework
* Base62 encoder for 64-bit integers

Builds
------

**Development Version**

* Added `com.sangupta.jerry.oauth` package to work with OAuth requests. 
* Utility client to send OAuth signed requests
* Server side filters to check validity of incoming requests per OAuth specifications
* Added ArchiveUtils - utility class to work with compressed files
* Added HttpServletResponseWrapper implementation that uses a ByteArray to buffer response
* Updated CookieUtils to add method to create a new Cookie
* Fixed bug in JavascriptTag to not include same Javascript URL twice

* Some minor refactorings and bug fixes
* Some libraries are back to `required` scope

**0.4.1**

* Added utility methods to Responseutils
* Fixed a bug in RequestCapturingFilter where chained filters were not being called
* Upgraded version on Apache Http-client library to 4.2.5
* Fixed bug where Cookie age was not being set correctly
* Added very fast Base64 encoder from another open-source project
* Fixed bug where not all data was being sent to HttpServletResponse due to encoding issues
* Added init() method to JerseyGrizzlyServer
* Some other smaller fixes

* All libraries were set to `provided` scope

**0.4.0**

* Added a Jersey-Grizzly based un/blocking server that can be used to run services from given packages
* Added `UnsafeMemory` class and corresponding interface to allow for serialization using `unsafe` direct memory for performance reasons
* Added a `WorkerGroup` convenience class to launch multiple worker threads via a given class/instance/runnable factory
* Added an interface `HttpStatusCode` that defines various HTTP status codes
* Added method to `EmailAddress` class to parse multiple email addresses from a given string supporting the format `<full name> email@domain.com`
* Added many constants to `DateUtils` class


Downloads
---------

The library can be downloaded from Maven Central using:

```xml
<dependency>
    <groupId>com.sangupta</groupId>
    <artifactId>jerry</artifactId>
    <version>0.3.0</version>
</dependency>
```

Dependencies
------------

`jerry` depends on the following frameworks directly

* Apache HTTP client for web access
* Apache Commons IO for disk I/O
* Jericho HTML parser to work with HTML

Besides these, `jerry` also uses the following libraries (as in Maven `provided` scope) to provide utility functions and extension classes to remove boiler-plate code.

* Hibernate framework
* Spring ORM framework
* Java MongoDB driver
* Spring Data MongoDB
* Google GSON library
* XStream XML parsing library

The difference in `direct` and `provided` dependency stems from the fact on usage. For example, you will only use `HibernateUtils` class when using Hibernate in your project, and thus we don't declare that as a direct dependency. This helps us keep the size of the `jerry` library small.

Continuous Integration
----------------------
The library is continuously integrated and unit tested using the *Travis CI system.

Current status of branch `MASTER`: [![Build Status](https://secure.travis-ci.org/sangupta/jerry.png?branch=master)](http://travis-ci.org/sangupta/jerry)

The library is tested against

* Oracle JDK 7
* Oracle JDK 6
* Open JDK 7

Note: Build are not tested against `Open JDK 6` because of a compilation failure in code on generics. It does not seem to be a code issue as it passes on `Oracle JDK 6`. Will fix the error once I get to know more on the cause and its remedy.

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`jerry` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------
	
Copyright (c) 2012, Sandeep Gupta

The project uses various other libraries that are subject to their
own license terms. See the distribution libraries or the project
documentation for more details.

The entire source is licensed under the Apache License, Version 2.0 
(the "License"); you may not use this work except in compliance with
the LICENSE. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

jerry
=====

`jerry` framework provides much of the common functionality that is used in a standard Java application like checking for application updates, providing template classes for CRUD operations with MongoDB, working with Quartz scheduler, and utility classes to work with HTML, cookies, JSON, XML, reflection, URIs, web and much more.

Features
--------
* Collection of utility classes
* Powerful HTTP framework for handling web requests
* MongoDB abstract class for writing CRUD operations on objects with single line of code
* Quartz scheduling framework
* Basic security framework classes

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

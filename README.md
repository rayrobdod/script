# Script
[![Build Status](https://travis-ci.org/rayrobdod/script.svg)](https://travis-ci.org/rayrobdod/script)

A library for reading scripts from XML and using those scripts

## Build Instructions
This repository uses [sbt](http://www.scala-sbt.org/) as its build tool.


##Usage
Basically, you write things by xml by hand, then feed that xml into a
ScriptFromXml instance and it outputs a ScriptElement instance. That
ScriptElement instance can then be put into a view, and it will display
the contents of the ScriptElement to a user.


The data model is an aggrigate, where ScriptElement is the interface and Group
is the aggrigate. You will need a switch statement to determine which script
element is being read.

The ScriptFromXml is also extensible via aggrigation - there is a
BaseScriptFromXml class which will construct elements included in this library,
but there is also an AggrigateScriptFromXml, which will take multiple
ScriptFromXmls, potentially including a ScriptFromXml written by a user of this
library, and invoke the `apply` method of the one that knows how to construct a
ScriptElement corresponding to that element.



As of right now, the only built-in view is a console view. It is similar to
ScriptFromXml in that it is extensible via aggrigation.


Included is a small text-based adventure, which is a sample usage of this
library that shows most points of this library, including the extension points.


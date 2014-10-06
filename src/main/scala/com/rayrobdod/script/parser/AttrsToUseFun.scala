package com.rayrobdod.script;
package parser

import com.codecommit.antixml.{Attributes => XmlAttrs}
	
/**
 * Determines, based on an Xml Element's attributes, a function
 * that determines whether, based on a program's state, whether a
 * script element is used or not.
 */
trait AttrsToUseFun[State] {
	def apply(attrs:XmlAttrs):Function1[State,Boolean]
}

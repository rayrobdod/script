package com.rayrobdod.script;
package parser

import scala.runtime.{AbstractFunction1 => AFunction1}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import java.net.URL

/**
 * Constructs script elements from xml
 */
trait ScriptFromXml {
	
	/**
	 * Constructs base script elements from xml
	 * @param useFun Determines, based on an Xml Element's attributes,
	 *			a function that determines whether, based on a program's
	 * 			state, whether a script element is used or not.
	 * @param xml the element to convert
	 * @param base the base url used when constructing links
	 * @param recurser 
	 */
	def apply[A](
			useFun:AttrsToUseFun[A],
			xml:Elem,
			base:URL,
			recurser:ScriptFromXml
	):ScriptElement[A]
	
	/**
	 * Returns true iff apply will not throw an error if presented with
	 * this Xml Element
	 */
	def isDefinedAt(xml:Elem):Boolean
}

/**
 * Applies function parameters to the first child which isDefinedAt
 * returns true.
 */
class AggregateScriptFromXml(childs:ScriptFromXml*) extends ScriptFromXml {
	
	def apply[A](useFun:AttrsToUseFun[A], xml:Elem, base:URL, recurser:ScriptFromXml):ScriptElement[A] = {
		childs.find{
			_.isDefinedAt(xml)
		 }.map{
		 	_.apply(useFun, xml, base, recurser)
		 }.getOrElse{
		 	throw new IllegalArgumentException("Unknown xml element")
		 }
	}
	
	override def isDefinedAt(xml:Elem):Boolean = {
		childs.find{_.isDefinedAt(xml)}.isDefined
	}
	
}

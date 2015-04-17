/*
	Copyright (c) 2015, Raymond Dodge
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
		* Redistributions of source code must retain the above copyright
		  notice, this list of conditions and the following disclaimer.
		* Redistributions in binary form must reproduce the above copyright
		  notice, this list of conditions and the following disclaimer in the
		  documentation and/or other materials provided with the distribution.
		* Neither the name "<PRODUCT NAME>" nor the names of its contributors
		  may be used to endorse or promote products derived from this software
		  without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
	 * @param recurser the ScriptFromXml that will handle recursive calls
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
 * A [[ScriptFromXml]] which, when instructed to convert an xml Elem,
 * will forward the instruction to the first of its child ScriptFromXmls
 * which is capable of handling the ScriptElement
 * 
 * @constructor
 * @param childs the seq of ScriptFromXml which this will delegate to
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
		childs.exists{_.isDefinedAt(xml)}
	}
	
}

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
package com.rayrobdod.scriptSample

import java.net.URL
import scala.runtime.{AbstractFunction1 => AFunction1}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML
}
import com.rayrobdod.script.ScriptElement
import com.rayrobdod.script.parser.{AttrsToUseFun => BaseAttrsToUseFun, ScriptFromXml}

/**
 * Constructs base script elements from xml
 */
object SampleScriptFromXml extends ScriptFromXml {
	
	def apply[A](useFun:BaseAttrsToUseFun[A], xml:Elem, base:URL, recurser:ScriptFromXml):ScriptElement[A] = xml match {
		case Elem(_, "setName", attrs, _, _) => {
			
			SetName
		}
		case Elem(_, "setGender", attrs, _, _) => {
			
			SetGender
		}
		case Elem(_, name, _, _, _) => {
			
			throw new IllegalArgumentException("Unexpected element: " + name)
		}
		case _ => {
			
			throw new IllegalArgumentException("Not an element ")
		}
	}
	
	/**
	 * Returns true iff apply will not throw an error if presented with
	 * this Xml Element
	 */
	def isDefinedAt(xml:Elem):Boolean = xml match {
		case Elem(_, name, _, _, _) => name match {
			case "setName" => true
			case "setGender" => true
			case _ => false
		}
		case _ => false
	}
	
	
}

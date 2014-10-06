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

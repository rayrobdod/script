package com.rayrobdod.scriptSample

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

import com.rayrobdod.script.ScriptElement
import com.rayrobdod.script.parser.{AttrsToUseFun => BaseAttrsToUseFun, ScriptFromXml}


class SampleScriptFromXmlTest extends FunSpec {
	object AlwaysUse extends BaseAttrsToUseFun[Any] {
		def apply(attrs:XmlAttrs) = {x:Any => true}
	}
	val baseUrl = new java.net.URL("http", "localhost", "DoNotCare")
	
	
	describe ("Basic XML -> Object serialization") {
		it ("<setName />") {
			val input:Elem = XML.fromString("<setName />")
			val expected = SetName
			
			assertResult(true){SampleScriptFromXml.isDefinedAt(input)}
			assertResult(expected){SampleScriptFromXml(AlwaysUse, input, baseUrl, SampleScriptFromXml)}
		}
		it ("<setGender />") {
			val input:Elem = XML.fromString("<setGender />")
			val expected = SetGender
			
			assertResult(true){SampleScriptFromXml.isDefinedAt(input)}
			assertResult(expected){SampleScriptFromXml(AlwaysUse, input, baseUrl, SampleScriptFromXml)}
		}
		it ("<thisElementDoesNotExist />") {
			val input:Elem = XML.fromString("<thisElementDoesNotExist />")
			
			assertResult(false){SampleScriptFromXml.isDefinedAt(input)} ;
			intercept[IllegalArgumentException]
			{
				SampleScriptFromXml(AlwaysUse, input, baseUrl, SampleScriptFromXml)
			}
		}
	}
	
	
}

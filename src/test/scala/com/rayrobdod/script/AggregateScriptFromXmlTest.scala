package com.rayrobdod.script
package parser

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

import parser.{AttrsToUseFun => BaseAttrsToUseFun}
import com.rayrobdod.scriptSample.{SampleScriptFromXml, SetName, SetGender}


class AggregateScriptFromXmlTest extends FunSpec {
	object AlwaysUse extends BaseAttrsToUseFun[Any] {
		def apply(attrs:XmlAttrs) = constTrue
	}
	val baseUrl = new java.net.URL("http", "localhost", "DoNotCare")
	val dut = new AggregateScriptFromXml(
			BaseScriptFromXml,
			SampleScriptFromXml
	)
	
	
	describe ("Basic XML -> Object serialization") {
		
		it ("<group><setName /><setGender /></group>") {
			val input:Elem = XML.fromString("<group><setName /><setGender /></group>")
			val expected = Group(Seq(SetName, SetGender))
			
			assertResult(true){dut.isDefinedAt(input)}
			assertResult(expected){dut(AlwaysUse, input, baseUrl, dut)}
		}
	}
}

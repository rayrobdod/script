package com.rayrobdod.script
package parser

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

import BaseScriptFromXml.{AttrsToUseFun => BaseAttrsToUseFun}


class BaseScriptFromXmlTest  extends FunSpec {
	val constFalse = {(a:Any) => false}
	object AlwaysUse extends BaseAttrsToUseFun[Any] {
		def apply(attrs:XmlAttrs) = constTrue
	}
	object IgnoreAttrUse extends BaseAttrsToUseFun[Any] {
		def apply(attrs:XmlAttrs) = if (attrs.contains("ignore")) {constFalse} else {constTrue}
	}
	val baseUrl = new java.net.URL("http", "localhost", "DoNotCare")
	
	
	describe ("Basic XML -> Object serialization") {
		it ("<speak speaker='123'>456</speak>") {
			val input:Elem = XML.fromString("<speak speaker='123'>456</speak>")
			val expected = Speak("123", "", "456")
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<setFlag flag='NAME' value='23' />") {
			val input:Elem = XML.fromString("<setFlag flag='NAME' value='42' />")
			val expected = SetFlag("NAME", 42)
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<yesNo flag='NAME' />") {
			val input:Elem = XML.fromString("<yesNo flag='NAME' />")
			val expected = YesNo("NAME")
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<noOp />") {
			val input:Elem = XML.fromString("<noOp />")
			val expected = NoOp
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<group></group>") {
			val input:Elem = XML.fromString("<group></group>")
			val expected = Group(Seq())
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<group><noOp /><noOp /><noOp /></group>") {
			val input:Elem = XML.fromString("<group><noOp /><noOp /><noOp /></group>")
			val expected = Group(Seq(NoOp, NoOp, NoOp))
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<options>...</options>") {
			val input:Elem = XML.fromString("""<options>
				<noOp optionName="Frist" />
				<noOp optionName="Sceond" />
				<noOp optionName="Thrid" />
			</options>""")
			val expected = Options(
				Seq("Frist", "Sceond", "Thrid"),
				Seq(NoOp, NoOp, NoOp)
			)
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(expected){BaseScriptFromXml(AlwaysUse, input, baseUrl)}
		}
		it ("<goto href='BaseScriptFromXmlTest_1.xml' />") {
			val baseUrl = new java.net.URL("file:///C:/Users/Raymond/Documents/Programming/Java/Games/Script/src/test/resources/baseFile")
			val input:Elem = XML.fromString("<goto href='BaseScriptFromXmlTest_1.xml' />")
			val expectedLayer2 = Speak("123", "", "456")
			
			assertResult(true){BaseScriptFromXml.isDefinedAt(input)}
			assertResult(classOf[GoTo[_]]){BaseScriptFromXml(AlwaysUse, input, baseUrl).getClass}
			assertResult(expectedLayer2){
				BaseScriptFromXml(AlwaysUse, input, baseUrl) match {
					case GoTo(href, _) => {
						href.apply
					}
					case _ => null
				}
			}
		}
		it ("<thisElementDoesNotExist />") {
			val input:Elem = XML.fromString("<thisElementDoesNotExist />")
			
			assertResult(false){BaseScriptFromXml.isDefinedAt(input)} ;
			intercept[IllegalArgumentException]
			{
				BaseScriptFromXml(AlwaysUse, input, baseUrl)
			}
		}
	}
	
	
	describe ("Alternate AttrsToUseFun function") {
		it ("<group></group>") {
			val input:Elem = XML.fromString("<group></group>")
			val expected = Group(Seq(), constTrue)
			
			assertResult(expected){BaseScriptFromXml(IgnoreAttrUse, input, baseUrl)}
		}
		it ("<group ignore='ignore'></group>") {
			val input:Elem = XML.fromString("<group ignore='ignore'></group>")
			val expected = Group(Seq(), constFalse)
			
			assertResult(expected){BaseScriptFromXml(IgnoreAttrUse, input, baseUrl)}
		}
	}
}

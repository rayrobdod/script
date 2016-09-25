package com.rayrobdod.script
package consoleView

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import com.codecommit.antixml.{Elem, Selector, Text,
		QName, Node, Attributes => XmlAttrs, XML}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

import java.io.{Reader, Writer}
import parser.{AttrsToUseFun => BaseAttrsToUseFun}
import com.rayrobdod.scriptSample.{State, SampleScriptPrinter, SetName, SetGender}


class AggregateScriptPrinterTest  extends FunSpec {
	
	
	def newSP = {
		new AggregateScriptPrinter[State](
			new BaseScriptPrinter[State](State.SetFlag),
			SampleScriptPrinter
		)
	}
	def getPrintedMessage(s:State, e:ScriptElement[State], input:String = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r"):String = {
		val writer = new StringWriter
		val reader = new StringReader(input)
		val sp = newSP
		
		val outState = sp.apply(writer, reader, sp, s, e)
		
		writer.toString
	}
	
	
	
	describe ("Group(Speak,SetGender) reponse") {
		val script = Group(Seq(
			Speak("A","","aaaaa"),
			SetGender
		))
		
		it ("prints the message contained in the script") {
			assertResult(
				"\tA:\naaaaa\n\t(B/G): "
			){
				getPrintedMessage(State.empty, script, "\r\r\r\r g \r\r\r\r")
			}
		}
		it ("changes current state") {
			assertResult(
				State("", Map("female" → 1))
			){
				newSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r g \r\r\r\r"),
						newSP, State.empty, script
				)
			}
		}
	}
	describe ("Unown reponse") {
		val script = new ScriptElement[State] {
			def use(s:State):Boolean = true
		}
		
		it ("is not defined at") {
			assert(! newSP.isDefinedAt(script))
		}
		it ("errors on apply") {
			intercept[IllegalArgumentException] {
				getPrintedMessage(State.empty, script, "\r\r\r\r g \r\r\r\r")
			}
		}
	}
}

package com.rayrobdod.scriptSample

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

import com.rayrobdod.script.ScriptElement

class SampleScriptPrinterTest  extends FunSpec {
	
	def getPrintedMessage(s:State, e:ScriptElement[State], input:String = "\r\r\r\r\r\r\r\r\r\r\r"):String = {
		val writer = new StringWriter
		val reader = new StringReader(input)
		
		val outState = SampleScriptPrinter.apply(writer, reader, SampleScriptPrinter, s,e)
		
		writer.toString
	}
	
	
	describe ("SetGender reponse") {
		val script = SetGender 
		
		it ("isDefinedAt => true") {
			assertResult(true){SampleScriptPrinter.isDefinedAt(script)}
		}
		it ("prints prompt to console") {
			assertResult("\t(B/G): "){getPrintedMessage(State.empty, script, "\r b \r")}
		}
		it ("sets flag to '1' if input is 'g'") {
			assertResult(
				State("", Map("female" → 1))
			){
				SampleScriptPrinter.apply(
						new StringWriter,
						new StringReader("\r g \r"),
						SampleScriptPrinter,
						State.empty, script
				)
			}
		}
		it ("sets flag to '0' if input is 'B'") {
			assertResult(
				State("", Map("female" → 0))
			){
				SampleScriptPrinter.apply(
						new StringWriter,
						new StringReader("\r B \r"),
						SampleScriptPrinter,
						State.empty, script
				)
			}
		}
	}
	describe ("SetName reponse") {
		val script = SetName
		
		it ("isDefinedAt => true") {
			assertResult(true){SampleScriptPrinter.isDefinedAt(script)}
		}
		it ("Prints prompt") {
			assertResult(
				"\tEnter name:\n> "
			){
				getPrintedMessage(State.empty, script, "ARGLEBARGLE\r\r\r\r\r\r")
			}
		}
		it ("changes the state to update the name") {
			assertResult(
				State("ARGLEBARGLE", Map.empty)
			){
				SampleScriptPrinter.apply(
						new StringWriter,
						new StringReader("ARGLEBARGLE\r\r\r\r\r\r\r\r"),
						SampleScriptPrinter,
						State.empty, script
				)
			}
		}
	}
}

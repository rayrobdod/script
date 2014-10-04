package com.rayrobdod.script
package consoleView

import java.io.{StringWriter, StringReader}
import scala.collection.immutable.{Seq, Set, Map}
import org.scalatest.{FunSuite, FunSpec}
import org.scalatest.prop.PropertyChecks

class BaseScriptPrinterTest  extends FunSpec {
	type State = Map[String, Int]
	val SetState = {(s:State, name:String, value:Int) => {
		s + ((name, value))
	}}
	
	def getPrintedMessage(s:State, e:ScriptElement[State], input:String = "\r\r\r\r\r\r\r\r\r\r\r"):String = {
		val writer = new StringWriter
		val reader = new StringReader(input)
		val bsp = new BaseScriptPrinter(writer, reader, SetState)
		
		val outState = bsp.apply(s,e)
		
		writer.toString
	}
	def newBSP:BaseScriptPrinter[State] = newBSP("\r\r\r\r\r\r\r\r\r\r\r")
	def newBSP(in:String):BaseScriptPrinter[State] = {
		new BaseScriptPrinter[State](new StringWriter, new StringReader(in), SetState)
	}
	
	
	describe ("Speak reponse") {
		val script = Speak("Hamlet", "", "To be or not to be") 
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints the message contained in the script") {
			assertResult(
				"\tHamlet:\nTo be or not to be\n"
			){
				getPrintedMessage(Map.empty, script)
			}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){newBSP.apply(Map.empty, script)}
		}
	}
	describe ("SetFlag reponse") {
		val script = SetFlag("KingExists", 1) 
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("does not print to console") {
			assertResult(""){getPrintedMessage(Map.empty, script)}
		}
		it ("updates current state by creating new Flag") {
			assertResult(
				Map(script.flag → script.setTo)
			){
				newBSP.apply(Map.empty, script)
			}
		}
		it ("updates current state by overriting flags") {
			assertResult(
				Map(script.flag → script.setTo)
			){
				newBSP.apply(Map(script.flag → -54), script)
			}
		}
	}
	describe ("YesNo reponse") {
		val script = YesNo("ToBeOrNotToBe") 
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints prompt to console") {
			assertResult("\t(y/n): \n"){getPrintedMessage(Map.empty, script, "\r y \r")}
		}
		it ("sets flag to '1' if input is 'y'") {
			assertResult(
				Map(script.flag → 1)
			){
				newBSP("\r y \r").apply(Map.empty, script)
			}
		}
		it ("sets flag to '0' if input is 'N'") {
			assertResult(
				Map(script.flag → 0)
			){
				newBSP("\r N \r").apply(Map.empty, script)
			}
		}
	}
	describe ("NoOp reponse") {
		val script = NoOp
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("doesn't print anything") {
			assertResult(""){getPrintedMessage(Map.empty, script)}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){newBSP.apply(Map.empty, script)}
		}
	}
	describe ("Group reponse") {
		val script = Group(Seq(
			Speak("A","","aaaaa"),
			Speak("B","","bbbbb")
		))
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints the message contained in the script") {
			assertResult(
				"\tA:\naaaaa\n\tB:\nbbbbb\n"
			){
				getPrintedMessage(Map.empty, script)
			}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){newBSP.apply(Map.empty, script)}
		}
	}
	describe ("use") {
		val script = Speak("KT", "", "ARGLEBARGLE",
			{(s:State) => s.get("Do").getOrElse(0) != 0}
		)
		
		it ("ignore if 'Do' not set") {
			assertResult(""){getPrintedMessage(Map.empty, script)}
		}
		it ("use if 'Do' is set") {
			assertResult(
				"\tKT:\nARGLEBARGLE\n"
			){
				getPrintedMessage(Map("Do" → 32), script)
			}
		}
	}
	
	
	
	describe ("Bigger picture") {
		val script = Group(Seq(
			Speak("Door","","Speak, Friend, and enter."),
			Speak("Narrator","","Do you speak?"),
			YesNo("SpokeFriend"),
			Group(Seq(
				Speak("You","","Friend"),
				Speak("Narrator","","The door opened.")
			), {(s:State) => s("SpokeFriend") == 1}),
			Speak("Narrator","","Nothing happened.",
				{(s:State) => s("SpokeFriend") == 0})
		))
		
		
		it ("if input is 'y', ") {
			assertResult(
"""	Door:
Speak, Friend, and enter.
	Narrator:
Do you speak?
	(y/n): 
	You:
Friend
	Narrator:
The door opened.
"""
			){
				getPrintedMessage(Map.empty, script, "\r\r\r\r\r\r\r\r\r y \r\r\r\r\r\r\r\r\r")
			}
		}
		it ("if input is 'n', ") {
			assertResult(
"""	Door:
Speak, Friend, and enter.
	Narrator:
Do you speak?
	(y/n): 
	Narrator:
Nothing happened.
"""
			){
				getPrintedMessage(Map.empty, script, "\r\r\r\r\r\r\r\r\r n \r\r\r\r\r\r\r\r\r")
			}
		}
		
	}
}

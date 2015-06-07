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
	
	def getPrintedMessage(s:State, e:ScriptElement[State], input:String = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r"):String = {
		val writer = new StringWriter
		val reader = new StringReader(input)
		val bsp = new BaseScriptPrinter(SetState)
		
		val outState = bsp.apply(writer, reader, bsp, s, e)
		
		writer.toString
	}
	def newBSP:BaseScriptPrinter[State] = new BaseScriptPrinter[State](SetState)
	
	
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
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty,
						script
				)
			}
		}
	}
	describe ("Long speak without spaces") {
		val script = Speak("Hamlet", "", "abcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxy") 
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints the message contained in the script") {
			assertResult(
				"\tHamlet:\nabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcdefghijklmnopqrstuvwxyabcde\nfghijklmnopqrstuvwxy\n"
			){
				getPrintedMessage(Map.empty, script)
			}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty,
						script
				)
			}
		}
	}
	describe ("Long speak with spaces") {
		val script = Speak("Hamlet", "", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
		
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints the message contained in the script") {
			assertResult(
				"\tHamlet:\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor\nincididunt ut labore et dolore magna aliqua.\n"
			){
				getPrintedMessage(Map.empty, script)
			}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty,
						script
				)
			}
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
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
		}
		it ("updates current state by overriting flags") {
			assertResult(
				Map(script.flag → script.setTo)
			){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map(script.flag → -54), script
				)
			}
		}
	}
	describe ("YesNo reponse") {
		val script = YesNo("ToBeOrNotToBe") 
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("prints prompt to console") {
			assertResult("\t(y/n): "){getPrintedMessage(Map.empty, script, "\r\r\r\r y \r\r\r\r")}
		}
		it ("sets flag to '1' if input is 'y'") {
			assertResult(
				Map(script.flag → 1)
			){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r y \r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
		}
		it ("sets flag to '0' if input is 'N'") {
			assertResult(
				Map(script.flag → 0)
			){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r N \r\r\r\r"),
						newBSP,
						Map.empty, script
				)
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
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
		}
	}
	describe ("Goto reponse") {
		val script = GoTo(() => Speak("Hamlet", "", "To be or not to be")) 
		
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
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
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
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
		}
	}
	describe ("Options reponse") {
		val script = Options(
			Seq(
				"A?" -> Speak("A","","aaaaa"),
				"B?" -> Speak("B","","bbbbb")
			)
		)
		
		it ("isDefinedAt => true") {
			assertResult(true){newBSP.isDefinedAt(script)}
		}
		it ("Prints option one for for option 1") {
			assertResult(
				"\tChoose one:\n0: A?\n1: B?\n> \tA:\naaaaa\n"
			){
				getPrintedMessage(Map.empty, script, "0 \r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r")
			}
		}
		it ("Prints option two for for option 2") {
			assertResult(
				"\tChoose one:\n0: A?\n1: B?\n> \tB:\nbbbbb\n"
			){
				getPrintedMessage(Map.empty, script, "1 \r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r")
			}
		}
		it ("doesn't change the current state") {
			assertResult(Map.empty){
				newBSP.apply(
						new StringWriter,
						new StringReader("1 \r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r"),
						newBSP,
						Map.empty, script
				)
			}
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
	
	
	
	describe ("Group passes state from one element to next") {
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
	(y/n): 	You:
Friend
	Narrator:
The door opened.
""".replace("\r\n", "\n")
			){
				getPrintedMessage(Map.empty, script, "\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r y \r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r")
			}
		}
		it ("if input is 'n', ") {
			assertResult(
"""	Door:
Speak, Friend, and enter.
	Narrator:
Do you speak?
	(y/n): 	Narrator:
Nothing happened.
""".replace("\r\n", "\n")
			){
				getPrintedMessage(Map.empty, script, "\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r n \r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r\r")
			}
		}
		
	}
}

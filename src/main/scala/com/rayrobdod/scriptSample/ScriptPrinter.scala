package com.rayrobdod.scriptSample

import java.io.{Reader, Writer}
import scala.collection.immutable.{Seq, Set, Map}

import com.rayrobdod.script.consoleView.ScriptPrinter
import com.rayrobdod.script.ScriptElement


/**
 * A [[com.rayrobdod.script.consoleView.ScriptPrinter ScriptPrinter]] that can handle the ScriptElements in
 * the [[com.rayrobdod.scriptSample]] package.
 */
object SampleScriptPrinter extends ScriptPrinter[State] {
	
	def apply(
			out:Writer,
			in:Reader,
			recurser:ScriptPrinter[State],
			s:State,
			e:ScriptElement[State]
	):State = {
		if (e.use(s)) {	e match {
			case SetGender => {
				out.write("\t(B/G): ")
				out.flush()
				var c:Char = 0;
				do {
					c = in.read().toChar.toLower
				} while (c != 'b' && c != 'g')
				
				State.SetFlag(s, "female", if (c == 'g') {1} else {0})
			}
			case SetName => {
				out.write("\tEnter name:\n> ")
				out.flush()
				
				val newName = this.readLine(in)
				
				s.copy(name = newName)
			}
			case _ => s	
		}} else {s}
	}
	
	def isDefinedAt(e:ScriptElement[State]):Boolean = {
		e match {
			case SetName => true
			case SetGender => true
			case _ => false
		}
	}
	
	
	
	
	
	private def readLine(in:Reader):String = {
		var s:String = ""
		var c:Char = '0'
		while (s.isEmpty || (c != '\n' && c != '\r')) {
			c = in.read.toChar
			if (c != '\n' && c != '\r') {s = s + c}
		}
		s
	}
}

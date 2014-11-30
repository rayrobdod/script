package com.rayrobdod.script
package consoleView

import java.io.{Reader, Writer}
import scala.collection.immutable.{Seq, Set, Map}

/**
 * A console-based user interface for ScriptElements.
 */
trait ScriptPrinter[State] {
	
	/**
	 * Handles the specified script element
	 * @param out where output gets writen
	 * @param in where input gets read from
	 * @param recurser the ScriptPrinter which handles recursive apply calls
	 * @param s state before the action gets performed
	 * @param e the action performed
	 * @return after the action gets performed
	 */
	def apply(
			out:Writer,
			in:Reader,
			recurser:ScriptPrinter[State],
			s:State,
			e:ScriptElement[State]
	):State
	
	
	/** 
	 * Returns true if apply can handle the specified ScriptElement 
	 */
	def isDefinedAt(e:ScriptElement[State]):Boolean
}

/**
 * A [[ScriptPrinter]] which, when instructed to handle a ScriptElement,
 * will forward the instruction to the first of its child ScriptPrinters
 * which is capable of handling the ScriptElement
 * 
 * @constructor
 * @param childs the seq of ScriptPrinters which this will delegate to
 */
class AggregateScriptPrinter[State](childs:ScriptPrinter[State]*) extends ScriptPrinter[State] {
	
	def apply(
			out:Writer,
			in:Reader,
			recurser:ScriptPrinter[State],
			s:State,
			e:ScriptElement[State]
	):State = {
		childs.find{
			_.isDefinedAt(e)
		 }.map{
		 	_.apply(out, in, recurser, s, e)
		 }.getOrElse{
		 	throw new IllegalArgumentException("Unknown script element")
		 }
	}
	
	def isDefinedAt(e:ScriptElement[State]):Boolean = {
		childs.find{_.isDefinedAt(e)}.isDefined
	}
	
}

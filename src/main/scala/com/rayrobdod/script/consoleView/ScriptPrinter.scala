package com.rayrobdod.script
package consoleView

import java.io.{Reader, Writer}
import scala.collection.immutable.{Seq, Set, Map}

trait ScriptPrinter[State] {
	
	/**
	 * @param out where output gets writen
	 * @param in where input gets read from
	 * @param s state before the action gets performed
	 * @param e the action performed
	 * @param state after the action gets performed
	 */
	def apply(
			out:Writer,
			in:Reader,
			recurser:ScriptPrinter[State],
			s:State,
			e:ScriptElement[State]
	):State
	
	
	/** 
	 * Returns true if apply will produce a reasonable response if
	 * given the provided ScriptElement
	 */
	def isDefinedAt(e:ScriptElement[State]):Boolean
}

/**
 * Applies function parameters to the first child which isDefinedAt
 * returns true.
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

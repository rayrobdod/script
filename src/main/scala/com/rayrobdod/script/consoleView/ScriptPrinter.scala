/*
	Copyright (c) 2015, Raymond Dodge
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
		* Redistributions of source code must retain the above copyright
		  notice, this list of conditions and the following disclaimer.
		* Redistributions in binary form must reproduce the above copyright
		  notice, this list of conditions and the following disclaimer in the
		  documentation and/or other materials provided with the distribution.
		* Neither the name "<PRODUCT NAME>" nor the names of its contributors
		  may be used to endorse or promote products derived from this software
		  without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
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
		childs.exists{_.isDefinedAt(e)}
	}
	
}

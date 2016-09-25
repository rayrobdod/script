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

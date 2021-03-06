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
 * A [[com.rayrobdod.script.consoleView.ScriptPrinter ScriptPrinter]] that can handle the ScriptElements in the
 * [[com.rayrobdod.script]] package.
 * 
 * @constructor
 * @param setFlag a function to use when handling the [[com.rayrobdod.script.SetFlag SetFlag]] ScriptElement. 
 */
class BaseScriptPrinter[State](
		setFlag:(State, String, Int) => State
) extends ScriptPrinter[State] {
	
	override def apply(
			out:Writer,
			in:Reader,
			recurser:ScriptPrinter[State],
			s:State,
			e:ScriptElement[State]
	):State = {
		if (e.use(s)) {	e match {
			case Group(elems, _) => {
				elems.foldLeft(s){recurser.apply(out, in, recurser, _, _)}
			}
			case Speak(speaker, _, words, _) => {
				out.write('\t')
				out.write(speaker)
				out.write(":\n")
				printWithWrapping(out, words)
				out.write('\n')
				out.flush()
				
				// wait for user to press '\n'
				var c:Int = 0;
				do { c = in.read() } while (c != '\r')
				
				s
			}
			case SetFlag(flagName, setTo, _) => {
				setFlag(s, flagName, setTo)
			}
			case YesNo(flagName, _) => {
				out.write("\t(y/n): ")
				out.flush()
				var c:Char = 0;
				do {
					c = in.read().toChar.toLower
				} while (c != 'y' && c != 'n')
				
				setFlag(s, flagName, if (c == 'y') {1} else {0})
			}
			case Options(allOptions, _) => {
				val currentOptions = allOptions.filter{x =>
					x._2.use(s)
				}
				
				out.write("\tChoose one:\n")
				currentOptions.zipWithIndex.foreach{x =>
					out.write(x._2.toString)
					out.write(": ")
					out.write(x._1._1)
					out.write('\n')
				}
				out.write("> ")
				out.flush()
				
				var res = -1
				do {
					res = readInt(in)
				} while (res <= 0 && currentOptions.size < res)
				
				recurser.apply(out, in, recurser, s, currentOptions(res)._2)
			}
			case GoTo(href, _) => {
				val value = href.apply()
				
				recurser.apply(out, in, recurser, s, value)
			}
			// includes NoOp.
			case _ => s	
		}} else {s}
	}
	
	override def isDefinedAt(e:ScriptElement[State]):Boolean = {
		e match {
			case Group(_,_) => true
			case Speak(_,_,_,_) => true
			case SetFlag(_,_,_) => true
			case YesNo(_,_) => true
			case Options(_,_) => true
			case GoTo(_,_) => true
			case NoOp => true
			case _ => false
		}
	}
	
	
	
	
	@scala.annotation.tailrec
	private def printWithWrapping(out:Writer, s:String):Unit = {
		val consoleWidth = 80
		
		val splitAt = s.take(consoleWidth).lastIndexOf(' ')
		
		if (s.length < consoleWidth) {
			out.write(s)
		} else if (-1 == splitAt) {
			out.write(s.take(consoleWidth))
			out.write('\n')
			printWithWrapping(out, s.drop(consoleWidth))
		} else {
			out.write(s.take(splitAt))
			out.write('\n')
			printWithWrapping(out, s.drop(splitAt + 1))
		}
	}
	
	private def readInt(in:Reader):Int = {
		val NUMBER_BASE = 10
		var s:Int = 0
		var c:Char = '0'
		while (c.isDigit || c == '\n') {
			c = in.read.toChar
			if (c.isDigit) {s = (s * NUMBER_BASE) + Character.digit(c, NUMBER_BASE)}
		}
		s
	}
}

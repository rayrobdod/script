package com.rayrobdod.script
package consoleView

import java.io.{Reader, Writer}
import scala.collection.immutable.{Seq, Set, Map}


class BaseScriptPrinter[State](
		out:Writer,
		in:Reader,
		setFlag:(State, String, Int) => State
) {
	def apply(s:State, e:ScriptElement[State]):State = {
		if (e.use(s)) {	e match {
			case Group(elems, _) => {
				elems.foldLeft(s){this.apply}
			}
			case Speak(speaker, _, words, _) => {
				out.write('\t')
				out.write(speaker)
				out.write(":\n")
				printWithWrapping(words)
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
			case Options(optionNames, elems, _) => {
				out.write("\tChoose one:\n")
				optionNames.zipWithIndex.foreach{x =>
					out.write(x._2.toString)
					out.write(": ")
					out.write(x._1)
					out.write('\n')
				}
				out.flush()
				
				var res = -1
				do {
					res = readInt()
				} while (res <= 0 && elems.size < res)
				
				this.apply(s, elems(res))
			}
			case _ => s	
		}} else {s}
	}
	
	def isDefinedAt(e:ScriptElement[State]):Boolean = {
		e match {
			case Group(_,_) => true
			case Speak(_,_,_,_) => true
			case SetFlag(_,_,_) => true
			case YesNo(_,_) => true
			case Options(_,_,_) => true
			case NoOp => true
			case _ => false
		}
	}
	
	
	
	
	@scala.annotation.tailrec
	private def printWithWrapping(s:String):Unit = {
		val consoleWidth = 80
		
		val splitAt = s.take(consoleWidth).lastIndexOf(' ')
		
		if (s.length < consoleWidth) {
			out.write(s)
		} else if (-1 == splitAt) {
			out.write(s.take(consoleWidth))
			out.write('\n')
			printWithWrapping(s.drop(consoleWidth))
		} else {
			out.write(s.take(splitAt))
			out.write('\n')
			printWithWrapping(s.drop(splitAt + 1))
		}
	}
	
	private def readInt():Int = {
		var s:Int = 0
		var c:Char = '0'
		while (c.isDigit) {
			c = in.read.toChar
			if (c.isDigit) {s = (s * 10) + Character.digit(c, 10)}
		}
		s
	}
}
